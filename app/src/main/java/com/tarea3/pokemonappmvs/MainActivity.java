package com.tarea3.pokemonappmvs;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tarea3.pokemonappmvs.adapter.ViewPagerAdapter;
import com.tarea3.pokemonappmvs.fragment.CapturedPokemonFragment;
import com.tarea3.pokemonappmvs.fragment.PokedexFragment;
import com.tarea3.pokemonappmvs.listener.OnPokemonCaptureListener;
import com.tarea3.pokemonappmvs.listener.OnPokemonDeletedListener;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements OnPokemonCaptureListener, OnPokemonDeletedListener {

    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    public void onPokemonCaptured(HashMap<String, Object> pokemon) {
        // Buscar el fragmento "Capturados" mediante FragmentManager
        CapturedPokemonFragment capturedFragment = (CapturedPokemonFragment) getSupportFragmentManager()
                .findFragmentByTag("f" + 0); // 0 es el índice del fragmento "Capturados" en ViewPager2

        if (capturedFragment != null) {
            // Llamar al método en el fragmento para actualizar la lista de Pokémon capturados
            capturedFragment.onPokemonCaptured(pokemon);
        }
    }

    @Override
    public void onPokemonDeleted(String name) {
        // Buscar el fragmento "Capturados" mediante FragmentManager
        PokedexFragment pokedexFragment = (PokedexFragment) getSupportFragmentManager()
                .findFragmentByTag("f" + 1);

        if (pokedexFragment != null) {
            pokedexFragment.onPokemonDeleted(name);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        // Configurar el adaptador para ViewPager
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        // Vincular TabLayout con ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(getString(R.string.tab_captured));
                            break;
                        case 1:
                            tab.setText(getString(R.string.tab_pokedex));
                            break;
                        case 2:
                            tab.setText(getString(R.string.tab_settings));
                            break;
                    }
                }).attach();
    }


}
