package org.example.demo;

public class Usuario {

    private String username;
    private String funcao;

    public Usuario(String username, String funcao) {
        this.username = username;
        this.funcao = funcao;
    }

    public String getUsername() {
        return username;
    }

    public String getFuncao() {
        return funcao;
    }
}