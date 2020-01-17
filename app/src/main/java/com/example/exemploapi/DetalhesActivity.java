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
    private String nomeDog, urlInicio = "https://dog.ceo/api/breed/", nomeSub;
    private String urlImagem;
    private RecyclerView rvD;
    private ArrayList<Raca> subRacas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        getSupportActionBar().hide();
        jsonRequest();
        tv = findViewById(R.id.nomeDog);
        tvSub = findViewById(R.id.nomeSub);
        fotoDog = findViewById(R.id.imageView);
        tvtSubRacas = findViewById(R.id.subRacas);
        rvD = findViewById(R.id.rvD);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rvD.setLayoutManager(lm);


        Bundle extras = getIntent().getExtras();
        Log.e("aaaaaaaaaaaaaaa", extras.toString());
        if (extras != null) {
            nomeDog = extras.getString("nomeDog");
            for(int i = 0; i < 20; i++) {
                Log.e("aaaa", nomeDog);
            }
            //nomeSub = extras.getString("nomeSub", "");
        }
        /*
        if (nomeSub.equals("")) {
            tv.setText("Raça: " + nomeDog);

        } else {
            tv.setText("Raça: " + nomeDog);
            tvSub.setText("Sub-raça: " + nomeSub);
            tvtSubRacas.setText("");
        }


        if (nomeSub.equals("")) {
            urlImagem = urlInicio + nomeDog.toLowerCase() + "/images/random";
        } else {
            urlImagem = urlInicio + nomeDog.toLowerCase() + "/" + nomeSub.toLowerCase() + "/images/random";
        }
*/

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

                        //adapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }


       /* listaSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DetalhesActivity.this, DetalhesActivity.class);
                String nomeSub = listaSub.getItemAtPosition(position).toString();
                intent.putExtra("nomeSub", nomeSub);
                intent.putExtra("nomeDog", nomeDog);
                startActivity(intent);
            }
        });
*/


    private void imageShared(String urlI) {
        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("urlImagem", urlI);
        editor.commit();
    }

    public void jsonRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = urlInicio + nomeDog.toLowerCase() + "/list";

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
                            rvD.setAdapter(new ListaAdapter(subRacas));
                            registerForContextMenu(rvD);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //adapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(request);
    }


}

