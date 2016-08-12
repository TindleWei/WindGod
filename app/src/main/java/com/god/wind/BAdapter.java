package com.god.wind;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * describe
 * created by tindle
 * created time 16/8/10 下午1:48
 */
public class BAdapter extends RecyclerView.Adapter<BAdapter.ViewHolder> {

    private ArrayList<String> titles;

    public BAdapter(ArrayList<String> titles) {
        this.titles = titles;
    }

    @Override
    public BAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_b, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tv_title.setText(titles.get(position));
        holder.tv_title.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "text title", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        holder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Hi, should we eidt it?", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
        holder.iv_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Delete it!!!", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

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

    public void swipedDone(RecyclerView.ViewHolder holder, int flag){
        if (flag == 0) { //left
            ((BAdapter.ViewHolder)holder).rightBackground.setVisibility(View.VISIBLE);
            ((BAdapter.ViewHolder)holder).leftBackground.setVisibility(View.GONE);
            ((BAdapter.ViewHolder)holder).clipForeground.setVisibility(View.GONE);
        } else if (flag == 1) { //right
            ((BAdapter.ViewHolder)holder).rightBackground.setVisibility(View.GONE);
            ((BAdapter.ViewHolder)holder).leftBackground.setVisibility(View.VISIBLE);
            ((BAdapter.ViewHolder)holder).clipForeground.setVisibility(View.GONE);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;

        public ImageView iv_add;
        public ImageView iv_delete;

        public LinearLayout leftBackground;
        public LinearLayout rightBackground;
        public LinearLayout clipForeground;

        public ViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            iv_add = (ImageView) view.findViewById(R.id.iv_add);
            iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
            leftBackground = (LinearLayout) view.findViewById(R.id.leftBackground);
            rightBackground = (LinearLayout) view.findViewById(R.id.rightBackground);
            clipForeground = (LinearLayout) view.findViewById(R.id.clipForeground);
        }
    }

    public void showSwipedView() {

    }
}
