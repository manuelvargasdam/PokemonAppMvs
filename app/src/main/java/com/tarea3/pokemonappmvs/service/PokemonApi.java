package com.tarea3.pokemonappmvs.service;

import com.tarea3.pokemonappmvs.entity.Pokemon;
import com.tarea3.pokemonappmvs.entity.PokemonResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokemonApi {
    @GET("pokemon")
    Call<PokemonResponse> getPokemonList(@Query("offset") int offset, @Query("limit") int limit);

    // Método para obtener los detalles de un Pokémon específico
    @GET("pokemon/{name}")
    Call<Pokemon> getPokemonDetails(@Path("name") String name);
}