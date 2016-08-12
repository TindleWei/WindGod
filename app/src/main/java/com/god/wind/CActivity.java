package com.god.wind;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

public class CActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> titles = new ArrayList<>();
    private CAdapter adapter;
    private RecyclerView recyclerView;

    private AlertDialog.Builder alertDialog;
    private EditText et_input;
    private View view;
    private int edit_position = -1;
    private boolean add = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);

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
        titles.add("Jon Snow");
        titles.add("Arya Stark");
        titles.add("Daenerys Targaryen");
        titles.add("Tyrion Lannister");
        titles.add("Bran Stark");
        adapter = new CAdapter(titles);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
                    // titles.add(et_input.getText().toString());
                    // adapter.notifyDataSetChanged();
                    adapter.addItem(et_input.getText().toString());
                    // recyclerView.scrollToPosition(titles.size() - 1);
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
