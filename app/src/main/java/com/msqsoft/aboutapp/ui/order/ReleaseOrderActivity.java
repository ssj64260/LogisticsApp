package com.msqsoft.aboutapp.ui.order;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msqsoft.aboutapp.R;
import com.msqsoft.aboutapp.app.BaseAppCompatActivity;
import com.msqsoft.aboutapp.utils.ToastMaster;

/**
 * 发布订单
 */

public class ReleaseOrderActivity extends BaseAppCompatActivity {

    private LinearLayout mLl_edit_sender_address, mLl_sender_details, mLl_edit_receiver_address, mLl_receiver_details,
            mLl_goods_details_choose, mIl_goods_details_is_choose, mLl_goods_details_choose_express_company,
            mLl_goods_details_choose_good_style, mLl_goods_details_choose_good_size, mLl_goods_details_choose_good_weight,
            mLl_remark_image, mLl_issuance_way_now;
    private TextView mTv_edit_sender_address, mTv_sender_name, mTv_sender_mobile, mTv_sender_address, mTv_sender_address_details,
            mTv_edit_receiver_address, mTv_receiver_name, mTv_receiver_mobile, mTv_receiver_address, mTv_receiver_address_details,
            mTv_goods_details_choose_express_company_name, mTv_goods_details_choose_good_style, mTv_goods_details_choose_good_size,
            mTv_goods_details_choose_good_weight, mTv_goods_details_choose_remark, mTv_issuance_way_book_time,
            mTv_issuance_way_reward_money, mTv_release_order, mTv_send_to_courier;
    private ImageView mIv_goods_details_choose, mIv_issuance_way_now, mIv_issuance_way_book, mIv_issuance_way_reward;
    private RelativeLayout mRl_goods_details_choose_remark, mRl_issuance_way_book, mRl_issuance_way_reward;
    private HorizontalScrollView mHsv_remark_image;

