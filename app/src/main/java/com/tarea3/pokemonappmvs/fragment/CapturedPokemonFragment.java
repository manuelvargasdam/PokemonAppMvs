package com.tarea3.pokemonappmvs.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tarea3.pokemonappmvs.listener.OnPokemonCaptureListener;
import com.tarea3.pokemonappmvs.listener.OnPokemonDeletedListener;
import com.tarea3.pokemonappmvs.R;
import com.tarea3.pokemonappmvs.adapter.CapturedPokemonAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class CapturedPokemonFragment extends Fragment implements OnPokemonCaptureListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private OnPokemonDeletedListener listener;
    private RecyclerView recyclerView;
    private CapturedPokemonAdapter adapter;
    private ArrayList<HashMap<String, Object>> capturedPokemonList = new ArrayList<>();

    @Override
    public void onAttach(@androidx.annotation.NonNull Context context) {
        super.onAttach(context);
        // Verificar si la actividad implementa la interfaz
        if (context instanceof OnPokemonDeletedListener) {
            listener = (OnPokemonDeletedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnPokemonCaptureListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_captured_pokemon, container, false);

        recyclerView = view.findViewById(R.id.captured_pokemon_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CapturedPokemonAdapter(capturedPokemonList, this::deletePokemon, getContext());
        recyclerView.setAdapter(adapter);

        // Añadir línea de separación entre elementos
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        loadCapturedPokemon();

        // Registrar el listener para cambios en SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        return view;
    }

    @Override
    public void onPokemonCaptured(HashMap<String, Object> pokemon) {
        // Agregar el Pokémon capturado a la lista y notificar el cambio
        loadCapturedPokemon();
        adapter.notifyDataSetChanged();
    }

    private void loadCapturedPokemon() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        db.collection("captured_pokemon")
                .whereEqualTo("idUsuario", auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    capturedPokemonList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        HashMap<String, Object> pokemon = (HashMap<String, Object>) document.getData();
                        pokemon.put("documentId", document.getId());
                        capturedPokemonList.add(pokemon);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al cargar Pokémon capturados", Toast.LENGTH_SHORT).show();
                });
    }

    private void deletePokemon(String documentId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Buscar el Pokémon por el documentId en capturedPokemonList
        HashMap<String, Object> pokemonToDelete = null;
        for (HashMap<String, Object> pokemon : capturedPokemonList) {
            if (pokemon.get("documentId").equals(documentId)) {
                pokemonToDelete = pokemon;
                break;
            }
        }

        if (pokemonToDelete != null) {
            String name = (String) pokemonToDelete.get("name"); // Obtener el nombre del Pokémon

            db.collection("captured_pokemon")
                    .document(documentId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), name + " " +getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                        loadCapturedPokemon(); // Recargar la lista de Pokémon
                        if (listener != null) {
                            listener.onPokemonDeleted(name); // Notificar que se eliminó el Pokémon
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Error al eliminar el Pokémon", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("allow_deletion")) {
            // Cambiar el estado de la eliminación según la preferencia
            loadCapturedPokemon();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (listener != null) {
            listener = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Desregistrar SharedPreferences listener
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);

        // Desregistrar SharedPreferences listener en el adaptador
        adapter.unregisterSharedPreferencesListener();
    }
}
