package com.example.exemploapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.exemploapi.MainActivity.capitalize;

public class DetalhesActivity extends AppCompatActivity {
    TextView tv;
    ListView listaSub;
    String nomeDog;
    final ArrayList<String> subNomes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        tv = findViewById(R.id.nomeDog);
        listaSub = findViewById(R.id.listaSub);

        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subNomes);
        listaSub.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nomeDog = extras.getString("nomeDog");
        }

        tv.setText(nomeDog);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://dog.ceo/api/breeds/list/all";

        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String a;
                            JSONArray array = response.getJSONArray(nomeDog);
                            for (int i = 0; i < array.length(); i++) {
                                a = capitalize(array.get(i).toString());
                                subNomes.add(a);
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


    }
}
