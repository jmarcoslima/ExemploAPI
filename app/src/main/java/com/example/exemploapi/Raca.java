package com.example.exemploapi;

import java.util.ArrayList;

public class Raca {
    private String nome;

    private String urlImagem;
    private ArrayList<String> subNome = new ArrayList<String>();

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public ArrayList<String> getSubNome() {
        return subNome;
    }

    public void setSubNome(ArrayList<String> subNome) {
        this.subNome = subNome;
    }

    @Override
    public String toString() {
        return nome;
    }
}

