package com.example.exemploapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.exemploapi.MainActivity.capitalize;

public class DetalhesActivity extends AppCompatActivity {
    TextView tv, tvSub, tvtSubRacas;
    ImageView fotoDog;
    ListView listaSub;
    String nomeDog, urlInicio = "https://dog.ceo/api/breed/", nomeSub;
    String urlImagem;


    final ArrayList<String> subNomes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_detalhes);
        getSupportActionBar().hide();
        tv = findViewById(R.id.nomeDog);
        tvSub = findViewById(R.id.nomeSub);
        fotoDog = findViewById(R.id.imageView);
        listaSub = findViewById(R.id.listaSub);
        tvtSubRacas = findViewById(R.id.subRacas);


        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subNomes);
        listaSub.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nomeDog = extras.getString("nomeDog");
            nomeSub = extras.getString("nomeSub", "");
        }
        if (nomeSub.equals("")) {
            tv.setText("Raça: " + nomeDog);

        } else {
            tv.setText("Raça: " + nomeDog);
            tvSub.setText("Sub-raça: " + nomeSub);
            tvtSubRacas.setText("");
        }


        RequestQueue queue = Volley.newRequestQueue(this);
        String url = urlInicio + nomeDog.toLowerCase() + "/list";
        if (nomeSub.equals("")) {
            urlImagem = urlInicio + nomeDog.toLowerCase() + "/images/random";
        } else {
            urlImagem = urlInicio + nomeDog.toLowerCase() + "/" + nomeSub.toLowerCase() + "/images/random";
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String a;
                            JSONArray array = response.getJSONArray("message");
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


        JsonObjectRequest requestImagem = new JsonObjectRequest(Request.Method.GET, urlImagem, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String imagemUrl = response.getString("message");
                            Picasso.get().load(imagemUrl).resize(200, 200).placeholder(R.drawable.gifload).into(fotoDog);
                            imageShared(imagemUrl);

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


        queue.add(requestImagem);
        if (nomeSub.equals("")) {
            queue.add(request);
        }


        listaSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DetalhesActivity.this, DetalhesActivity.class);
                String nomeSub = listaSub.getItemAtPosition(position).toString();
                intent.putExtra("nomeSub", nomeSub);
                intent.putExtra("nomeDog", nomeDog);
                startActivity(intent);
            }
        });


    }

    private void imageShared(String urlI) {
        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("urlImagem", urlI);
        editor.commit();
    }


}

