package com.example.exemploapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class FavoritosActivity extends AppCompatActivity {
    RecyclerView rvF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        rvF = findViewById(R.id.rvF);

        rvF = findViewById(R.id.rvF);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rvF.setLayoutManager(lm);

    }
}
