package com.example.cw;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    List<Hiking> dataList;
    HikingAdapter adapter;
    SearchView searchView;
    DatabaseHelper dbHelper;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        dataList = new ArrayList<>();

        // Initialize your dbHelper here
        dbHelper = new DatabaseHelper(this);

        dataList = dbHelper.getAllHikings();

        adapter = new HikingAdapter(MainActivity.this, dataList, dbHelper, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged(); // Cập nhật RecyclerView

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateHikingActivity.class);
                startActivity(intent);
            }
        });

    }

    @SuppressLint("Range")
    public void refreshData() {
        dataList.clear(); // Xóa dữ liệu hiện tại
        dataList = dbHelper.getAllHikings();
        adapter.notifyDataSetChanged(); // Cập nhật RecyclerView
    }

    public void searchList(String text){
        ArrayList<Hiking> searchList = new ArrayList<>();
        for (Hiking dataClass: dataList){
            if (dataClass.getName().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }

}