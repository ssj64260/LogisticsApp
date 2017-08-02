package com.msqsoft.aboutapp.service;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.io.EOFException;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 网络请求拦截器
 */

public class HttpInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public static final int NONE = 0;
    public static final int BASIC = 1;
    public static final int HEADERS = 2;
    public static final int BODY = 3;

    @IntDef({NONE, BASIC, HEADERS, BODY})
    @Retention(RetentionPolicy.SOURCE)
    @interface LevelType {

    }

    private volatile int level = NONE;

    /**
     * Change the level at which this interceptor logs.
     */
    public HttpInterceptor setLevel(@LevelType int level) {
        this.level = level;
        return this;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        final StringBuilder httpMessage = new StringBuilder();
        String responseText = "";

        final int level = this.level;

        final Request request = chain.request();
        if (level == NONE) {
            return chain.proceed(request);
        }

        final boolean logBody = level == BODY;
        final boolean logHeaders = logBody || level == HEADERS;

        final RequestBody requestBody = request.body();
        final boolean hasRequestBody = requestBody != null;

        final Connection connection = chain.connection();
        final Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        String requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol;
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        httpMessage.append(requestStartMessage).append("\n");

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    httpMessage.append("Content-Type: ").append(requestBody.contentType()).append("\n");
                }
                if (requestBody.contentLength() != -1) {
                    httpMessage.append("Content-Length: ").append(requestBody.contentLength()).append("\n");
                }
            }

            final Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                final String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    httpMessage.append(name).append(": ").append(headers.value(i)).append("\n");
                }
            }

            if (!logBody || !hasRequestBody) {
                httpMessage.append("--> END ").append(request.method()).append("\n");
            } else if (bodyEncoded(request.headers())) {
                httpMessage.append("--> END ").append(request.method()).append(" (encoded body omitted)").append("\n");
            } else {
                final Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                final MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                httpMessage.append("\n");
                if (isPlaintext(buffer)) {
                    httpMessage.append(buffer.readString(charset)).append("\n");
                    httpMessage.append("--> END ").append(request.method())
                            .append(" (").append(requestBody.contentLength()).append("-byte body)").append("\n");
                } else {
                    httpMessage.append("--> END ").append(request.method())
                            .append(" (binary ").append(requestBody.contentLength()).append("-byte body omitted)").append("\n");
                }
            }
        }

        final long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            httpMessage.append("<-- HTTP FAILED: ").append(e).append("\n");
            throw e;
        }
        final long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        final ResponseBody responseBody = response.body();
        final long contentLength = responseBody.contentLength();
        final String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        httpMessage.append("<-- ").append(response.code())
                .append(" ").append(response.message())
                .append(" ").append(response.request().url())
                .append(" (").append(tookMs).append("ms").append(!logHeaders ? ", " + bodySize + " body" : "").append(')').append("\n");

        if (logHeaders) {
            final Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                httpMessage.append(headers.name(i)).append(": ").append(headers.value(i)).append("\n");
            }

            if (!logBody || !HttpHeaders.hasBody(response)) {
                httpMessage.append("<-- END HTTP").append("\n");
            } else if (bodyEncoded(response.headers())) {
                httpMessage.append("<-- END HTTP (encoded body omitted)").append("\n");
            } else {
                final BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                final Buffer buffer = source.buffer();

                Charset charset = UTF8;
                final MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                if (!isPlaintext(buffer)) {
                    httpMessage.append("\n");
                    httpMessage.append("<-- END HTTP (binary ").append(buffer.size()).append("-byte body omitted)").append("\n");
                    return response;
                }

                if (contentLength != 0) {
                    responseText = buffer.clone().readString(charset);
                    httpMessage.append("\n");
                    httpMessage.append(responseText).append("\n");
                }

                httpMessage.append("<-- END HTTP (").append(buffer.size()).append("-byte body)").append("\n");
            }
        }

        Logger.d(httpMessage.toString());
//        Logger.json(responseText);

        return response;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}