    private boolean isChoose = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_order);

        initView();
    }

    private void initView(){

        initToolbar();
        mLl_edit_sender_address = (LinearLayout) findViewById(R.id.ll_edit_sender_address);
        /**
         * 获得返回的寄件人信息，更改下面两个控件的visibility
         */
        mTv_edit_sender_address = (TextView) findViewById(R.id.tv_edit_sender_address);
        mLl_sender_details = (LinearLayout) findViewById(R.id.ll_sender_details);
        mTv_sender_name = (TextView) findViewById(R.id.tv_sender_name);
        mTv_sender_mobile = (TextView) findViewById(R.id.tv_sender_mobile);
        mTv_sender_address = (TextView) findViewById(R.id.tv_sender_address);
        mTv_sender_address_details = (TextView) findViewById(R.id.tv_sender_address_details);

        mLl_edit_receiver_address = (LinearLayout) findViewById(R.id.ll_edit_receiver_address);
        /**
         * 获得返回的收件人信息，更改下面两个控件的visibility
         */
        mTv_edit_receiver_address = (TextView) findViewById(R.id.tv_edit_receiver_address);
        mLl_receiver_details = (LinearLayout) findViewById(R.id.ll_receiver_details);
        mTv_receiver_name = (TextView) findViewById(R.id.tv_receiver_name);
        mTv_receiver_mobile = (TextView) findViewById(R.id.tv_receiver_mobile);
        mTv_receiver_address = (TextView) findViewById(R.id.tv_receiver_address);
        mTv_receiver_address_details = (TextView) findViewById(R.id.tv_receiver_address_details);

        mLl_goods_details_choose = (LinearLayout) findViewById(R.id.ll_goods_details_choose);
        mIv_goods_details_choose = (ImageView) findViewById(R.id.iv_goods_details_choose);
        mIl_goods_details_is_choose = (LinearLayout) findViewById(R.id.ll_goods_details_is_choose);
        mLl_goods_details_choose_express_company = (LinearLayout) findViewById(R.id.ll_goods_details_choose_express_company);
        /**
         * 获得返回的快递公司信息，更改下面控件的信息
         */
        mTv_goods_details_choose_express_company_name = (TextView) findViewById(R.id.tv_goods_details_choose_express_company_name);
        mLl_goods_details_choose_good_style = (LinearLayout) findViewById(R.id.ll_goods_details_choose_good_style);
        /**
         * 获得返回的货物类型信息，更改下面控件的信息
         */
        mTv_goods_details_choose_good_style = (TextView) findViewById(R.id.tv_goods_details_choose_good_style);
        mLl_goods_details_choose_good_size = (LinearLayout) findViewById(R.id.ll_goods_details_choose_good_size);
        /**
         * 获得返回的货物大小信息，更改下面控件的信息
         */
        mTv_goods_details_choose_good_size = (TextView) findViewById(R.id.tv_goods_details_choose_good_size);
        mLl_goods_details_choose_good_weight = (LinearLayout) findViewById(R.id.ll_goods_details_choose_good_weight);
        /**
         * 获得返回的货物重量信息，更改下面控件的信息
         */
        mTv_goods_details_choose_good_weight = (TextView) findViewById(R.id.tv_goods_details_choose_good_weight);
        mRl_goods_details_choose_remark = (RelativeLayout) findViewById(R.id.rl_goods_details_choose_remark);
        /**
         * 获得返回的添加备注信息，更改下面三个控件的信息
         */
        mTv_goods_details_choose_remark = (TextView) findViewById(R.id.tv_goods_details_choose_remark);
        mHsv_remark_image = (HorizontalScrollView) findViewById(R.id.hsv_remark_image);
        mLl_remark_image = (LinearLayout) findViewById(R.id.ll_remark_image);

        mLl_issuance_way_now = (LinearLayout) findViewById(R.id.ll_issuance_way_now);
        mIv_issuance_way_now = (ImageView) findViewById(R.id.iv_issuance_way_now);
        mRl_issuance_way_book = (RelativeLayout) findViewById(R.id.rl_issuance_way_book);
        /**
         * 获得返回的发布时间，更改下面控件的信息
         */
        mTv_issuance_way_book_time = (TextView) findViewById(R.id.tv_issuance_way_book_time);
        mIv_issuance_way_book = (ImageView) findViewById(R.id.iv_issuance_way_book);
        mRl_issuance_way_reward = (RelativeLayout) findViewById(R.id.rl_issuance_way_reward);
        /**
         * 获得返回的悬赏金额，更改下面控件的信息
         */
        mTv_issuance_way_reward_money = (TextView) findViewById(R.id.tv_issuance_way_reward_money);
        mIv_issuance_way_reward = (ImageView) findViewById(R.id.iv_issuance_way_reward);
        mTv_release_order = (TextView) findViewById(R.id.tv_release_order);
        mTv_send_to_courier = (TextView) findViewById(R.id.tv_send_to_courier);

        mLl_edit_sender_address.setOnClickListener(click);
        mLl_edit_receiver_address.setOnClickListener(click);
        mLl_goods_details_choose.setOnClickListener(click);
        mLl_goods_details_choose_express_company.setOnClickListener(click);
        mLl_goods_details_choose_good_style.setOnClickListener(click);
        mLl_goods_details_choose_good_size.setOnClickListener(click);
        mLl_goods_details_choose_good_weight.setOnClickListener(click);
        mRl_goods_details_choose_remark.setOnClickListener(click);
        mLl_issuance_way_now.setOnClickListener(click);
        mRl_issuance_way_book.setOnClickListener(click);
        mRl_issuance_way_reward.setOnClickListener(click);
        mTv_release_order.setOnClickListener(click);
        mTv_send_to_courier.setOnClickListener(click);
    }

    private void initToolbar() {

        final ImageView ivBack = (ImageView) findViewById(R.id.iv_toolbar_back);
        final TextView tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);

        ivBack.setOnClickListener(click);
        tvTitle.setText(getString(R.string.title_release_order));
    }

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_toolbar_back:
                    hideKeyboard();
                    onBackPressed();
                    break;
                case R.id.ll_edit_sender_address:
                    ToastMaster.toast("点击寄件人");
                    break;
                case R.id.ll_edit_receiver_address:
                    ToastMaster.toast("点击收件人");
                    break;
                case R.id.ll_goods_details_choose:
                    if(isChoose){
                        mIv_goods_details_choose.setImageDrawable(getResources().getDrawable(R.drawable.list_bottom));
                        mIl_goods_details_is_choose.setVisibility(View.GONE);
                    }else {
                        mIv_goods_details_choose.setImageDrawable(getResources().getDrawable(R.drawable.list_top));
                        mIl_goods_details_is_choose.setVisibility(View.VISIBLE);
                    }
                    isChoose = !isChoose;
                    break;
                case R.id.ll_goods_details_choose_express_company:
                    ToastMaster.toast("点击快递公司");
                    break;
                case R.id.ll_goods_details_choose_good_style:
                    ToastMaster.toast("点击货物类型");
                    break;
                case R.id.ll_goods_details_choose_good_size:
                    ToastMaster.toast("点击货物大小");
                    break;
                case R.id.ll_goods_details_choose_good_weight:
                    ToastMaster.toast("点击货物重量");
                    break;
                case R.id.rl_goods_details_choose_remark:
                    ToastMaster.toast("点击添加备注");
                    break;
                case R.id.ll_issuance_way_now:
                    ToastMaster.toast("点击即时发布");
                    mIv_issuance_way_now.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_selected));
                    mIv_issuance_way_book.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_nomal));
                    mIv_issuance_way_reward.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_nomal));
                    break;
                case R.id.rl_issuance_way_book:
                    ToastMaster.toast("点击预约发布");
                    mIv_issuance_way_now.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_nomal));
                    mIv_issuance_way_book.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_selected));
                    mIv_issuance_way_reward.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_nomal));
                    break;
                case R.id.rl_issuance_way_reward:
                    ToastMaster.toast("点击悬赏发布");
                    mIv_issuance_way_now.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_nomal));
                    mIv_issuance_way_book.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_nomal));
                    mIv_issuance_way_reward.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_selected));
                    break;
                case R.id.tv_release_order:
                    ToastMaster.toast("点击发布订单");
                    break;
                case R.id.tv_send_to_courier:
                    ToastMaster.toast("点击发送给快递员");
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(ReleaseOrderActivity.this);
        dialog.setTitle(getString(R.string.save_draft_dialog_title));
        dialog.setCancelable(true);
        dialog.setPositiveButton(getString(R.string.save_draft_dialog_positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastMaster.toast(getString(R.string.save_draft_dialog_positive_button));
                finish();
            }
        });
        dialog.setNegativeButton(getString(R.string.save_draft_dialog_negative_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastMaster.toast(getString(R.string.save_draft_dialog_negative_button));
                finish();
            }
        });
        dialog.show();
    }
}
