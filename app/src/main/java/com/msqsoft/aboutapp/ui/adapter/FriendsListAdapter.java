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
import com.msqsoft.aboutapp.model.CourierBean;
import com.msqsoft.aboutapp.model.FriendBean;
import com.msqsoft.aboutapp.widget.imageloader.GlideCircleTransform;
import com.msqsoft.aboutapp.widget.imageloader.ImageLoaderFactory;

import java.util.List;

/**
 * 好友列表
 */

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendsListHolder>{

    private Context mContext;
    private List<FriendBean> mList;
    private OnListClickListener mListener;

    private GlideCircleTransform mTransform;

    public FriendsListAdapter(Context context, List<FriendBean> list){
        mContext = context;
        mList = list;
        mTransform = new GlideCircleTransform();
    }

    public void setOnListClickListener(OnListClickListener onListClickListener){
        mListener = onListClickListener;
    }

    @Override
    public FriendsListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_friend, parent, false);
        return new FriendsListHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendsListHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class FriendsListHolder extends RecyclerView.ViewHolder{

        private View root;
        private ImageView mIv_friend_avatar, mIv_courier_icon;
        private TextView mTv_friend_name, mTv_friend_signature, mTv_courier_name, mTv_courier_distance;
        private LinearLayout mLl_courier;

        public FriendsListHolder(View itemView) {
            super(itemView);
            root = itemView;
            mIv_friend_avatar = (ImageView) root.findViewById(R.id.iv_friend_avatar);
            mTv_friend_name = (TextView) root.findViewById(R.id.tv_friend_name);
            mTv_friend_signature = (TextView) root.findViewById(R.id.tv_friend_signature);
            mLl_courier = (LinearLayout) root.findViewById(R.id.ll_courier);
            mIv_courier_icon = (ImageView) root.findViewById(R.id.iv_courier_icon);
            mTv_courier_name = (TextView) root.findViewById(R.id.tv_courier_name);
            mTv_courier_distance = (TextView) root.findViewById(R.id.tv_courier_distance);
        }

        public void bindView(final int position){

            final FriendBean friendBean = mList.get(position);
            final String avatar = friendBean.getAvatar();
            final String user_nicename = friendBean.getUser_nicename();

            ImageLoaderFactory.getLoader().loadImageFitCenter(mContext, mIv_friend_avatar, avatar, mTransform);
            mTv_friend_name.setText(user_nicename);

            final String user_type = friendBean.getUser_type();
            if(user_type.equals("2")){
                final String signature = friendBean.getSignature();

                mTv_friend_signature.setVisibility(View.VISIBLE);
                mTv_friend_signature.setText(signature);
            }else if(user_type.equals("3")){
                final CourierBean courier_attrs = friendBean.getCourier_attrs();
                final String name = courier_attrs.getName();
                final String icon = courier_attrs.getIcon();
                final String longitude = courier_attrs.getLongitude();
                final String latitude = courier_attrs.getLatitude();

                mLl_courier.setVisibility(View.VISIBLE);
                ImageLoaderFactory.getLoader().loadImageFitCenter(mContext, mIv_courier_icon, icon);
                mTv_courier_name.setText(name);
                mTv_courier_distance.setText("800m");
            }

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(position);
                }
            });
        }
    }
}
