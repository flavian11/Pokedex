package com.flav.pokedex;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Pokemon {
    public String pokemonUrl;
    public String name;
    public String id;
    public String spriteURL;
    public String speed;
    public String defSpe;
    public String atkSpe;
    public String def;
    public String atk;
    public String hp;
    public String[] types;
    public String weight;

    public ImageView imageView;
    public Bitmap pokemonBmp;
    public GetSinglePokemonTask getSinglePokemonTask = new GetSinglePokemonTask();
    public boolean infoFilled = false;

    public Pokemon(String name, String url) {
        this.name = name;
        this.pokemonUrl = url;
    }

    public PokeInfo getPokeInfo() {
        return new PokeInfo(name, id, spriteURL, speed, defSpe, atkSpe, def, atk, hp, types, weight);
    }

    public void fillPokemonInfo(ImageView imageView) {
        this.imageView = imageView;
        if (!this.infoFilled) {
            getSinglePokemonTask.execute(this.pokemonUrl);
            this.infoFilled = true;
        }
    }

    public void dlAndPutImage() {
        GetImagePokemonTask dlImage = new GetImagePokemonTask(this.imageView);
            dlImage.execute(this.spriteURL);
    }

    private class GetImagePokemonTask
            extends AsyncTask<String, Void, Bitmap> {

        public ImageView imageView;

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
            imageView.setImageBitmap(pokemonImg);
            setPokemonBmp(pokemonImg);
        }
    }

    public void setPokemonBmp(Bitmap bmp) {
        this.pokemonBmp = bmp;
    }

    private class GetSinglePokemonTask
            extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            try {
                Request getRequest = new Request.Builder().url(params[0]).build();
                Response response = client.newCall(getRequest).execute();
                final String text = response.body().string();
                final int statusCode = response.code();
                return new JSONObject(text);

            }
            catch (Exception e) {
                Log.d("pokemon", e.getMessage());
                e.printStackTrace();
            }

            return null;
        }

        // process JSON response and update ListView
        @Override
        protected void onPostExecute(JSONObject pokemon) {
            constructPokemon(pokemon);
            dlAndPutImage();
        }
    }

    private void constructPokemon(JSONObject pokemon) {
        try {
            JSONArray stats = pokemon.getJSONArray("stats");

            for(int i = 0; i < stats.length(); i++) {
                String statName = stats.getJSONObject(i).getJSONObject("stat").getString("name");
                switch (statName) {
                    case "speed":
                        this.speed = Integer.toString(stats.getJSONObject(i).getInt("base_stat"));
                        break;
                    case "special-defense":
                        this.defSpe = Integer.toString(stats.getJSONObject(i).getInt("base_stat"));
                        break;
                    case "special-attack":
                        this.atkSpe = Integer.toString(stats.getJSONObject(i).getInt("base_stat"));
                        break;
                    case "defense":
                        this.def = Integer.toString(stats.getJSONObject(i).getInt("base_stat"));
                        break;
                    case "attack":
                        this.atk = Integer.toString(stats.getJSONObject(i).getInt("base_stat"));
                        break;
                    case "hp":
                        this.hp = Integer.toString(stats.getJSONObject(i).getInt("base_stat"));
                        break;
                }
            }

            this.id = Integer.toString(pokemon.getInt("id"));
            this.spriteURL = pokemon.getJSONObject("sprites").getString("front_default");
            this.weight = Integer.toString(pokemon.getInt("weight"));
        }
        catch (Exception e) {
            Log.d("pokemon", e.getMessage());
            e.printStackTrace();
        }
    }

}
