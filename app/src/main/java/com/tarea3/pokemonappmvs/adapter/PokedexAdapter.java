package com.tarea3.pokemonappmvs.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import android.graphics.Color;

import com.tarea3.pokemonappmvs.entity.Pokemon;
import com.tarea3.pokemonappmvs.R;


public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.ViewHolder> {

    private final ArrayList<Pokemon> pokemonList;
    private final ArrayList<String> capturedPokemonNames;
    private final OnPokemonClickListener listener;

    public interface OnPokemonClickListener {
        void onPokemonClick(Pokemon pokemon);
    }

    public PokedexAdapter(ArrayList<Pokemon> pokemonList, ArrayList<String> capturedPokemonNames, OnPokemonClickListener listener) {
        this.pokemonList = pokemonList;
        this.capturedPokemonNames = capturedPokemonNames;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokedex_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);
        holder.pokemonName.setText(pokemon.getName());

        // Verificar si el Pokémon está en la lista de capturados y cambiar el color
        if (capturedPokemonNames.contains(pokemon.getName())) {
            holder.pokemonName.setTextColor(Color.RED); // Cambiar el color a rojo si está capturado
        } else {
            holder.pokemonName.setTextColor(Color.BLACK); // Color predeterminado si no está capturado
        }

        holder.itemView.setOnClickListener(v -> listener.onPokemonClick(pokemon));
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView pokemonName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pokemonName = itemView.findViewById(R.id.pokemon_name);
        }
    }
}
