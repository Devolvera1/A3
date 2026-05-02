package org.example.demo.util;

public class Usuario {

    private int id;
    private String username;
    private String funcao;

    public Usuario(int id, String username, String funcao) {
        this.id = id;
        this.username = username;
        this.funcao = funcao;
    }



    public String getUsername() {
        return username;
    }
    public int getId() { return id; }

    public String getFuncao() {
        return funcao;
    }
}