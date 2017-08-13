package com.msqsoft.aboutapp.service;

import com.msqsoft.aboutapp.model.ServiceResult;
import com.msqsoft.aboutapp.utils.ToastMaster;
import com.orhanobut.logger.Logger;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static com.msqsoft.aboutapp.config.Config.RESULT_CODE_SUCCESS;
import static com.msqsoft.aboutapp.config.Config.RESULT_CODE_TOKEN_INCORRECT;

/**
 * 请求回调，处理错误码
 */

public abstract class MyObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull T result) {
        if (result instanceof ServiceResult) {
            final ServiceResult serviceResult = (ServiceResult) result;
            if (RESULT_CODE_SUCCESS.equals(serviceResult.getResultCode())) {
                onSuccess(result);
            } else if (RESULT_CODE_TOKEN_INCORRECT.equals(serviceResult.getResultCode())) {
                ToastMaster.toast(serviceResult.getResultMsg());
                onTokenIncorrect();
            } else {
                onError(serviceResult.getResultMsg());
            }
        } else {
            onSuccess(result);
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        onError(e.getMessage());
    }

    @Override
    public void onComplete() {

    }

    public void onTokenIncorrect() {

    }

    public void onError(String errorMsg) {
        Logger.e(errorMsg);
    }

    public abstract void onSuccess(@NonNull T result);
}
