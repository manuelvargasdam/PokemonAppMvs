package com.tarea3.pokemonappmvs.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tarea3.pokemonappmvs.fragment.CapturedPokemonFragment;
import com.tarea3.pokemonappmvs.fragment.PokedexFragment;
import com.tarea3.pokemonappmvs.fragment.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragmentList;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragmentList = new ArrayList<>();
        fragmentList.add(new CapturedPokemonFragment()); // Fragmento de Capturados
        fragmentList.add(new PokedexFragment()); // Fragmento de Pokédex
        fragmentList.add(new SettingsFragment()); // Fragmento de Ajustes
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    public Fragment getFragment(int position) {
        return fragmentList.get(position);
    }
}

