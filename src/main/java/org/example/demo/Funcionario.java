package org.example.demo;

import javafx.beans.property.*;

public class Funcionario {
    private final IntegerProperty id;
    private final StringProperty nome;
    private final StringProperty cpf;
    private final StringProperty email;
    private final StringProperty telefone;
    private final StringProperty cargo;
    private final DoubleProperty salario;
    private final StringProperty status;

    public Funcionario(int id, String nome, String cpf,String email, String telefone, String cargo, double salario, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.nome = new SimpleStringProperty(nome);
        this.cpf = new SimpleStringProperty(cpf);
        this.email = new SimpleStringProperty(email);
        this.telefone = new SimpleStringProperty(telefone);
        this.cargo = new SimpleStringProperty(cargo);
        this.salario = new SimpleDoubleProperty(salario);
        this.status = new SimpleStringProperty(status);
    }

    public IntegerProperty id() { return id; }
    public StringProperty nome() { return nome; }
    public StringProperty CPF() { return cpf; }
    public StringProperty email() { return email; }
    public StringProperty telefone() { return telefone; }
    public StringProperty cargo() { return cargo; }
    public DoubleProperty salario() { return salario; }
    public StringProperty status() { return status; }
}