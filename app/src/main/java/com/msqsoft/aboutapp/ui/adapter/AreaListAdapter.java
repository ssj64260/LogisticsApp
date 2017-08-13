package com.msqsoft.aboutapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msqsoft.aboutapp.R;
import com.msqsoft.aboutapp.model.AreaBean;

import java.util.List;

/**
 * 省市区列表
 */

public class AreaListAdapter extends RecyclerView.Adapter<AreaListAdapter.AreaListHolder>{
    private Context mContext;
    private List<AreaBean> mList;
    private OnListClickListener mListener;

    public AreaListAdapter(Context context, List<AreaBean> list){
        mContext = context;
        mList = list;
    }

    public void setOnListClickListener(OnListClickListener onListClickListener){
        mListener = onListClickListener;
    }

    @Override
    public AreaListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_area, parent, false);
        return new AreaListHolder(view);
    }

    @Override
    public void onBindViewHolder(AreaListHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class AreaListHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView mTv_area_name;

        public AreaListHolder(View itemView){
            super(itemView);
            mView = itemView;
            mTv_area_name = (TextView) mView.findViewById(R.id.tv_area_name);
        }

        public void bindView(final int position) {
            final String area_name = mList.get(position).getName();

            mTv_area_name.setText(area_name);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(position);
                }
            });
        }
    }
}
