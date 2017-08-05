package com.msqsoft.aboutapp.ui.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.msqsoft.aboutapp.R;
import com.msqsoft.aboutapp.app.BaseAppCompatActivity;
import com.msqsoft.aboutapp.widget.imageloader.ImageLoaderFactory;

/**
 * 图片展示
 */

public class ShowImageActivity extends BaseAppCompatActivity {

    public static final String URL_IMAGE = "url_image";

    private PhotoView pvPicture;

    private String mImageUrl;

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_toolbar_back:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        initData();
        initView();

    }

    private void initData() {
        mImageUrl = getIntent().getStringExtra(URL_IMAGE);
    }

    private void initToolbar() {
        final ImageView ivBack = (ImageView) findViewById(R.id.iv_toolbar_back);
        final TextView tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);

        ivBack.setOnClickListener(click);
        tvTitle.setText(getString(R.string.title_show_image));
    }

    private void initView() {
        initToolbar();
        pvPicture = (PhotoView) findViewById(R.id.pv_picture);

        if (!TextUtils.isEmpty(mImageUrl)) {
            ImageLoaderFactory.getLoader().loadImageFitCenter(this, pvPicture, mImageUrl);
        }

    }
}
