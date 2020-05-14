package com.flav.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ListView mListView;

    public static MediaPlayer mp;
    public static boolean musicPlaying = true;

    public static final String EXTRA_POKEMON = "pokemonExtra";
    public static int soundID;

    private List<Pokemon> pokemons = new ArrayList<Pokemon>();
    private List<Pokemon> allPokemons = new ArrayList<>();
    private String url;
    private GetPokemonTask getLocalPokemonTask = new GetPokemonTask();
    private ArrayAdapter<Pokemon> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listview);
        adapter = new pokemonAdapter(this, pokemons);
        mListView.setAdapter(adapter);
        url = "https://pokeapi.co/api/v2/pokemon?limit=999";
        getLocalPokemonTask.execute(url);

        //sound management
        mp = MediaPlayer.create(this, R.raw.pokemusic);
        mp.start();

        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = findViewById(R.id.pokemonEditText);
                List<Pokemon> newPokemons = new ArrayList<Pokemon>();
                String search = editText.getText().toString();

                dismissKeyboard(editText);
                newPokemons.clear();
                for (Pokemon pokemon : allPokemons) {
                    if (pokemon.name.contains(search)) {
                        newPokemons.add(pokemon);
                    }
                }
                pokemons.clear();
                pokemons.addAll(newPokemons);
                adapter.notifyDataSetChanged(); // rebind to ListView
                mListView.smoothScrollToPosition(0); // scroll to top
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), PokeInfoActivity.class);
                intent.putExtra(EXTRA_POKEMON, ((Pokemon) mListView.getItemAtPosition(position)).getPokeInfo());
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void dismissKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private class GetPokemonTask
            extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            try {
                Request getRequest = new Request.Builder().url(params[0]).build();
                Response response = client.newCall(getRequest).execute();
                final String text = Objects.requireNonNull(response.body()).string();
                final int statusCode = response.code();
                return new JSONObject(text);

            } catch (Exception e) {
                Log.d("pokemon", e.getMessage());
                e.printStackTrace();
            }

            return null;
        }

        // process JSON response and update ListView
        @Override
        protected void onPostExecute(JSONObject pokemons) {
            convertJSONtoArrayList(pokemons); // repopulate weatherList
            adapter.notifyDataSetChanged(); // rebind to ListView
            mListView.smoothScrollToPosition(0); // scroll to top
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.music:
                if (musicPlaying) {
                    mp.pause();
                    item.setIcon(R.drawable.ic_volume_off_white_24dp);
                    musicPlaying = false;
                }
                else {
                    mp.start();
                    item.setIcon(R.drawable.ic_volume_up_white_24dp);
                    musicPlaying = true;
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void convertJSONtoArrayList(JSONObject forecast) {
        try {
            pokemons.clear();
            JSONArray arr = forecast.getJSONArray("results");
            for (int i = 0; i < arr.length(); i++) {
                pokemons.add(new Pokemon(arr.getJSONObject(i).getString("name"), arr.getJSONObject(i).getString("url")));
            }
            allPokemons = new ArrayList<>(pokemons);
        } catch (JSONException e) {
            Log.d("pokemon", e.getMessage());
            e.printStackTrace();
        }
    }
}
