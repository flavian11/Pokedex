package com.flav.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PokeInfoActivity extends AppCompatActivity {

    public ImageView pokeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_info);
        PokeInfo pokemon = (PokeInfo) getIntent().getSerializableExtra(MainActivity.EXTRA_POKEMON);

        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView pokemonIdTextView = findViewById(R.id.pokemonIdText);
        TextView hpTextView = findViewById(R.id.hpTextView);
        TextView speedTextView = findViewById(R.id.speedTextView);
        TextView weightTextView = findViewById(R.id.weightTextView);
        TextView atkTextView = findViewById(R.id.atkTextView);
        TextView defTextView = findViewById(R.id.defTextView);
        TextView atkSpeTextView = findViewById(R.id.atkSpeTextView);
        TextView defSpeTextView = findViewById(R.id.defSpeTextView);
        pokeImage = findViewById(R.id.pokeImage);

        nameTextView.setText(pokemon.name);
        pokemonIdTextView.setText(" n° " + pokemon.id);
        speedTextView.setText("speed: " + pokemon.speed);
        weightTextView.setText("weight: " + pokemon.weight);
        hpTextView.setText("hp: " + pokemon.hp);
        atkTextView.setText("atk: " + pokemon.atk);
        defTextView.setText("def: " + pokemon.def);
        atkSpeTextView.setText("atkSpe: " + pokemon.atkSpe);
        defSpeTextView.setText("defSpe: " + pokemon.defSpe);

        GetImagePokemonTask getImagePokemonTask = new GetImagePokemonTask(pokeImage);
        getImagePokemonTask.execute(pokemon.spriteURL);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.music:
                if (MainActivity.musicPlaying) {
                    MainActivity.mp.pause();
                    item.setIcon(R.drawable.ic_volume_off_white_24dp);
                    MainActivity.musicPlaying = false;
                }
                else {
                    MainActivity.mp.start();
                    item.setIcon(R.drawable.ic_volume_up_white_24dp);
                    MainActivity.musicPlaying = true;
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetImagePokemonTask
            extends AsyncTask<String, Void, Bitmap> {

        private ImageView imageView;

        public GetImagePokemonTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            try {
                Request getRequest = new Request.Builder().url(params[0]).build();
                Response response = client.newCall(getRequest).execute();

                InputStream inputStream = response.body().byteStream();
                return BitmapFactory.decodeStream(inputStream);


            } catch (Exception e) {
                Log.d("pokemon", e.getMessage());
                e.printStackTrace();
            }

            return null;
        }

        // process JSON response and update ListView
        @Override
        protected void onPostExecute(Bitmap pokemonImg) {
            this.imageView.setImageBitmap(pokemonImg);
        }
    }
}
