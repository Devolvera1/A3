package org.example.demo;

import javafx.beans.property.*;

public class RegistroPonto {
    private final IntegerProperty ID = new SimpleIntegerProperty();
    private final IntegerProperty funcionario_id = new SimpleIntegerProperty();
    private final StringProperty data = new SimpleStringProperty();
    private final StringProperty entrada = new SimpleStringProperty();
    private final StringProperty saida = new SimpleStringProperty();
    private final StringProperty entrada2 = new SimpleStringProperty();
    private final StringProperty saida2 = new SimpleStringProperty();
    private final StringProperty observacao = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();

    private final StringProperty nomeFuncionario = new SimpleStringProperty();

    public RegistroPonto(int ID, String nome, String data, String ent1,  String sai1, String ent2, String sai2, String obs,String status) {
        this.funcionario_id.set(ID);
        this.nomeFuncionario.set(nome);
        this.data.set(data);
        this.entrada.set(ent1);
        this.saida.set(sai1);
        this.entrada2.set(ent2);
        this.saida2.set(sai2);
        this.observacao.set(obs);
        this.status.set(status);
    }

    public StringProperty nomeFuncionarioProperty() { return nomeFuncionario; }


    public IntegerProperty id() { return ID; }
    public IntegerProperty funcionarioIDProperty() { return funcionario_id; }
    public StringProperty dataProperty() { return data; }
    public StringProperty entradaProperty() { return entrada; }
    public StringProperty saidaProperty() { return saida; }
    public StringProperty entrada2Property() { return entrada2; }
    public StringProperty saida2Property() { return saida2; }
    public StringProperty observacaoProperty() { return observacao; }
    public StringProperty statusProperty() { return status; }


}
