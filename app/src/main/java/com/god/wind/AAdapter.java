package com.god.wind;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * describe
 * created by tindle
 * created time 16/8/10 下午1:48
 */
public class AAdapter extends RecyclerView.Adapter<AAdapter.ViewHolder> {

    private ArrayList<String> titles;

    public AAdapter(ArrayList<String> titles) {
        this.titles = titles;
    }

    @Override
    public AAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_a, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_title.setText(titles.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public void addItem(String title) {
        titles.add(title);
        notifyItemInserted(titles.size());
    }

    public void removeItem(int position) {
        titles.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, titles.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;

        public ViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
        }
    }
}
