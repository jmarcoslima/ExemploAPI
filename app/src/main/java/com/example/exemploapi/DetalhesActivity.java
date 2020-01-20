package com.example.exemploapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

import static com.example.exemploapi.MainActivity.capitalize;

public class DetalhesActivity extends AppCompatActivity {
    private TextView tv, tvSub, tvtSubRacas;
    private ImageView fotoDog;
    private String nomeDog, nomeSubDog, urlInicio = "https://dog.ceo/api/breed/", url, urlImagem;
    private RecyclerView rvD;
    private List<Raca> subRacas = new ArrayList<>();
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        getSupportActionBar().hide();
        tv = findViewById(R.id.nomeDog);
        tvSub = findViewById(R.id.nomeSub);
        fotoDog = findViewById(R.id.imageView);
        tvtSubRacas = findViewById(R.id.subRacas);
        rvD = findViewById(R.id.rvD);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rvD.setLayoutManager(lm);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            nomeDog = extras.getString("nomeDog", "");
            nomeSubDog = extras.getString("nomeSub", "");
            url = urlInicio + nomeDog.toLowerCase() + "/list";
            if (nomeSubDog.equals("")) {
                urlImagem = urlInicio + nomeDog.toLowerCase() + "/images/random";
            } else {
                urlImagem = urlInicio + nomeDog.toLowerCase() + "/" + nomeSubDog.toLowerCase() + "/images/random";
            }
            queue = Volley.newRequestQueue(this);
            jsonRequest();
            jsonRequestImage();

        }
    }

    private void imageShared(String urlI) {
        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("urlImagem", urlI);
        editor.commit();
    }

    public void jsonRequest() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("message");
                            for (int i = 0; i < array.length(); i++) {
                                Raca r = new Raca();
                                r.setNome(capitalize(array.get(i).toString()));
                                subRacas.add(r);

                            }
                            ListaAdapter adapter = new ListaAdapter(subRacas);
                            adapter.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    Intent i = new Intent(DetalhesActivity.this, DetalhesActivity.class);
                                    String nomeSub = subRacas.get(position).toString();
                                    i.putExtra("nomeDog", nomeDog);
                                    i.putExtra("nomeSub", nomeSub);
                                    startActivity(i);
                                }
                            });
                            rvD.setAdapter(adapter);
                            registerForContextMenu(rvD);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        if (nomeSubDog.equals("")) {
            queue.add(request);
        }
    }

    public void jsonRequestImage() {
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

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(requestImagem);

    }


}

