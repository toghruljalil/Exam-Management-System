import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class AdminController {
    private static final String URL = "jdbc:postgresql://localhost:5432/Sinav_Yonetim_Sistemi";
    private static final String USER = "postgres";
    private static final String PASSWORD = "06green08";

    //Kordinator Ekleme
    @FXML TextField bolumAdi;
    @FXML TextField kordinatorAdi;
    @FXML TextField kordinatorSoyadi;
    @FXML TextField eposta;
    @FXML TextField sifre;
    //Kordinator Listesi
    @FXML TableView kordinatortable;
    @FXML TableColumn bolumadicol;
    @FXML TableColumn adsoyad;
    @FXML TableColumn epostacol;
    ObservableList<KordinatorModel> kordinatorListesi = FXCollections.observableArrayList();
    //Bolum Yonetme Table
    @FXML TableView kayitlibolumlertable;
    @FXML TableColumn kayitlibolumadi;
    @FXML TableColumn<KordinatorModel, Void> kayitlibolumislem;
    ObservableList<KordinatorModel> kayitlibolumListesi = FXCollections.observableArrayList();

    @FXML private void initialize(){
        //Kayitli Kordinatorler
        bolumadicol.setCellValueFactory(new PropertyValueFactory<>("bolumadi"));
        adsoyad.setCellValueFactory(new PropertyValueFactory<>("adsoyad"));
        epostacol.setCellValueFactory(new PropertyValueFactory<>("eposta"));
        KordinatorYukle();

        //Kayitli Bolumlere Erisim
        kayitlibolumadi.setCellValueFactory(new PropertyValueFactory<>("bolumadi"));
        kayitlibolumislem.setCellFactory(col -> new TableCell<>() {
            private final Button edit = new Button("Yönet");
            private final HBox box = new HBox(1, edit);

            {
                edit.setOnAction(e -> {
                    var data = getTableView().getItems().get(getIndex());
                    try {
                        DashboardAc(data);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    data = null;
                });

                edit.setStyle("-fx-background-color:#0a9a4c; -fx-border-color:#b2ffba; -fx-text-fill:white; -fx-background-radius:5;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
        BolumYukle();
    }

    @FXML
    private void KordinatorEkle(){
        String query = "INSERT INTO kullanicilar (eposta, rol, bolum, sifre, ad, soyad) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, eposta.getText());
            stmt.setString(2, "Kordinator");
            stmt.setString(3, bolumAdi.getText());
            stmt.setString(4, sifre.getText());
            stmt.setString(5, kordinatorAdi.getText());
            stmt.setString(6, kordinatorSoyadi.getText());
            stmt.executeUpdate();
            CallAlert("Mesaj", "İşlem başarılı!", bolumAdi.getText() + " bölümü için sisteme yeni kordinatör başarıyla eklendi! ✅", Alert.AlertType.INFORMATION);
            //Temizleme
            bolumAdi.clear();
            kordinatorAdi.clear();
            kordinatorSoyadi.clear();
            eposta.clear();
            sifre.clear();
            KordinatorYukle();
            BolumYukle();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void KordinatorYukle()
    {
        String sql = "SELECT * FROM kullanicilar WHERE rol = 'Kordinator'";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            kordinatorListesi.clear();
            while (rs.next()) {
                KordinatorModel kordinator = new KordinatorModel(
                        rs.getString("bolum"),
                        rs.getString("eposta"),
                        rs.getString("ad") + " " + rs.getString("soyad")
                );
                kordinatorListesi.add(kordinator);
            }
            kordinatortable.setItems(kordinatorListesi);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void BolumYukle()
    {
        String sql = "SELECT bolum FROM kullanicilar WHERE rol = 'Kordinator'";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            kayitlibolumListesi.clear();
            while (rs.next()) {
                KordinatorModel kordinator = new KordinatorModel(
                        rs.getString("bolum"),
                        null,
                        null
                );
                kayitlibolumListesi.add(kordinator);
            }
            kayitlibolumlertable.setItems(kayitlibolumListesi);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void DashboardAc(KordinatorModel data) throws IOException {
        LoginController.bolum = data.getBolumadi();
        Parent root = FXMLLoader.load(getClass().getResource("CordinatorPanel.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Kordinatör Paneli");
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
