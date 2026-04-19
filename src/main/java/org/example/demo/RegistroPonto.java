package org.example.demo;

import javafx.beans.property.*;

public class RegistroPonto {
    private final IntegerProperty ID = new SimpleIntegerProperty();
    private final IntegerProperty funcionario_id = new SimpleIntegerProperty();
    private final StringProperty data = new SimpleStringProperty();
    private final StringProperty entrada = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();

    private final StringProperty nomeFuncionario = new SimpleStringProperty();

    public RegistroPonto(int ID, String nome, String data, String status) {
        this.funcionario_id.set(ID);
        this.nomeFuncionario.set(nome);
        this.data.set(data);
        this.status.set(status);
    }

    public StringProperty nomeFuncionarioProperty() { return nomeFuncionario; }


    public IntegerProperty id() { return ID; }
    public IntegerProperty funcionarioIDProperty() { return funcionario_id; }
    public StringProperty dataProperty() { return data; }
    public StringProperty entradaProperty() { return entrada; }
    public StringProperty statusProperty() { return status; }


}
