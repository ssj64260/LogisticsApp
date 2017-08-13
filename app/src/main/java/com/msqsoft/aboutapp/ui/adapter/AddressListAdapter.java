package com.msqsoft.aboutapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msqsoft.aboutapp.R;
import com.msqsoft.aboutapp.model.AddressBean;

import java.util.List;

/**
 * 用户地址列表
 */

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.AddressListHolder>{
    private Context mContext;
    private List<AddressBean> mList;
    private OnListClickListener mListener;

    public AddressListAdapter(Context context, List<AddressBean> list){
        mContext = context;
        mList = list;
    }

    public void setOnListClickListener(OnListClickListener onListClickListener){
        mListener = onListClickListener;
    }

    @Override
    public AddressListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_address, parent, false);
        return new AddressListHolder(view);
    }

    @Override
    public void onBindViewHolder(AddressListHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class AddressListHolder extends RecyclerView.ViewHolder {

        private View root;
        private TextView mTv_address_user_name, mTv_address_user_mobile, mTv_address_user_address, mTv_address_user_address_details,
                mTv_is_default;
        private ImageView mIv_is_default;
        private LinearLayout mLl_address_user, mLl_is_default, mLl_edit_address, mLl_delete_address;

        public AddressListHolder(View itemView) {
            super(itemView);
            root = itemView;
            mLl_address_user = (LinearLayout) root.findViewById(R.id.ll_address_user);
            mTv_address_user_name = (TextView) root.findViewById(R.id.tv_address_user_name);
            mTv_address_user_mobile = (TextView) root.findViewById(R.id.tv_address_user_mobile);
            mTv_address_user_address = (TextView) root.findViewById(R.id.tv_address_user_address);
            mTv_address_user_address_details = (TextView) root.findViewById(R.id.tv_address_user_address_details);
            mLl_is_default = (LinearLayout) root.findViewById(R.id.ll_is_default);
            mIv_is_default = (ImageView) root.findViewById(R.id.iv_is_default);
            mTv_is_default = (TextView) root.findViewById(R.id.tv_is_default);
            mLl_edit_address = (LinearLayout) root.findViewById(R.id.ll_edit_address);
            mLl_delete_address = (LinearLayout) root.findViewById(R.id.ll_delete_address);
        }

        public void bindView(final int position) {
            final String name = mList.get(position).getName();
            final String mobile = mList.get(position).getMobile();
            final String area = mList.get(position).getArea();
            final String detail = mList.get(position).getDetail();
            final String status = mList.get(position).getStatus();

            mTv_address_user_name.setText(name);
            mTv_address_user_mobile.setText(mobile);
            mTv_address_user_address.setText(area);
            mTv_address_user_address_details.setText(detail);
            if(status.equals("1")){
                mIv_is_default.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_radio_nomal));
                mTv_is_default.setText(mContext.getString(R.string.title_set_default_address));
            }else if(status.equals("2")){
                mIv_is_default.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_radio_selected));
                mTv_is_default.setText(mContext.getString(R.string.title_default_address));
            }

            mLl_address_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(position);
                }
            });
            mLl_is_default.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onTagClick(OnListClickListener.BUTTON, position);
                }
            });
            mLl_edit_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onTagClick(OnListClickListener.TEXTVIEW, position);
                }
            });
            mLl_delete_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onTagClick(OnListClickListener.LINEARLAYOUT, position);
                }
            });
        }
    }
}
