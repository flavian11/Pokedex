package com.flav.pokedex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class pokemonAdapter extends ArrayAdapter<Pokemon> {

    private static class ViewHolder {
        TextView nameTextView;
        ImageView pokemonSprite;
    }

    public pokemonAdapter(Context context, List<Pokemon> forecast) {
        super(context, R.layout.list_item, forecast);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get Weather object for this specified ListView position
        Pokemon pokemon = getItem(position);

        ViewHolder viewHolder; // object that reference's list item's views

        // check for reusable ViewHolder from a ListView item that scrolled
        // offscreen; otherwise, create a new ViewHolder
        if (convertView == null) { // no reusable ViewHolder, so create one
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView =
                    inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.nameTextView =
                    (TextView) convertView.findViewById(R.id.nameTextView);
            viewHolder.pokemonSprite =
                    (ImageView) convertView.findViewById(R.id.pokeImage);
            convertView.setTag(viewHolder);
        }
        else { // reuse existing ViewHolder stored as the list item's tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        pokemon.fillPokemonInfo(viewHolder.pokemonSprite);

        viewHolder.pokemonSprite.setImageBitmap(pokemon.pokemonBmp);
        viewHolder.nameTextView.setText(pokemon.name);

        return convertView; // return completed list item to display
    }


}
