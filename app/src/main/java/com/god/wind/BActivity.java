package com.god.wind;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

public class BActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> titles = new ArrayList<>();
    private BAdapter adapter;
    private RecyclerView recyclerView;

    private AlertDialog.Builder alertDialog;
    private EditText et_input;
    private View view;
    private int edit_position = -1;
    private boolean flag_swiped = false;
    private boolean add = false;

    private Paint p = new Paint();

    public static final int SWIPE_STATE_MID = 0;
    public static final int SWIPE_STATE_LEFT = 1;
    public static final int SWIPE_STATE_RIGHT = 2;
    public int swipe_state = 0;

    private Rect clickRegionRect;
    private Rect leftImageRegionRect, rightImageRegionRect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

        initViews();
        initDialog();

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    // Log.e("onTouch", "X: " + motionEvent.getRawX() + " Y: " + motionEvent.getRawY());
                    Point point = new Point((int)motionEvent.getRawX(), (int)motionEvent.getRawY());

                    if (swipe_state == SWIPE_STATE_LEFT) {
                        if(clickRegionRect.contains(point.x, point.y)){
                            if(leftImageRegionRect.contains(point.x, point.y)){
                                Log.d("onTouch", " left Image ! pos: " + edit_position);
                                removeView();
                                alertDialog.setTitle("Edit Content");
                                et_input.setText(titles.get(edit_position));
                                alertDialog.show();
                            }
                        }
                    } else if (swipe_state == SWIPE_STATE_RIGHT) {
                        if(clickRegionRect.contains(point.x, point.y)){
                            if(rightImageRegionRect.contains(point.x, point.y)){
                                Log.d("onTouch", " right Image ! pos: " + edit_position);
                                recyclerView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        titles.remove(edit_position);
                                        adapter.notifyDataSetChanged();
                                    }
                                }, 300);
                            }
                        }
                    }
                }
                return false;
            }
        });
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
        adapter = new BAdapter(titles);
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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ClipItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public class ClipItemTouchHelper extends ItemTouchHelper.SimpleCallback {

        public ClipItemTouchHelper(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int position = viewHolder.getAdapterPosition();
            if (position != edit_position) {
                if (edit_position != -1 && flag_swiped == true) {
                    adapter.notifyItemChanged(edit_position);
                    flag_swiped = false;
                    edit_position = position;
                }
            }
            return super.getSwipeDirs(recyclerView, viewHolder);
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            flag_swiped = true;
            edit_position = viewHolder.getAdapterPosition();

            BAdapter.ViewHolder mHolder = (BAdapter.ViewHolder)viewHolder;
            View itemView = mHolder.itemView;
            ImageView addView = mHolder.iv_add;
            ImageView delView = mHolder.iv_delete;

            clickRegionRect = new Rect(itemView.getLeft(),
                    itemView.getTop() + (int)recyclerView.getY(),
                    itemView.getRight(),
                    itemView.getBottom() + (int)recyclerView.getY());

            leftImageRegionRect = new Rect(addView.getLeft() + clickRegionRect.left,
                    addView.getTop() + clickRegionRect.top,
                    addView.getRight() + clickRegionRect.left,
                    addView.getBottom() + clickRegionRect.top);

            rightImageRegionRect = new Rect(delView.getLeft() + clickRegionRect.left,
                    delView.getTop() + clickRegionRect.top,
                    delView.getRight() + clickRegionRect.left,
                    delView.getBottom() + clickRegionRect.top);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (viewHolder != null) {
                final View foregroundView = ((BAdapter.ViewHolder) viewHolder).clipForeground;
                getDefaultUIUtil().onSelected(foregroundView);
            }
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            final View foregroundView = ((BAdapter.ViewHolder) viewHolder).clipForeground;

            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);

            drawBackground(viewHolder, dX, actionState);

            if (dX >= getResources().getDisplayMetrics().widthPixels) {
                if (swipe_state != SWIPE_STATE_LEFT) {
                    swipe_state = SWIPE_STATE_LEFT;
                }
                Log.d("onChildDraw", " dx > screenWidth" + "  flag_swiped: " + flag_swiped);
            } else if (dX <= -getResources().getDisplayMetrics().widthPixels) {
                if (swipe_state != SWIPE_STATE_RIGHT) {
                    swipe_state = SWIPE_STATE_RIGHT;
                }
                Log.d("onChildDraw", " dx < screenWidth" + "  flag_swiped: " + flag_swiped);
            } else if (dX == 0) {
                if (swipe_state != SWIPE_STATE_MID) {
                    swipe_state = SWIPE_STATE_MID;
                }
            }
        }

        @Override
        public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            final View foregroundView = ((BAdapter.ViewHolder) viewHolder).clipForeground;

            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);

            drawBackground(viewHolder, dX, actionState);

        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            final View backgroundView = ((BAdapter.ViewHolder) viewHolder).leftBackground;
            final View foregroundView = ((BAdapter.ViewHolder) viewHolder).clipForeground;

            backgroundView.setRight(0);

            getDefaultUIUtil().clearView(foregroundView);
        }


        private void drawBackground(RecyclerView.ViewHolder viewHolder, float dX, int actionState) {
            final View backgroundView = ((BAdapter.ViewHolder) viewHolder).leftBackground;

            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                backgroundView.setRight((int) Math.max(dX, 0));
            }
        }
    }

    private void initDialog() {
        alertDialog = new AlertDialog.Builder(this);
        view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (add) {
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
        et_input = (EditText) view.findViewById(R.id.et_input);
    }

    private void removeView() {
        if (view.getParent() != null) {
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
