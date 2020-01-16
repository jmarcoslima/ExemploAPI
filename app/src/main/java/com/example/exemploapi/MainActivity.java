package com.example.exemploapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;


public class MainActivity extends AppCompatActivity {
    ListView listview;
    final ArrayList<String> nomes = new ArrayList<>();
    String ftUltima;
    ImageView ultimoDog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        listview = findViewById(R.id.lista);
        ultimoDog = findViewById(R.id.ftDog);
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomes);
        listview.setAdapter(adapter);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://dog.ceo/api/breeds/list";

        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String a;
                            JSONArray array = response.getJSONArray("message");
                            for (int i = 0; i < array.length(); i++) {
                                a = capitalize(array.get(i).toString());
                                nomes.add(a);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetalhesActivity.class);
                String nomeDog = listview.getItemAtPosition(position).toString();
                intent.putExtra("nomeDog", nomeDog);
                startActivity(intent);


            }
        });

    }


    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }


        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Override
    protected void onResume() {
        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        ftUltima = preferences.getString("urlImagem", "");

        if (ftUltima != null) {
            Picasso.get().load(ftUltima).resize(200, 200).placeholder(R.drawable.gifload).into(ultimoDog);
        }
        super.onResume();
    }
}