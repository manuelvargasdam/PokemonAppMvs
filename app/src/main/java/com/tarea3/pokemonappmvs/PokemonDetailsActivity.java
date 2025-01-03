package com.tarea3.pokemonappmvs;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class PokemonDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_details);


        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());


        // Obtenemos referencias a los elementos del diseño
        ImageView pokemonImage = findViewById(R.id.pokemon_image_detail);
        TextView pokemonName = findViewById(R.id.pokemon_name_detail);
        TextView pokemonType = findViewById(R.id.pokemon_type_detail);
        TextView pokemonWeight = findViewById(R.id.pokemon_weight_detail);
        TextView pokemonHeight = findViewById(R.id.pokemon_height_detail);
        TextView pokemonIndex = findViewById(R.id.pokemon_index_detail);

        // Recuperamos los datos del Intent
        String name = getIntent().getStringExtra("pokemonName");
        String type = getIntent().getStringExtra("pokemonType");
        String imageUrl = getIntent().getStringExtra("pokemonImage");
        String weight = getIntent().getStringExtra("pokemonWeight");
        String height = getIntent().getStringExtra("pokemonHeight");
        String index = getIntent().getStringExtra("pokemonIndex");

        // Configuramos los datos en la vista
        pokemonName.setText(String.format(getString(R.string.pokemon_name), name));
        pokemonType.setText(String.format(getString(R.string.pokemon_type), type));
        pokemonIndex.setText(String.format(getString(R.string.pokemon_index), index));
        pokemonWeight.setText(String.format(getString(R.string.pokemon_weight), weight));
        pokemonHeight.setText(String.format(getString(R.string.pokemon_height), height));
        Glide.with(this).load(imageUrl).into(pokemonImage);
    }
}
