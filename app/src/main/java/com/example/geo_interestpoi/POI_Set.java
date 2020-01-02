package com.example.geo_interestpoi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class POI_Set extends AppCompatActivity {


    RecyclerView recyclerView;

    List<Integer> imageList= new ArrayList<>();
    List<String> titleList= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi__set);

        initViews();

        GridLayoutManager gridLayoutManager= new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        imageList.add(R.mipmap.ic_launcher_round);
        imageList.add(R.mipmap.register);
        imageList.add(R.mipmap.round_arrow_back_black_24);
        imageList.add(R.mipmap.ic_launcher_round);

        titleList.add("ic_launcher_round");
        titleList.add("register");
        titleList.add("round_arrow_back_black_24");
        titleList.add("ic_launcher_round");

        recyclerView.setAdapter(new POIAdapter(imageList, titleList));

    }

    private void initViews() {

        recyclerView = findViewById(R.id.recyclerView);

    }
}
