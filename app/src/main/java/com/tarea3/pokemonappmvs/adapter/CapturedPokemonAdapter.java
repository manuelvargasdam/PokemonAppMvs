package com.tarea3.pokemonappmvs.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tarea3.pokemonappmvs.PokemonDetailsActivity;
import com.tarea3.pokemonappmvs.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CapturedPokemonAdapter extends RecyclerView.Adapter<CapturedPokemonAdapter.ViewHolder> implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final ArrayList<HashMap<String, Object>> capturedPokemonList;
    private final OnDeleteClickListener onDeleteClickListener;
    private SharedPreferences sharedPreferences;

    public interface OnDeleteClickListener {
        void onDeleteClick(String documentId);
    }

    public CapturedPokemonAdapter(ArrayList<HashMap<String, Object>> capturedPokemonList, OnDeleteClickListener listener, Context context) {
        this.capturedPokemonList = capturedPokemonList;
        this.onDeleteClickListener = listener;

        // Iniciar SharedPreferences
        this.sharedPreferences = context.getSharedPreferences("PokemonAppPreferences", Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("allow_deletion")) {
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.captured_pokemon_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, Object> pokemon = capturedPokemonList.get(position);

        holder.pokemonName.setText((String) pokemon.get("name"));
        holder.pokemonType.setText(pokemon.get("types") != null ? pokemon.get("types").toString() : "Unknown");

        Glide.with(holder.itemView.getContext())
                .load((String) pokemon.get("sprites"))
                .into(holder.pokemonImage);

        boolean allowDeletion = sharedPreferences.getBoolean("allow_deletion", false);

        // Si allow_deletion es true, mostrar el ícono de eliminar, si es false, ocultarlo
        holder.deleteIcon.setVisibility(allowDeletion ? View.VISIBLE : View.GONE);

        holder.deleteIcon.setOnClickListener(v -> {
            String documentId = (String) pokemon.get("documentId");
            onDeleteClickListener.onDeleteClick(documentId);
        });

        holder.itemView.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(holder.itemView.getContext(), PokemonDetailsActivity.class);

                // Configurar los datos para la actividad de detalles
                intent.putExtra("pokemonName", (String) pokemon.get("name"));
                intent.putExtra("pokemonType", pokemon.get("types") != null ? pokemon.get("types").toString() : "Unknown");
                intent.putExtra("pokemonImage", (String) pokemon.get("sprites"));
                intent.putExtra("pokemonWeight", pokemon.get("weight") != null ? pokemon.get("weight").toString() : "0");
                intent.putExtra("pokemonHeight", pokemon.get("height") != null ? pokemon.get("height").toString() : "0");
                intent.putExtra("pokemonIndex", pokemon.get("id") != null ? pokemon.get("id").toString() : "0");

                holder.itemView.getContext().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();  // Para registro de errores
                Toast.makeText(holder.itemView.getContext(), "Error al abrir detalles del Pokémon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return capturedPokemonList.size();
    }

    // Método para desregistrar el listener cuando ya no se necesite
    public void unregisterSharedPreferencesListener() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pokemonImage;
        TextView pokemonName;
        TextView pokemonType;
        ImageView deleteIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pokemonImage = itemView.findViewById(R.id.pokemon_image);
            pokemonName = itemView.findViewById(R.id.pokemon_name);
            pokemonType = itemView.findViewById(R.id.pokemon_type);
            deleteIcon = itemView.findViewById(R.id.delete_icon);
        }
    }
}
