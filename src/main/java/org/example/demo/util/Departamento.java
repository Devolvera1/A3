package org.example.demo.util;

import javafx.beans.property.*;

public class Departamento {
    private int id;
    private String nome;
    public Departamento(int id, String nome) { this.id = id; this.nome = nome; }
    public int getId() { return id; }
    public String getNome() { return nome; }
    @Override public String toString() { return nome; }

    public static class RegistroPonto {
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

        public RegistroPonto(int ID, int funcID, String nome, String data, String ent1, String sai1, String ent2, String sai2, String obs, String status) {
            this.ID.set(ID);
            this.funcionario_id.set(funcID);
            this.nomeFuncionario.set(nome);
            this.data.set(data);
            this.entrada.set(ent1);
            this.saida.set(sai1);
            this.entrada2.set(ent2);
            this.saida2.set(sai2);
            this.observacao.set(obs);
            this.status.set(status);
        }

        public int getId() { return ID.get(); }
        public String getData() { return data.get(); }
        public String getNomeFuncionario() { return nomeFuncionario.get(); }
        public IntegerProperty idProperty() { return ID; }
        public IntegerProperty funcionarioIDProperty() { return funcionario_id; }
        public StringProperty nomeFuncionarioProperty() { return nomeFuncionario; }
        public StringProperty dataProperty() { return data; }
        public StringProperty entradaProperty() { return entrada; }
        public StringProperty saidaProperty() { return saida; }
        public StringProperty entrada2Property() { return entrada2; }
        public StringProperty saida2Property() { return saida2; }
        public StringProperty observacaoProperty() { return observacao; }
        public StringProperty statusProperty() { return status; }

        public int getFuncionarioId() { return funcionario_id.get(); }
        public String getEntrada() { return entrada.get(); }
        public String getSaidaAlmoco() { return saida.get(); }
        public String getRetornoAlmoco() { return entrada2.get(); }
        public String getSaida() { return saida2.get(); }
        public String getObservacao() { return observacao.get(); }
        public String getStatus() { return status.get(); }
    }
    public static class HistoricoSalario {
        private final IntegerProperty id = new SimpleIntegerProperty();
        private final IntegerProperty funcionarioId = new SimpleIntegerProperty();
        private final StringProperty nomeFuncionario = new SimpleStringProperty();
        private final DoubleProperty salario = new SimpleDoubleProperty();
        private final StringProperty dataInicio = new SimpleStringProperty();
        private final StringProperty dataFim = new SimpleStringProperty();
        private final StringProperty motivo = new SimpleStringProperty();

        public HistoricoSalario(int id, int funcionarioId, String nomeFuncionario, double salario,
                                String dataInicio, String dataFim, String motivo) {
            this.id.set(id);
            this.funcionarioId.set(funcionarioId);
            this.nomeFuncionario.set(nomeFuncionario);
            this.salario.set(salario);
            this.dataInicio.set(dataInicio);
            this.dataFim.set(dataFim);
            this.motivo.set(motivo);
        }

        public int getId() { return id.get(); }
        public int getFuncionarioId() { return funcionarioId.get(); }
        public String getNomeFuncionario() { return nomeFuncionario.get(); }
        public double getSalario() { return salario.get(); }
        public String getDataInicio() { return dataInicio.get(); }
        public String getDataFim() { return dataFim.get(); }
        public String getMotivo() { return motivo.get(); }

        public IntegerProperty idProperty() { return id; }
        public IntegerProperty funcionarioIdProperty() { return funcionarioId; }
        public StringProperty nomeFuncionarioProperty() { return nomeFuncionario; }
        public DoubleProperty salarioProperty() { return salario; }
        public StringProperty dataInicioProperty() { return dataInicio; }
        public StringProperty dataFimProperty() { return dataFim; }
        public StringProperty motivoProperty() { return motivo; }
    }

}
