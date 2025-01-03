package com.tarea3.pokemonappmvs.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tarea3.pokemonappmvs.LogoutConfirmationActivity;
import com.tarea3.pokemonappmvs.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.tarea3.pokemonappmvs.adapter.SettingsAdapter;

public class SettingsFragment extends Fragment {

    private RecyclerView settingsRecyclerView;
    private List<String> options;
    private SettingsAdapter adapter;

    private boolean isDeletionEnabled;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        settingsRecyclerView = view.findViewById(R.id.settings_recycler_view);
        settingsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        options = new ArrayList<>();
        options.add(getString(R.string.language));
        options.add(getString(R.string.delete_my_pokemon));
        options.add(getString(R.string.about));
        options.add(getString(R.string.logout));

        adapter = new SettingsAdapter(options);
        settingsRecyclerView.setAdapter(adapter);

        // Leer el estado del Switch desde SharedPreferences
        isDeletionEnabled = requireActivity()
                .getSharedPreferences("PokemonAppPreferences", Context.MODE_PRIVATE)
                .getBoolean("allow_deletion", false);

        adapter.setOnItemClickListener((position) -> {
            if (position == 0) {
                showLanguageDialog();
            } else if (position == 1) {
                showSwitchDeleteDialog();
            } else if (position == 2) {
                showAboutDialog();
            } else if (position == 3) {
                showLogoutConfirmation();
            }
        });

        return view;
    }

    private void showLogoutConfirmation() {
        Intent intent = new Intent(getActivity(), LogoutConfirmationActivity.class);
        startActivity(intent);
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(getString(R.string.about_title));
        builder.setMessage(getString(R.string.about_message));
        builder.setPositiveButton(getString(R.string.close), (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showLanguageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(getString(R.string.language_title));

        // Agregar un Switch al diálogo para cambiar el idioma
        final Switch languageSwitch = new Switch(requireContext());
        languageSwitch.setChecked(isSpanish()); // Verifica el idioma actual

        languageSwitch.setText(getString(R.string.language_switch_text));

        languageSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Guardar el estado del Switch en SharedPreferences
            changeLanguage(isChecked);
        });

        builder.setView(languageSwitch);
        builder.setPositiveButton(getString(R.string.close), (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void changeLanguage(boolean isSpanish) {
        SharedPreferences preferences = requireActivity().getSharedPreferences("PokemonAppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isSpanish", isSpanish);
        editor.apply();

        // Cambiar el idioma
        setLanguage(isSpanish);

        // Mostrar un Toast indicando el idioma seleccionado
        Toast.makeText(requireContext(), isSpanish ? getString(R.string.spanish_enabled) : getString(R.string.english_enabled), Toast.LENGTH_SHORT).show();

        // Reiniciar la actividad para aplicar el cambio de idioma
        requireActivity().recreate();
    }

    private boolean isSpanish() {
        // Obtener el idioma actual del dispositivo
        String language = Locale.getDefault().getLanguage();
        // Verificar si el idioma es español ("es")
        return language.equals("es");
    }


    private void setLanguage(boolean isSpanish) {
        Locale locale = isSpanish ? new Locale("es") : new Locale("en");
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = locale;
        requireActivity().getResources().updateConfiguration(config, requireActivity().getResources().getDisplayMetrics());
    }

    private void showSwitchDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(getString(R.string.allow_deletion_title));

        // Agregar un Switch al diálogo
        final Switch deleteSwitch = new Switch(requireContext());
        deleteSwitch.setChecked(isDeletionEnabled);
        deleteSwitch.setText(getString(R.string.switch_activate_deletion));

        deleteSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Guardar el estado del Switch en SharedPreferences
            isDeletionEnabled = isChecked;
            requireActivity()
                    .getSharedPreferences("PokemonAppPreferences", Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean("allow_deletion", isChecked)
                    .apply();

            // Actualizar el mensaje del Toast
            Toast.makeText(requireContext(),
                    isChecked ? getString(R.string.deletion_enabled) : getString(R.string.deletion_disabled),
                    Toast.LENGTH_SHORT).show();
        });

        builder.setView(deleteSwitch);
        builder.setPositiveButton(getString(R.string.close), (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
