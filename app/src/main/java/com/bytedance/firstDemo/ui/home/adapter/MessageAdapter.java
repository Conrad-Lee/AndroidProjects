//// MessageAdapter.java
//package com.bytedance.firstDemo.ui.home.adapter;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bytedance.firstDemo.R;
//
//import java.util.List;
//
//public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private static final int VIEW_TYPE_HORIZONTAL = 1;
//    private static final int VIEW_TYPE_VERTICAL = 2;
//
//    private List<Object> itemList;
//
//    public MessageAdapter(List<Object> itemList) {
//        this.itemList = itemList;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (itemList.get(position) instanceof String) {  // 假设横向部分是字符串数据
//            return VIEW_TYPE_HORIZONTAL;
//        } else {
//            return VIEW_TYPE_VERTICAL;
//        }
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == VIEW_TYPE_HORIZONTAL) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal, parent, false);
//            return new HorizontalViewHolder(view);
//        } else {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
//            return new VerticalViewHolder(view);
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (holder instanceof HorizontalViewHolder) {
//            // 绑定横向内容
//        } else if (holder instanceof VerticalViewHolder) {
//            // 绑定纵向内容
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return itemList.size();
//    }
//
//    static class HorizontalViewHolder extends RecyclerView.ViewHolder {
//        public HorizontalViewHolder(View itemView) {
//            super(itemView);
//            // 初始化横向布局组件
//        }
//    }
//
//    static class VerticalViewHolder extends RecyclerView.ViewHolder {
//        public VerticalViewHolder(View itemView) {
//            super(itemView);
//            // 初始化纵向布局组件
//        }
//    }
//}
