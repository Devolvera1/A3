package org.example.demo;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Funcionario {
    private final IntegerProperty id;
    private final StringProperty nome;
    private final StringProperty cpf;
    private final StringProperty email;
    private final StringProperty telefone;
    private final StringProperty cargo;
    private final DoubleProperty salario;
    private final StringProperty status;
    private final ObjectProperty<LocalDate> data_admissao;
    private final StringProperty departamento_id;

    public Funcionario(int id, String nome, String cpf, String email, String telefone, String cargo, double salario, String status, LocalDate data_admissao,  String departamento_id) {
        this.id = new SimpleIntegerProperty(id);
        this.nome = new SimpleStringProperty(nome);
        this.cpf = new SimpleStringProperty(cpf);
        this.email = new SimpleStringProperty(email);
        this.telefone = new SimpleStringProperty(telefone);
        this.cargo = new SimpleStringProperty(cargo);
        this.salario = new SimpleDoubleProperty(salario);
        this.status = new SimpleStringProperty(status);
        this.data_admissao = new SimpleObjectProperty<>(data_admissao);
        this.departamento_id = new SimpleStringProperty(departamento_id);
    }

    public IntegerProperty id() { return id; }
    public StringProperty nome() { return nome; }
    public StringProperty CPF() { return cpf; }
    public StringProperty email() { return email; }
    public StringProperty telefone() { return telefone; }
    public StringProperty cargo() { return cargo; }
    public DoubleProperty salario() { return salario; }
    public StringProperty status() { return status; }
    public ObjectProperty<LocalDate> data_admissao() { return data_admissao; }

    public Funcionario(int id, String nome) {
        this(id, nome, null, null, null, null, 0.0, null, null, null    );
    }
    public int getId() { return id.get(); }
    public String getNome() { return nome.get(); }
    public IntegerProperty idProperty() { return id; }
    public StringProperty nomeProperty() { return nome; }
    public String getDepartamentoNome() { return departamento_id.get(); }

    @Override
    public String toString() {
        return getNome();
    }
}


