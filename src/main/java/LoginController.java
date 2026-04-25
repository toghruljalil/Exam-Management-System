import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class LoginController {
    private static final String URL = "jdbc:postgresql://localhost:5432/Sinav_Yonetim_Sistemi";
    private static final String USER = "postgres";
    private static final String PASSWORD = "06green08";

    @FXML private TextField eposta;
    @FXML private TextField sifre;

    public static String bolum;

    @FXML
    public void Login(ActionEvent event){
        String query = "SELECT sifre FROM kullanicilar WHERE eposta = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, eposta.getText());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                if(rs.getString("sifre").equals(sifre.getText())) {
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();
                    Role(eposta.getText());
                }
                else{
                    CallAlert("Uyarı", "Yanlış Giriş!", "Eposta veya şifre yanlış! Lütfen yeniden deneyiniz.",  Alert.AlertType.WARNING);
                }
            }
            else {
                CallAlert("Uyarı", "Yanlış Giriş!", "Eposta veya şifre yanlış! Lütfen yeniden deneyiniz.",   Alert.AlertType.WARNING);
            }
        }
        catch (SQLException | IOException e) {
            System.out.println("Hata kod: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void Role(String eposta) throws IOException {
        String query = "SELECT rol, bolum FROM kullanicilar WHERE eposta = ?";
        String window = "";
        String panelTitle = "";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, eposta);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                if(rs.getString("rol").equals("Admin")) {
                    window = "AdminPanel.fxml";
                    panelTitle = "Admin Paneli";
                }
                else if(rs.getString("rol").equals("Kordinator")) {
                    window = "CordinatorPanel.fxml";
                    panelTitle = "Kordinatör Paneli";
                }
                else
                {
                    CallAlert("Uyarı", "Belirsiz Giriş!", "Girilen kullanıcının rolü belirsizdir!", Alert.AlertType.WARNING);
                }

                bolum = rs.getString("bolum");
            }
        }
        catch (SQLException e) {
            System.out.println("Hata kod: " + e.getMessage());
        }
        Parent root = FXMLLoader.load(getClass().getResource(window));
        Stage newStage = new Stage();
        newStage.setTitle(panelTitle);
        newStage.setScene(new Scene(root));
        try {
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png")));
            newStage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("İkon yüklenemedi: " + e.getMessage());
        }
        newStage.show();
    }

    private void CallAlert(String title, String header, String message, Alert.AlertType type)
    {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
