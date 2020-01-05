package com.example.geo_interestpoi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class POI_Set extends AppCompatActivity {


    RecyclerView recyclerView;
    FloatingActionButton fab_add, fab_confirm;

    List<Integer> imageList= new ArrayList<>();
    List<String> titleList= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi__set);

        initViews();
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        fab_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(POI_Set.this,Home.class));
                finish();
            }
        });

        GridLayoutManager gridLayoutManager= new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        imageList.add(R.drawable.cinema);
        imageList.add(R.drawable.gym);
        imageList.add(R.drawable.hotel);
        imageList.add(R.drawable.mall);
        imageList.add(R.drawable.restround);
        imageList.add(R.drawable.music_venues );
        imageList.add(R.drawable.music_venues );



        titleList.add("cinema");
        titleList.add("gym");
        titleList.add("hotel");
        titleList.add("mall");
        titleList.add("restround");
        titleList.add("music_venues");
        titleList.add("music_venues");



        recyclerView.setAdapter(new POIAdapter(imageList, titleList));

    }

    private void initViews() {

        recyclerView = findViewById(R.id.recyclerView);
        fab_add = findViewById(R.id.fab_add);
        fab_confirm = findViewById(R.id.fab_confirm);

    }
}
