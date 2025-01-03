package com.tarea3.pokemonappmvs.entity;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Pokemon {
    private int id; // Índice de Pokédex
    private String name; // Nombre del Pokémon

    @SerializedName("sprites")
    private Sprites sprites; // Foto del Pokémon

    private List<Types> types; // Tipo(s) del Pokémon
    private int weight; // Peso del Pokémon
    private int height; // Altura del Pokémon
    private String idUsuario;

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }

    public List<Types> getTypes() {
        return types;
    }

    public void setTypes(List<Types> types) {
        this.types = types;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public static class Sprites {
        @SerializedName("front_default")
        private String frontDefault; // URL de la foto frontal

        public String getFrontDefault() {
            return frontDefault;
        }

        public void setFrontDefault(String frontDefault) {
            this.frontDefault = frontDefault;
        }
    }

    public static class Types {
        private Type type;

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public static class Type {
            private String name; // Nombre del tipo

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
