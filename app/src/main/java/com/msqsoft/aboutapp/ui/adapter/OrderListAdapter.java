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
import com.msqsoft.aboutapp.model.OrderDetailBean;
import com.msqsoft.aboutapp.widget.imageloader.GlideCircleTransform;
import com.msqsoft.aboutapp.widget.imageloader.ImageLoaderFactory;

import java.util.List;

/**
 * 订单列表
 */

public class OrderListAdapter extends RecyclerView.Adapter {

    private OnListClickListener mListClick;
    private Context mContext;
    private List<OrderDetailBean> mList;
    private LayoutInflater mInflater;

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            if (mListClick != null) {
                mListClick.onItemClick(position);
            }
        }
    };

    public OrderListAdapter(Context context, List<OrderDetailBean> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListViewHolder(mInflater.inflate(R.layout.item_main_my_order_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindNoHeadItem((ListViewHolder) holder, position);
    }

    private void bindNoHeadItem(ListViewHolder holder, final int position) {
        final OrderDetailBean order = mList.get(position);
        final String avatarUrl = order.getAvatar();
        final String nickname = order.getUser_nicename();
        final String placeFrom = order.getSender_area_name();
        final String placeTo = order.getRecipient_area_name();
        final String goodsType = order.getGoods_kind_name();

        ImageLoaderFactory.getLoader().loadImageCenterCrop(mContext, holder.ivImage, avatarUrl, new GlideCircleTransform());
        holder.tvTitle.setText(nickname);
        holder.llPlace.setVisibility(View.VISIBLE);
        holder.tvPlaceFrom.setText(placeFrom);
        holder.tvPlaceTo.setText(placeTo);
        holder.tvSubTitle.setText(goodsType);
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(mClick);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class ListViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private ImageView ivImage;
        private TextView tvTitle;
        private LinearLayout llPlace;
        private TextView tvPlaceFrom;
        private TextView tvPlaceTo;
        private TextView tvSubTitle;
        private TextView tvStatus;
        private TextView tvTips;

        private ListViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            llPlace = (LinearLayout) itemView.findViewById(R.id.ll_place);
            tvPlaceFrom = (TextView) itemView.findViewById(R.id.tv_place_from);
            tvPlaceTo = (TextView) itemView.findViewById(R.id.tv_place_to);
            tvSubTitle = (TextView) itemView.findViewById(R.id.tv_sub_title);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            tvTips = (TextView) itemView.findViewById(R.id.tv_tips);
        }
    }

    public void setOnListClickListener(OnListClickListener listClick) {
        this.mListClick = listClick;
    }
}
