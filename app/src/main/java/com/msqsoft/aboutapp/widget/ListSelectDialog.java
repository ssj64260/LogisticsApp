package com.msqsoft.aboutapp.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.msqsoft.aboutapp.R;
import com.msqsoft.aboutapp.ui.adapter.OnListClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 列表选择对话框
 */
public class ListSelectDialog extends AlertDialog {

    private OnListClickListener mListClick;

    private RecyclerView rvList;
    private List<String> mListData;
    private ListSelectAdapter mAdapter;

    public ListSelectDialog(Context context) {
        super(context, R.style.loadingDialogStyle);
        mListData = new ArrayList<>();
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams attr = window.getAttributes();
            if (attr != null) {
                attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_list_select);

        mAdapter = new ListSelectAdapter();
        rvList = (RecyclerView) findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvList.setHasFixedSize(true);
        rvList.setAdapter(mAdapter);

        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public void setListData(List<String> listData) {
        mListData.clear();
        mListData.addAll(listData);
        mAdapter.notifyDataSetChanged();
    }

    private class ListSelectAdapter extends RecyclerView.Adapter {
        private LayoutInflater mInflater;

        private ListSelectAdapter() {
            mInflater = LayoutInflater.from(getContext());
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ListViewHolder(mInflater.inflate(R.layout.item_list_select, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            bindNoHeadItem((ListViewHolder) holder, position);
        }

        private void bindNoHeadItem(ListViewHolder holder, final int position) {
            final String name = mListData.get(position);
            holder.tvName.setTag(position);
            holder.tvName.setText(name);
            holder.tvName.setOnClickListener(click);
            if (position < mListData.size() - 1) {
                holder.viewLine.setVisibility(View.VISIBLE);
            } else {
                holder.viewLine.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return mListData.size();
        }

        private class ListViewHolder extends RecyclerView.ViewHolder {

            private TextView tvName;
            private View viewLine;

            private ListViewHolder(View itemView) {
                super(itemView);
                tvName = (TextView) itemView.findViewById(R.id.tv_name);
                viewLine = itemView.findViewById(R.id.view_line);
            }
        }

        private View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                if (mListClick != null) {
                    mListClick.onItemClick(position);
                }
            }
        };
    }

    public void setListClick(OnListClickListener listClick) {
        this.mListClick = listClick;
    }
}
