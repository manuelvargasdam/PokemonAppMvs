package com.tarea3.pokemonappmvs.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tarea3.pokemonappmvs.listener.OnPokemonCaptureListener;
import com.tarea3.pokemonappmvs.listener.OnPokemonDeletedListener;
import com.tarea3.pokemonappmvs.entity.Pokemon;
import com.tarea3.pokemonappmvs.service.PokemonApi;
import com.tarea3.pokemonappmvs.entity.PokemonResponse;
import com.tarea3.pokemonappmvs.R;
import com.tarea3.pokemonappmvs.service.RetrofitInstance;
import com.tarea3.pokemonappmvs.adapter.PokedexAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokedexFragment extends Fragment implements OnPokemonDeletedListener {

    private OnPokemonCaptureListener listener;
    private RecyclerView recyclerView;
    private PokedexAdapter adapter;
    private ArrayList<Pokemon> pokemonList = new ArrayList<>();
    private ArrayList<String> capturedPokemonNames = new ArrayList<>();


    @Override
    public void onAttach(@androidx.annotation.NonNull Context context) {
        super.onAttach(context);
        // Verificar si la actividad implementa la interfaz
        if (context instanceof OnPokemonCaptureListener) {
            listener = (OnPokemonCaptureListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnPokemonCaptureListener");
        }
    }

    @Override
    public void onPokemonDeleted(String namePokemon) {
        capturedPokemonNames.remove(namePokemon);
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokedex, container, false);

        recyclerView = view.findViewById(R.id.pokedex_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PokedexAdapter(pokemonList, capturedPokemonNames, pokemon -> {
            // Capturar Pokémon
            capturePokemon(pokemon.getName());
        });
        recyclerView.setAdapter(adapter);

        // Cargar datos de la Pokédex
        loadPokedex();

        // Cargar Pokémon capturados desde Firebase
        loadCapturedPokemon();

        return view;
    }


    private void loadCapturedPokemon() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Cargar los Pokémon capturados desde Firebase
        db.collection("captured_pokemon")
                .whereEqualTo("idUsuario", user.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (com.google.firebase.firestore.QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String pokemonName = document.getString("name");
                        if (pokemonName != null) {
                            capturedPokemonNames.add(pokemonName);
                        }
                    }
                    adapter.notifyDataSetChanged(); // Actualizar la UI después de cargar los Pokémon capturados
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al cargar Pokémon capturados", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadPokedex() {
        RetrofitInstance.getApi().getPokemonList(0, 150).enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(@NonNull Call<PokemonResponse> call, @NonNull Response<PokemonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pokemonList.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PokemonResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error al cargar la Pokédex", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void capturePokemon(String pokemonName) {

        if (capturedPokemonNames.contains(pokemonName)) {
            Toast.makeText(getContext(), pokemonName + " " +  getString(R.string.is_captured), Toast.LENGTH_SHORT).show();
            return;
        }
        // Obtener la instancia de la API
        PokemonApi api = RetrofitInstance.getApi();

        // Llamar al método para obtener detalles del Pokémon
        api.getPokemonDetails(pokemonName.toLowerCase()).enqueue(new retrofit2.Callback<Pokemon>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(retrofit2.Call<Pokemon> call, retrofit2.Response<Pokemon> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Pokemon pokemonDao = response.body();

                    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Mapeo de datos del Pokémon
                    HashMap<String, Object> pokemonData = new HashMap<>();
                    pokemonData.put("id", pokemonDao.getId());
                    pokemonData.put("name", pokemonDao.getName());
                    pokemonData.put("sprites", pokemonDao.getSprites() != null ? pokemonDao.getSprites().getFrontDefault() : null);
                    pokemonData.put("types", pokemonDao.getTypes() != null ?
                            pokemonDao.getTypes().stream().map(type -> type.getType().getName()).collect(Collectors.toList()) : null);
                    pokemonData.put("weight", pokemonDao.getWeight());
                    pokemonData.put("height", pokemonDao.getHeight());
                    pokemonData.put("idUsuario", user.getUid());

                    // Guardar en la colección 'captured_pokemon'
                    db.collection("captured_pokemon")
                            .add(pokemonData)
                            .addOnSuccessListener(documentReference -> {
                                capturedPokemonNames.add(pokemonDao.getName());
                                adapter.notifyDataSetChanged();

                                // Notificar al CapturedPokemonFragment que un Pokémon ha sido capturado
                                if (listener != null) {
                                    listener.onPokemonCaptured(pokemonData);
                                }

                            })
                            .addOnFailureListener(e -> {
                                System.err.println("Error al guardar Pokémon: " + e.getMessage());
                            });
                } else {
                    Toast.makeText(requireContext(), "Error al obtener los detalles de " + pokemonName, Toast.LENGTH_SHORT).show();
                }
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onFailure(retrofit2.Call<Pokemon> call, Throwable t) {
                Toast.makeText(requireContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
