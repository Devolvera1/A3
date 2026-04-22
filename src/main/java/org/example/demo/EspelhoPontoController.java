package org.example.demo;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EspelhoPontoController implements Initializable {
    private final Database db = new Database();
    private Usuario usuarioLogado;

    @FXML private TableView<RegistroPonto> tabelaPonto;
    @FXML private Button Adicionar, Editar, Deletar;

    @FXML private TableColumn<RegistroPonto, Integer> colFuncID;
    @FXML private TableColumn<RegistroPonto, String> colNome, colData, colEntrada, colSaida, colEntrada2, colSaida2, colObs, colStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colFuncID.setCellValueFactory(c -> c.getValue().funcionarioIDProperty().asObject());
        colNome.setCellValueFactory(c -> c.getValue().nomeFuncionarioProperty());
        colData.setCellValueFactory(c -> c.getValue().dataProperty());
        colEntrada.setCellValueFactory(c -> c.getValue().entradaProperty());
        colSaida.setCellValueFactory(c -> c.getValue().saidaProperty());
        colEntrada2.setCellValueFactory(c -> c.getValue().entrada2Property());
        colSaida2.setCellValueFactory(c -> c.getValue().saida2Property());
        colObs.setCellValueFactory(c -> c.getValue().observacaoProperty());
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());

        tabelaPonto.setRowFactory(tv -> {
            TableRow<RegistroPonto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Deletar(new ActionEvent());
                }
            });
            return row;
        });
    }

    public void setUsuario(Usuario user) {
        this.usuarioLogado = user;
        if (user != null) {
            tabelaPonto.setItems(db.getPontosPorPerfil(user));
            boolean naoEhAdmin = !user.getFuncao().equalsIgnoreCase("ADMIN");
            Adicionar.setDisable(naoEhAdmin);
            Editar.setDisable(naoEhAdmin);
            Deletar.setDisable(naoEhAdmin);
        }
    }

    @FXML
    private void Adicionar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/AdicionarEspelho.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/demo/Img/logo.png")));
            stage.setScene(new Scene(root));
            stage.showAndWait();
            Reload(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Editar(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/EditarEspelho.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/demo/Img/logo.png")));
            stage.setScene(new Scene(root));
            stage.showAndWait();
            Reload(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Deletar(ActionEvent event) {
        RegistroPonto selecionado = tabelaPonto.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            exibirAlerta("Atenção", "Selecione um registro na tabela para excluir!", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja realmente deletar o ponto do dia " + selecionado.getData() + "?");

        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                if (db.deletarPonto(selecionado.getId())) {
                    tabelaPonto.getItems().remove(selecionado);
                    exibirAlerta("Sucesso!", "Registro removido!", Alert.AlertType.INFORMATION);
                } else {
                    exibirAlerta("Erro", "Erro ao excluir do banco de dados.", Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void exibirAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void Reload(ActionEvent event) {
        if (this.usuarioLogado != null) {
            tabelaPonto.setItems(db.getPontosPorPerfil(this.usuarioLogado));
            tabelaPonto.refresh();
        }
    }
}
