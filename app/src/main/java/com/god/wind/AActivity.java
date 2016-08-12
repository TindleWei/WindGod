package com.god.wind;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

public class AActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> titles = new ArrayList<>();
    private AAdapter adapter;
    private RecyclerView recyclerView;

    private AlertDialog.Builder alertDialog;
    private EditText et_input;
    private View view;
    private int edit_position = -1;
    private boolean flag_swiped = false;
    private boolean add = false;

    private Paint p = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);

        initViews();
        initDialog();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AAdapter(titles);
        recyclerView.setAdapter(adapter);
        titles.add("Jon Snow");
        titles.add("Arya Stark");
        titles.add("Daenerys Targaryen");
        titles.add("Tyrion Lannister");
        titles.add("Bran Stark");
        adapter.notifyDataSetChanged();
        initSwipe();
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                if(position!=edit_position){
                    if(edit_position!=-1 && flag_swiped == true) {
                        adapter.notifyItemChanged(edit_position);
                        flag_swiped = false;
                        edit_position = position;
                    }
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                flag_swiped = true;
                if (direction == ItemTouchHelper.LEFT) {
                    adapter.removeItem(position);
                } else {
                    removeView();
                    edit_position = position;
                    alertDialog.setTitle("Edit Content");
                    et_input.setText(titles.get(position));
                    alertDialog.show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Drawable icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    int height = itemView.getBottom() - itemView.getTop();
                    int width = itemView.getRight() - itemView.getLeft();
                    float iconH = getResources().getDisplayMetrics().density * 28;
                    float iconW = getResources().getDisplayMetrics().density * 28;

                    if (dX > 0) {

                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        p.setColor(Color.parseColor("#FF9800"));
                        c.drawRect(background, p);

                        icon = ContextCompat.getDrawable(getBaseContext(), android.R.drawable.ic_input_add);
                        icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                        // you can also use icon size
                        // int iconWidth = icon.getIntrinsicWidth();
                        // int iconHeight = icon.getIntrinsicHeight();

                        float rate = Math.abs(dX) / width;

                        int iconLeft = (int) (itemView.getLeft() - iconW + width / 3 * rate);
                        int iconTop = (int) (itemView.getTop() + height /2 - iconH/2);
                        int iconRight = (int) (itemView.getLeft() + width / 3 * rate);
                        int iconBottom = (int) (itemView.getBottom() - height/2 + iconH/2);
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        icon.draw(c);

                    } else if (dX < 0) {

                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        p.setColor(Color.parseColor("#E91E63"));
                        c.drawRect(background, p);

                        icon = ContextCompat.getDrawable(getBaseContext(), android.R.drawable.ic_menu_delete);
                        icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

                        float rate = Math.abs(dX) / width;

                        int iconLeft = (int) (itemView.getRight() - width / 3 * rate);
                        int iconTop = (int) (itemView.getTop() + height /2 - iconH/2);
                        int iconRight = (int) (itemView.getRight() + iconW - width / 3 * rate);
                        int iconBottom = (int) (itemView.getBottom() - height/2 + iconH/2);
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        icon.draw(c);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void initDialog() {
        alertDialog = new AlertDialog.Builder(this);
        view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(add){
                    add = false;
                    titles.add(et_input.getText().toString());
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(titles.size() - 1);
                    dialog.dismiss();
                } else {
                    titles.set(edit_position, et_input.getText().toString());
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
        et_input = (EditText)view.findViewById(R.id.et_input);
    }

    private void removeView(){
        if(view.getParent()!=null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                removeView();
                add = true;
                alertDialog.setTitle("Add Item");
                et_input.setText("");
                alertDialog.show();
                break;
        }
    }
}
