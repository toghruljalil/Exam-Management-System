import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.controlsfx.control.CheckComboBox;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class CordinatorController {
    private static final String URL = "jdbc:postgresql://localhost:5432/Sinav_Yonetim_Sistemi";
    private static final String USER = "postgres";
    private static final String PASSWORD = "06green08";

    //ScrollPane
    @FXML ScrollPane scrollpane;
    //Tablar
    @FXML Tab dersekletab;
    @FXML Tab ogrenciekletab;
    @FXML Tab ogrencilistesitab;
    @FXML Tab derslistesitab;
    @FXML Tab sinavprogramitab;
    @FXML Tab oturmaduzenitab;
    //Derslik ekleme
    @FXML TextField bolumAdi;
    @FXML TextField derslikKodu;
    @FXML TextField derslikAdi;
    @FXML TextField kapasite;
    @FXML TextField enineSira;
    @FXML TextField boyunaSira;
    @FXML TextField siraYapisi;
    //Derslikler tablosu
    @FXML TableView dersliktable;
    @FXML TableColumn bolumadicol;
    @FXML TableColumn derslikkoducol;
    @FXML TableColumn derslikadicol;
    @FXML TableColumn derslikkapasitesicol;
    @FXML TableColumn eninesiracol;
    @FXML TableColumn boyunasiracol;
    @FXML TableColumn sirayapisicol;
    @FXML TableColumn<DersliklerModel, Void> derslikislemcol;
    ObservableList<DersliklerModel> derslikListesi = FXCollections.observableArrayList();
    //Derslik arama
    @FXML TextField derslikarafield;
    //Derslik duzenle
    @FXML TextField bolumadiduzenle;
    @FXML TextField derslikadiduzenle;
    @FXML TextField eninesiraduzenle;
    @FXML TextField boyunasiraduzenle;
    @FXML TextField kapasiteduzenle;
    @FXML TextField sirayapisiduzenle;
    //Derslik Datasi
    DersliklerModel data;
    //Ders Ekleme
    @FXML TextField derslistesiadi;
    @FXML TableView derstable1;
    @FXML TableColumn derskodu1;
    @FXML TableColumn dersadi1;
    @FXML TableColumn dershocaadi1;
    @FXML TableColumn derssinif1;
    @FXML TableColumn dersyapi1;
    @FXML Button derslistesionayla;
    ObservableList<EklenecekDersModel> dersListesi = FXCollections.observableArrayList();
    //Ogrenci Ekleme
    @FXML TextField ogrencilistesiadi;
    @FXML TableView ogrencitable1;
    @FXML TableColumn ogrencikodu1;
    @FXML TableColumn ogrenciisim1;
    @FXML TableColumn ogrencisinif1;
    @FXML TableColumn ogrenciders1;
    @FXML Button ogrencilistesionayla;
    ObservableList<EklenecekOgrenciModel> ogrenciListesi = FXCollections.observableArrayList();
    //Ogrenci Listesi
    @FXML TextField ogrenciara;
    @FXML TextField ogrencininadisoyadi;
    @FXML TableView ogrencitable2;
    @FXML TableColumn ogrencidersadi;
    @FXML TableColumn ogrenciderskodu;
    ObservableList<DersModel> ogrenciDersListesi = FXCollections.observableArrayList();
    //Ders Listesi
    @FXML TextField dersara;
    @FXML TableView derstable2;
    @FXML TableColumn derskodu2;
    @FXML TableColumn dersadi2;
    ObservableList<DersModel> dersListesi2 = FXCollections.observableArrayList();
    @FXML TableView dersialanogrencilertable;
    @FXML TableColumn dersialanogrencino;
    @FXML TableColumn dersialanogrenciadi;
    ObservableList<OgrenciModel> ogrenciListesi2 = FXCollections.observableArrayList();
    //Kisitlar
    //Ders Table
    @FXML TableView<CikarilacakDersModel> cikarilacakderstable;
    @FXML TableColumn<CikarilacakDersModel, String> cikarilacakdersadi;
    @FXML TableColumn<CikarilacakDersModel, String> cikarilacakderskodu;
    @FXML TableColumn<CikarilacakDersModel, Boolean> cikarilacakderssec;
    ObservableList<CikarilacakDersModel> cikarilacakdersListesi = FXCollections.observableArrayList();
    //Takvim
    @FXML DatePicker sinavbaslangicdate;
    @FXML DatePicker sinavbitisdate;
    @FXML CheckComboBox dahilolmayangunler;
    //Sinav Turu
    @FXML ComboBox sinavturu;
    //Sinav Suresi
    @FXML private TableView<SinavaDahilDerslerModel> sinavsuresitable;
    @FXML private TableColumn<SinavaDahilDerslerModel, String> sinavsuresidersadi;
    @FXML private TableColumn<SinavaDahilDerslerModel, String> sinavsuresiderskodu;
    @FXML private TableColumn<SinavaDahilDerslerModel, String> sinavsuresi;
    ObservableList<SinavaDahilDerslerModel> sinavsuresiListesi = FXCollections.observableArrayList();
    //Bekleme suresi
    @FXML TextField beklemesuresi;
    //Ayni Zamanda Sinav Olusumu
    @FXML CheckBox aynizamandasinav;
    //Sinav Table
    @FXML TableView sinavtable;
    @FXML TableColumn sinavtarih;
    @FXML TableColumn sinavsaat;
    @FXML TableColumn sinavderskodu;
    @FXML TableColumn sinavdersadi;
    @FXML TableColumn sinavderslik;
    @FXML TableColumn<SinavModel, Void> sinavislem;
    ObservableList<SinavModel> sinavtableListesi = FXCollections.observableArrayList();
    //Oturma Duzeni Table
    @FXML TableView oturmaDuzeniTable;
    @FXML TableColumn oturogrencino;
    @FXML TableColumn oturogrenciadi;
    @FXML TableColumn oturduguderslik;
    @FXML TableColumn oturdugusatir;
    @FXML TableColumn oturdugusutun;
    ObservableList<OturmaDuzeniModel> oturmaDuzeniListesi = FXCollections.observableArrayList();
    ObservableList<OturmaDuzeniModel> tamoturmaDuzeniListesi = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        //Baslangic Metodlari
        //Tab Kontrolu
        OgrenciVeDersVarmi();
        //Ders Listesi Yukle
        DersYukle();

        //Derslik Ekleme (bolum)
        bolumAdi.setText(LoginController.bolum);

        //Derslikler Tablosu
        bolumadicol.setCellValueFactory(new PropertyValueFactory<>("bolumadi"));
        derslikkoducol.setCellValueFactory(new PropertyValueFactory<>("derslikkodu"));
        derslikadicol.setCellValueFactory(new PropertyValueFactory<>("derslikadi"));
        derslikkapasitesicol.setCellValueFactory(new PropertyValueFactory<>("kapasite"));
        eninesiracol.setCellValueFactory(new PropertyValueFactory<>("eninesira"));
        boyunasiracol.setCellValueFactory(new PropertyValueFactory<>("boyunasira"));
        sirayapisicol.setCellValueFactory(new PropertyValueFactory<>("sirayapisi"));
        DerslikleriYukle();
        dersliktable.setItems(derslikListesi);
        derslikislemcol.setCellFactory(col -> new TableCell<>() {
            private final Button edit = new Button("Düzenle");
            private final Button del = new Button("Sil");
            private final Button view = new Button("👁");
            private final HBox box = new HBox(5, edit, del, view);

            {
                del.setOnAction(e -> {
                    var data = getTableView().getItems().get(getIndex());
                    DerslikSil(data);
                    data = null;
                });

                edit.setOnAction(e -> {
                    data = getTableView().getItems().get(getIndex());
                    //TextFieldlari Doldur
                    bolumadiduzenle.setText(data.getBolumadi());
                    derslikadiduzenle.setText((data.getDerslikadi()));
                    eninesiraduzenle.setText(String.valueOf(data.getEninesira()));
                    boyunasiraduzenle.setText(String.valueOf(data.getBoyunasira()));
                    kapasiteduzenle.setText(String.valueOf(data.getKapasite()));
                    sirayapisiduzenle.setText(String.valueOf(data.getSirayapisi()));
                    //TextFieldlari Editable Yap
                    bolumadiduzenle.setEditable(true);
                    derslikadiduzenle.setEditable(true);
                    eninesiraduzenle.setEditable(true);
                    boyunasiraduzenle.setEditable(true);
                    kapasiteduzenle.setEditable(true);
                    sirayapisiduzenle.setEditable(true);
                    //Scroll ayarla
                    scrollpane.setVvalue(1);
                });

                view.setOnAction(e -> {
                    var data = getTableView().getItems().get(getIndex());
                    if (data != null) {
                        oturmaDuzeniGoster(data.getEninesira(), data.getBoyunasira(), data.getSirayapisi(), "");
                    }
                    data = null;
                });

                del.setStyle("-fx-background-color:#d9534f; -fx-border-color:#fb7a79; -fx-text-fill:white; -fx-background-radius:5;");
                edit.setStyle("-fx-background-color:#4f6fd9; -fx-border-color:#7996f6; -fx-text-fill:white; -fx-background-radius:5;");
                view.setStyle("-fx-background-color:#61d94f; -fx-border-color:#acfd9f; -fx-text-fill:white; -fx-background-radius:5;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        //Ders Onay Tablosu
        derskodu1.setCellValueFactory(new PropertyValueFactory<>("derskodu"));
        dersadi1.setCellValueFactory(new PropertyValueFactory<>("dersadi"));
        dershocaadi1.setCellValueFactory(new PropertyValueFactory<>("hocaadi"));
        derssinif1.setCellValueFactory(new PropertyValueFactory<>("sinif"));
        dersyapi1.setCellValueFactory(new PropertyValueFactory<>("yapi"));

        //Ogrenci Onay Tablosu
        ogrencikodu1.setCellValueFactory(new PropertyValueFactory<>("ogrencikodu"));
        ogrenciisim1.setCellValueFactory(new PropertyValueFactory<>("ogrenciisim"));
        ogrencisinif1.setCellValueFactory(new PropertyValueFactory<>("sinif"));
        ogrenciders1.setCellValueFactory(new PropertyValueFactory<>("aldigiders"));

        //Ogrenci Ders Tablosu
        ogrencidersadi.setCellValueFactory(new PropertyValueFactory<>("dersadi"));
        ogrenciderskodu.setCellValueFactory(new PropertyValueFactory<>("derskodu"));

        //Ders Tablosu
        dersadi2.setCellValueFactory(new PropertyValueFactory<>("dersadi"));
        derskodu2.setCellValueFactory(new PropertyValueFactory<>("derskodu"));
        derstable2.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && derstable2.getSelectionModel().getSelectedItem() != null) {
                DersModel selected = (DersModel) derstable2.getSelectionModel().getSelectedItem();
                if(selected!=null){
                    DersiAlanOgrenciler(selected.getDerskodu());
                }
            }
        });

        //Dersi Alan Ogrenciler Tablosu
        dersialanogrencino.setCellValueFactory(new PropertyValueFactory<>("ogrencino"));
        dersialanogrenciadi.setCellValueFactory(new PropertyValueFactory<>("ogrenciadi"));

        //Cikarilacak Ders Tablosu
        cikarilacakdersadi.setCellValueFactory(new PropertyValueFactory<>("dersAdi"));
        cikarilacakderskodu.setCellValueFactory(new PropertyValueFactory<>("dersKodu"));
        cikarilacakderssec.setCellValueFactory(cellData -> cellData.getValue().secildiProperty());

        cikarilacakderssec.setCellFactory(tc -> {
            CheckBoxTableCell<CikarilacakDersModel, Boolean> cell = new CheckBoxTableCell<>(index -> {
                BooleanProperty active = cikarilacakderstable.getItems().get(index).secildiProperty();
                return active;
            });
            return cell;
        });

        cikarilacakderstable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(CikarilacakDersModel ders, boolean empty) {
                super.updateItem(ders, empty);
                if (empty || ders == null) {
                    setStyle("");
                    return;
                }

                updateRowColor(ders.isSecildi());

                ders.secildiProperty().addListener((obs, oldVal, newVal) -> {
                    updateRowColor(newVal);
                    SinavaDahilDersleriYukle();
                });
            }

            private void updateRowColor(boolean isSelected) {
                if (isSelected) {
                    setStyle("-fx-background-color: rgba(255, 80, 80, 0.3);");
                } else {
                    setStyle("");
                }
            }
        });
        CikarilacakTabloIcinDersYukle();

        //Sinava Dahil Olan Derslerin Suresi
        sinavsuresidersadi.setCellValueFactory(new PropertyValueFactory<>("dersAdi"));
        sinavsuresiderskodu.setCellValueFactory(new PropertyValueFactory<>("dersKodu"));
        sinavsuresi.setCellValueFactory(new PropertyValueFactory<>("dersSuresi"));
        sinavsuresitable.setEditable(true);
        sinavsuresi.setCellFactory(TextFieldTableCell.forTableColumn());

        sinavsuresi.setOnEditCommit(event -> {
            SinavaDahilDerslerModel ders = event.getRowValue();
            ders.setDersSuresi(event.getNewValue());
            for (SinavaDahilDerslerModel dto : sinavsuresiListesi) {
                System.out.println(dto.getDersAdi() + " " + dto.getDersKodu() + " " + dto.getDersSuresi());
            }
        });
        SinavaDahilDersleriYukle();

        //Sinav Turleri
        sinavturu.getItems().addAll("Vize", "Final", "Bütünleme");

        //Sinav Table
        sinavtarih.setCellValueFactory(new PropertyValueFactory<>("tarih"));
        sinavsaat.setCellValueFactory(new PropertyValueFactory<>("saat"));
        sinavdersadi.setCellValueFactory(new PropertyValueFactory<>("dersadi"));
        sinavderskodu.setCellValueFactory(new PropertyValueFactory<>("derskodu"));
        sinavderslik.setCellValueFactory(new PropertyValueFactory<>("derslik"));
        sinavislem.setCellFactory(col -> new TableCell<>() {
            private final Button bak = new Button("👁");
            private final Button olustur = new Button("Oluştur");
            private final HBox box = new HBox(5, bak, olustur);

            {
                bak.setOnAction(e -> {
                    var data = getTableView().getItems().get(getIndex());
                    String derslikler = data.getDerslik();
                    String[] derslik = derslikler.split(",");
                    for (String d : derslik) {
                        DerslikOturmaDuzeniGoster(d.trim());
                    }
                    data = null;
                });

                olustur.setOnAction(e -> {
                    var data = getTableView().getItems().get(getIndex());
                    oturmaDuzeniListesi.clear();
                    //Ogrenci Listesini Al
                    List<OgrenciModel> ogrenciListesi = new ArrayList<>();
                    SinavDersiAlanOgrenciler(ogrenciListesi, data.getDerskodu());
                    //Derslik Listesini Al
                    List<String> derslikListesi = new ArrayList<>();
                    String derslikler = data.getDerslik();
                    String[] derslik = derslikler.split(",");
                    for (String d : derslik) {
                        derslikListesi.add(d.trim());
                    }
                    for(int i = 0; ogrenciListesi.size() > 0; i++)
                    {
                        DerslikBilgileriniAl(derslikListesi.get(i), ogrenciListesi);
                    }
                    oturmaDuzeniTable.setItems(tamoturmaDuzeniListesi);
                    data = null;
                });

                bak.setStyle("-fx-background-color:#61d94f; -fx-border-color:#acfd9f; -fx-text-fill:white; -fx-background-radius:5;");
                olustur.setStyle("-fx-background-color:#4f6fd9; -fx-border-color:#7996f6; -fx-text-fill:white; -fx-background-radius:5;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
        SinavYukle();

        //Oturma Duzeni Table
        oturogrencino.setCellValueFactory(new PropertyValueFactory<>("ogrencino"));
        oturogrenciadi.setCellValueFactory(new PropertyValueFactory<>("ogrenciadi"));
        oturduguderslik.setCellValueFactory(new PropertyValueFactory<>("derslik"));
        oturdugusatir.setCellValueFactory(new PropertyValueFactory<>("satir"));
        oturdugusutun.setCellValueFactory(new PropertyValueFactory<>("sutun"));
    }

    @FXML private void DerslikEkle()
    {
        String query = "INSERT INTO derslikler (bolum, derslik_kodu, derslik_adi, derslik_kapasitesi, enine_sira_sayisi, boyuna_sira_sayisi, sira_yapisi)\n" +
                "VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, LoginController.bolum);
            stmt.setInt(2, Integer.parseInt(derslikKodu.getText()));
            stmt.setString(3, derslikAdi.getText());
            stmt.setInt(4, Integer.parseInt(kapasite.getText()));
            stmt.setInt(5, Integer.parseInt(enineSira.getText()));
            stmt.setInt(6, Integer.parseInt(boyunaSira.getText()));
            stmt.setInt(7, Integer.parseInt(siraYapisi.getText()));
            stmt.executeUpdate();
            //Bildirim Goster
            CallAlert("Mesaj", "İşlem başarılı!", derslikAdi.getText() + " dersliği veritabanına başarıyla eklendi! ✅", Alert.AlertType.INFORMATION);
            //TextFieldlari Temizle
            derslikKodu.clear();
            derslikAdi.clear();
            kapasite.clear();
            enineSira.clear();
            boyunaSira.clear();
            siraYapisi.clear();
            //Derslik Listesini Guncelle
            DerslikleriYukle();
            dersliktable.setItems(derslikListesi);
        }
        catch (SQLException e) {
            System.out.println("Hata kod: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void DerslikleriYukle()
    {
        String query = "SELECT * FROM derslikler WHERE bolum = ? ORDER BY derslik_kodu ASC";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, LoginController.bolum);
            ResultSet rs = stmt.executeQuery();
            derslikListesi.clear();
            while (rs.next()) {
                DersliklerModel derslik = new DersliklerModel(
                        rs.getString("bolum"),
                        rs.getInt("derslik_kodu"),
                        rs.getString("derslik_adi"),
                        rs.getInt("derslik_kapasitesi"),
                        rs.getInt("enine_sira_sayisi"),
                        rs.getInt("boyuna_sira_sayisi"),
                        rs.getInt("sira_yapisi")
                );
                derslikListesi.add(derslik);
            }
            //Diger Tablari ac
            if(!derslikListesi.isEmpty())
            {
                dersekletab.setDisable(false);
                ogrenciekletab.setDisable(false);
            }
            else{
                dersekletab.setDisable(true);
                ogrenciekletab.setDisable(true);
            }
        }
        catch (SQLException e) {
            System.out.println("Hata kod: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void DerslikSil(DersliklerModel data)
    {
        String query = "DELETE FROM derslikler WHERE derslik_kodu = ? AND bolum = ?;";
        int kod = data.getDerslikkodu();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, kod);
            stmt.setString(2, LoginController.bolum);
            stmt.executeUpdate();
            //Bildirim Goster
            CallAlert("Mesaj", "İşlem Başarılı!", "Derslik başarıyla silindi! 🗑️", Alert.AlertType.INFORMATION);
            DerslikleriYukle();
        }
        catch (SQLException e) {
            System.out.println("Hata kod: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void DerslikAra()
    {
        String query = "SELECT * FROM derslikler WHERE derslik_kodu = ? AND bolum = ?;";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)){
            if(derslikarafield.getText().isEmpty())
            {
                DerslikleriYukle();
                dersliktable.setItems(derslikListesi);
            }
            else {
                stmt.setInt(1, Integer.parseInt(derslikarafield.getText()));
                stmt.setString(2, LoginController.bolum);
                stmt.executeQuery();
                ResultSet rs = stmt.getResultSet();
                derslikListesi.clear();
                if (rs.next()) {
                    DersliklerModel derslik = new DersliklerModel(
                            rs.getString("bolum"),
                            rs.getInt("derslik_kodu"),
                            rs.getString("derslik_adi"),
                            rs.getInt("derslik_kapasitesi"),
                            rs.getInt("enine_sira_sayisi"),
                            rs.getInt("boyuna_sira_sayisi"),
                            rs.getInt("sira_yapisi")
                    );
                    derslikListesi.add(derslik);
                }
                dersliktable.setItems(derslikListesi);
                //TextField temizle
                derslikarafield.clear();
            }
        }
        catch (SQLException e) {
            System.out.println("Hata kod: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void DerslikDuzenle()
    {
        String query = """
                UPDATE derslikler
                SET bolum = ?,
                    derslik_adi = ?,
                    enine_sira_sayisi = ?,
                    boyuna_sira_sayisi = ?,
                    derslik_kapasitesi = ?,
                    sira_yapisi = ?
                WHERE derslik_kodu = ? AND bolum = ?;""";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, bolumadiduzenle.getText());
            stmt.setString(2, derslikadiduzenle.getText());
            stmt.setInt(3, Integer.parseInt(eninesiraduzenle.getText()));
            stmt.setInt(4, Integer.parseInt(boyunasiraduzenle.getText()));
            stmt.setInt(5, Integer.parseInt(kapasiteduzenle.getText()));
            stmt.setInt(6, Integer.parseInt(sirayapisiduzenle.getText()));
            stmt.setInt(7, data.getDerslikkodu());
            stmt.setString(8, LoginController.bolum);
            stmt.executeUpdate();
            CallAlert("Mesaj", "İşlem Başarılı!", "Derslik bilgileri başarıyla güncellendi! 🔧", Alert.AlertType.INFORMATION);
            //TextFieldlari Temizle
            bolumadiduzenle.clear();
            derslikadiduzenle.clear();
            eninesiraduzenle.clear();
            boyunasiraduzenle.clear();
            kapasiteduzenle.clear();
            sirayapisiduzenle.clear();
            //TextFieldlari Non-editable yap
            bolumadiduzenle.setEditable(false);
            derslikadiduzenle.setEditable(false);
            eninesiraduzenle.setEditable(false);
            boyunasiraduzenle.setEditable(false);
            kapasiteduzenle.setEditable(false);
            sirayapisiduzenle.setEditable(false);
            //Derslikler Tablosunu Guncelle
            DerslikleriYukle();
            dersliktable.setItems(derslikListesi);
            derslikarafield.clear();
            //Datayi sil
            data = null;
        }
        catch (SQLException e) {
            System.out.println("Hata kod: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void oturmaDuzeniGoster(int sutun, int satir, int siraYapisi, String baslik) {
        GridPane grid = oturmaDuzeniOlustur(sutun, satir, siraYapisi);

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

        Stage stage = new Stage();
        stage.setTitle("Oturma Düzeni" + " - " + baslik);
        Scene scene = new Scene(scroll, 750, 500);
        stage.setScene(scene);
        try {
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/desk.png")));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("İkon yüklenemedi: " + e.getMessage());
        }
        stage.show();
    }

    private GridPane oturmaDuzeniOlustur(int sutun, int satir, int siraYapisi) {
        sutun = sutun * siraYapisi;
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        int blokSayisi = (int) Math.ceil((double) sutun / siraYapisi);
        int boslukGenisligi = 45;

        for (int i = 0; i < satir; i++) {
            int colIndex = 0;
            for (int blok = 0; blok < blokSayisi; blok++) {
                for (int k = 0; k < siraYapisi; k++) {
                    int j = blok * siraYapisi + k;
                    if (j >= sutun) break;

                    Button seat = new Button((i + 1) + "-" + (j + 1));
                    seat.setStyle("-fx-background-color:#5e5e5e; -fx-text-fill:white; -fx-background-radius:5;");
                    if(siraYapisi == 2)
                    {
                        if((j + 1) % 2 == 1)
                        {
                            seat.setStyle("-fx-background-color:#0d7301; -fx-text-fill:white; -fx-background-radius:5;");
                        }
                    }
                    else if(siraYapisi % 2 == 1)
                    {
                        if((j + 1) % siraYapisi == 1 || (j + 1) % siraYapisi == 0 || (j + 1) % siraYapisi == 3)
                        {
                            seat.setStyle("-fx-background-color:#0d7301; -fx-text-fill:white; -fx-background-radius:5;");
                        }
                    }
                    else if(siraYapisi == 4)
                    {
                        if((j + 1) % siraYapisi == 1 || (j + 1) % siraYapisi == 0)
                        {
                            seat.setStyle("-fx-background-color:#0d7301; -fx-text-fill:white; -fx-background-radius:5;");
                        }
                    }
                    seat.setPrefSize(50, 50);
                    grid.add(seat, colIndex++, i);
                }
                if (blok < blokSayisi - 1) {
                    Region bosluk = new Region();
                    bosluk.setPrefWidth(boslukGenisligi);
                    grid.add(bosluk, colIndex++, i);
                }
            }
        }
        return grid;
    }

    @FXML
    private void DersExcelYukle() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Ders Listesi Excel Dosyasını Seç");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Dosyaları", "*.xlsx", "*.xls"));

            File file = fileChooser.showOpenDialog(new Stage());
            if (file == null) return;

            //Ders tablosunda goster
            DersParseExcel(file);
            derstable1.setItems(dersListesi);
            derslistesionayla.setDisable(false);
            //Dosya adini goster
            derslistesiadi.setText(file.getName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void OgrenciExcelYukle() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Öğrenci Listesi Excel Dosyasını Seç");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Dosyaları", "*.xlsx", "*.xls"));

            File file = fileChooser.showOpenDialog(new Stage());
            if (file == null) return;

            //Ders tablosunda goster
            OgrenciParseExcel(file);
            ogrencitable1.setItems(ogrenciListesi);
            ogrencilistesionayla.setDisable(false);
            //Dosya adini goster
            ogrencilistesiadi.setText(file.getName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void DersParseExcel(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();

        String sinif = "";
        String yapi = "Zorunlu Ders";
        for (Row row : sheet) {
            if(row.getCell(0).toString().toLowerCase().equals("ders kodu")) continue;

            if(row.getCell(0).toString().toLowerCase().contains(". sınıf")){
                sinif = row.getCell(0).toString().substring(0, 1);
                yapi = "Zorunlu Ders";
                continue;
            }
            else if(row.getCell(0).toString().toUpperCase().contains("SEÇMELİ DERS")){
                yapi = "Seçmeli Ders";
                continue;
            }

            String dersKodu = formatter.formatCellValue(row.getCell(0));
            String dersAdi  = formatter.formatCellValue(row.getCell(1));
            String hocaAdi  = formatter.formatCellValue(row.getCell(2));

            if (dersKodu == null || dersKodu.isEmpty()) continue;

            dersListesi.add(new EklenecekDersModel(dersKodu, dersAdi, hocaAdi, sinif, yapi));
        }

        workbook.close();
        fis.close();
    }

    private void OgrenciParseExcel(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();

        for (Row row : sheet) {
            if(row.getRowNum() == 0) continue;

            String ogrencinum = formatter.formatCellValue(row.getCell(0));
            String ogrenciad  = formatter.formatCellValue(row.getCell(1));
            String sinif  = formatter.formatCellValue(row.getCell(2));
            String ders = formatter.formatCellValue(row.getCell(3));

            if (ogrencinum == null) continue;

            ogrenciListesi.add(new EklenecekOgrenciModel(ogrencinum, ogrenciad, sinif, ders));
        }

        workbook.close();
        fis.close();
    }

    @FXML
    private void DersKaydetVeritabani() {
        String sql = "INSERT INTO dersler (ders_kodu, ders_adi, hoca_adi, sinif, yapi, bolum) VALUES (?, ?, ?, ?, ?, ?)";
        String delete = "TRUNCATE TABLE dersler RESTART IDENTITY";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             PreparedStatement stmt1 = conn.prepareStatement(delete)) {
            stmt1.executeUpdate();
            for (EklenecekDersModel d : dersListesi) {
                stmt.setString(1, d.getDerskodu());
                stmt.setString(2, d.getDersadi());
                stmt.setString(3, d.getHocaadi());
                stmt.setString(4, d.getSinif());
                stmt.setString(5, d.getYapi());
                stmt.setString(6, LoginController.bolum);
                stmt.addBatch();
            }
            stmt.executeBatch();
            DersYukle();
            //Bildirim Goster ve dosya adini temizle
            CallAlert("İşlem Başarılı!", "Ders Listesi Başarıyla Yüklendi!", "✅ " + dersListesi.size() + " ders başarıyla veritabanına kaydedildi.", Alert.AlertType.INFORMATION);
            derslistesiadi.clear();
            dersListesi.clear();
            derstable1.setItems(null);
            derslistesionayla.setDisable(true);
            derslistesitab.setDisable(false);
            OgrenciVeDersVarmi();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void OgrenciKaydetVeritabani() {
        String sql = "INSERT INTO ogrenciler(ogrenci_numarasi, ogrenci_adi, sinif, ders_kodu, bolum) VALUES (?, ?, ?, ?, ?)";
        String delete = "TRUNCATE TABLE ogrenciler RESTART IDENTITY";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             PreparedStatement stmt1 = conn.prepareStatement(delete)) {
            stmt1.executeUpdate();

            for (EklenecekOgrenciModel o : ogrenciListesi) {
                stmt.setString(1, o.getOgrencikodu());
                stmt.setString(2, o.getOgrenciisim());
                stmt.setString(3, o.getSinif());
                stmt.setString(4, o.getAldigiders());
                stmt.setString(5, LoginController.bolum);
                stmt.addBatch();
            }
            stmt.executeBatch();
            //Bildirim Goster ve dosya adini temizle
            CallAlert("İşlem Başarılı!", "Öğrenci Listesi Başarıyla Yüklendi!", "✅ " + ogrenciListesi.size() + " öğrenci başarıyla veritabanına kaydedildi.", Alert.AlertType.INFORMATION);
            ogrencilistesiadi.clear();
            ogrenciListesi.clear();
            ogrencitable1.setItems(null);
            ogrencilistesionayla.setDisable(true);
            ogrencilistesitab.setDisable(false);
            OgrenciVeDersVarmi();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void OgrenciAra()
    {
        String sql = "SELECT ders_kodu, ogrenci_adi FROM ogrenciler WHERE ogrenci_numarasi = ? AND bolum = ?";
        String sql1 = "SELECT ders_adi FROM dersler WHERE ders_kodu = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             PreparedStatement stmt1 = conn.prepareStatement(sql1)) {
            stmt.setString(1, ogrenciara.getText());
            stmt.setString(2, LoginController.bolum);
            ResultSet rs = stmt.executeQuery();
            DersModel ogrenciders;
            ogrenciDersListesi.clear();
            ogrencininadisoyadi.clear();
            ogrencitable2.setItems(null);
            while (rs.next()) {
                stmt1.setString(1, rs.getString("ders_kodu"));
                ResultSet rs1 = stmt1.executeQuery();
                if (rs1.next()) {
                    ogrenciders = new DersModel(
                            rs.getString("ders_kodu"),
                            rs1.getString("ders_adi")
                    );
                    ogrenciDersListesi.add(ogrenciders);
                    ogrencininadisoyadi.setText(rs.getString("ogrenci_adi"));
                }
            }
            ogrencitable2.setItems(ogrenciDersListesi);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void OgrenciVeDersVarmi()
    {
        String sql = "SELECT ogrenci_adi FROM ogrenciler WHERE bolum = ?";
        String sql1 = "SELECT ders_adi FROM dersler WHERE bolum = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             PreparedStatement stmt1 = conn.prepareStatement(sql1)) {
            stmt.setString(1, LoginController.bolum);
            stmt1.setString(1, LoginController.bolum);
            ResultSet rs = stmt.executeQuery();
            ResultSet rs1 = stmt1.executeQuery();
            if (rs.next() && rs1.next())
            {
                sinavprogramitab.setDisable(false);
                ogrencilistesitab.setDisable(false);
                derslistesitab.setDisable(false);
            }
            else if(rs.next() || rs1.next()){
                if(rs.next())
                {
                    ogrencilistesitab.setDisable(false);
                }
                else{
                    ogrencilistesitab.setDisable(true);
                }
                if(rs1.next())
                {
                    derslistesitab.setDisable(false);
                }
                else{
                    derslistesitab.setDisable(true);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void DersYukle()
    {
        String sql = "SELECT ders_kodu, ders_adi FROM dersler WHERE bolum = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, LoginController.bolum);
            ResultSet rs = stmt.executeQuery();
            dersListesi2.clear();
            while (rs.next()) {
                DersModel ders = new DersModel(
                        rs.getString("ders_kodu"),
                        rs.getString("ders_adi")
                );
                dersListesi2.add(ders);
            }
            derstable2.setItems(dersListesi2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void DersAra()
    {
        String sql = "SELECT ders_kodu, ders_adi FROM dersler WHERE ders_adi ILIKE ? OR ders_kodu ILIKE ? AND bolum = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dersara.getText() + "%");
            stmt.setString(2, dersara.getText() + "%");
            stmt.setString(3, LoginController.bolum);
            ResultSet rs = stmt.executeQuery();
            dersListesi2.clear();
            while (rs.next()) {
                DersModel ders = new DersModel(
                        rs.getString("ders_kodu"),
                        rs.getString("ders_adi")
                );
                dersListesi2.add(ders);
            }
            derstable2.setItems(dersListesi2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void DersiAlanOgrenciler(String dersKodu)
    {
        String sql = "SELECT ogrenci_adi, ogrenci_numarasi FROM ogrenciler WHERE ders_kodu = ? AND bolum = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dersKodu);
            stmt.setString(2, LoginController.bolum);
            ResultSet rs = stmt.executeQuery();
            ogrenciListesi2.clear();
            while (rs.next()) {
                OgrenciModel ogrenci = new OgrenciModel(
                        rs.getString("ogrenci_numarasi"),
                        rs.getString("ogrenci_adi")
                );
                ogrenciListesi2.add(ogrenci);
            }
            dersialanogrencilertable.setItems(ogrenciListesi2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void CikarilacakTabloIcinDersYukle()
    {
        String sql = "SELECT ders_kodu, ders_adi FROM dersler WHERE bolum = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, LoginController.bolum);
            ResultSet rs = stmt.executeQuery();
            cikarilacakdersListesi.clear();
            while (rs.next()) {
                CikarilacakDersModel ders = new CikarilacakDersModel(
                        rs.getString("ders_adi"),
                        rs.getString("ders_kodu"),
                        false
                );
                cikarilacakdersListesi.add(ders);
            }
            cikarilacakderstable.setItems(cikarilacakdersListesi);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void SinavaDahilDersleriYukle() {
        List<CikarilacakDersModel> secilmeyenDersler = cikarilacakderstable.getItems().stream()
                .filter(d -> !d.isSecildi())
                .toList();

        sinavsuresiListesi.clear();
        for (int i = 0; i < secilmeyenDersler.size(); i++) {
            CikarilacakDersModel n = secilmeyenDersler.get(i);
            sinavsuresiListesi.add(new SinavaDahilDerslerModel(n.getDersAdi(), n.getDersKodu(), "75"));
        }
        sinavsuresitable.setItems(sinavsuresiListesi);
    }

    @FXML
    private void GunAl()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy EEEE");
        dahilolmayangunler.getItems().clear();

        LocalDate baslangic = sinavbaslangicdate.getValue();
        LocalDate bitis = sinavbitisdate.getValue();

        if (baslangic == null || bitis == null) {
            return;
        }

        if (bitis.isBefore(baslangic)) {
            return;
        }

        long gunSayisi = ChronoUnit.DAYS.between(baslangic, bitis) + 1;

        for (int i = 0; i < gunSayisi; i++) {
            LocalDate tarih = baslangic.plusDays(i);
            String metin = tarih.format(formatter);
            dahilolmayangunler.getItems().add(metin);
        }
    }

    @FXML
    private void SinavOlustur()
    {
        SinavProgramiOlusturucu create = new SinavProgramiOlusturucu();

        //Secilmeyen Gunleri Al
        ObservableList<String> tumListe = dahilolmayangunler.getItems();
        ObservableList<String> secilenler = dahilolmayangunler.getCheckModel().getCheckedItems();
        List<LocalDate> sinavadahilgunler = new ArrayList<>();
        for (String gun : tumListe) {
            if (!secilenler.contains(gun)) {
                String datePart = gun.split(" ")[0];
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate date = LocalDate.parse(datePart, formatter);
                sinavadahilgunler.add(date);
            }
        }

        String mola;
        String sinav;
        if(beklemesuresi.getText().isEmpty())
        {
            mola = "15";
        }
        else {
            mola = beklemesuresi.getText();
        }

        if(sinavturu.getValue().equals(null))
        {
            sinav = "";
        }
        else
        {
            sinav = sinavturu.getValue().toString().toUpperCase();
        }

        create.Olustur(sinavsuresiListesi, sinavadahilgunler, Integer.parseInt(mola), aynizamandasinav.isSelected(), sinav);
        SinavYukle();
        oturmaduzenitab.setDisable(false);
        sinavbaslangicdate.setValue(null);
        sinavbitisdate.setValue(null);
        beklemesuresi.clear();
        aynizamandasinav.setSelected(false);
        sinavturu.setValue(null);
    }

    private void SinavYukle()
    {
        String sql = "SELECT * FROM sinavlar WHERE bolum = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, LoginController.bolum);
            ResultSet rs = stmt.executeQuery();
            sinavtableListesi.clear();
            while (rs.next()) {
                SinavModel sinav = new SinavModel(
                        rs.getString("tarih"),
                        rs.getString("saat"),
                        rs.getString("ders_kodu"),
                        rs.getString("ders_adi"),
                        rs.getString("derslik_adi")
                );
                sinavtableListesi.add(sinav);
                oturmaduzenitab.setDisable(false);
            }
            sinavtable.setItems(sinavtableListesi);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void DerslikOturmaDuzeniGoster(String derslikAdi)
    {
        String sql = "SELECT enine_sira_sayisi, boyuna_sira_sayisi, sira_yapisi FROM derslikler WHERE bolum = ? AND derslik_adi = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, LoginController.bolum);
            stmt.setString(2, derslikAdi);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                oturmaDuzeniGoster(rs.getInt("enine_sira_sayisi"), rs.getInt("boyuna_sira_sayisi"), rs.getInt("sira_yapisi"), derslikAdi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void SinavDersiAlanOgrenciler(List<OgrenciModel> liste, String dersKodu)
    {
        String sql = "SELECT ogrenci_adi, ogrenci_numarasi FROM ogrenciler WHERE ders_kodu = ? AND bolum = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dersKodu);
            stmt.setString(2, LoginController.bolum);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OgrenciModel ogrenci = new OgrenciModel(
                        rs.getString("ogrenci_numarasi"),
                        rs.getString("ogrenci_adi")
                );
                liste.add(ogrenci);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private GridPane SinavOturmaDuzeniOlustur(int sutun, int satir, int siraYapisi, List<OgrenciModel> liste, String derslikAdi) {
        sutun = sutun * siraYapisi;
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        int blokSayisi = (int) Math.ceil((double) sutun / siraYapisi);
        int boslukGenisligi = 45;

        for (int i = 0; i < satir; i++) {
            int colIndex = 0;
            for (int blok = 0; blok < blokSayisi; blok++) {
                for (int k = 0; k < siraYapisi; k++) {
                    int j = blok * siraYapisi + k;
                    if (j >= sutun) break;

                    Button seat = new Button((i + 1) + "-" + (j + 1));
                    seat.setStyle("-fx-background-color:#5e5e5e; -fx-text-fill:white; -fx-background-radius:5; -fx-font-size:12px;");
                    seat.setWrapText(true);
                    seat.setPrefWidth(110);  // 🔹 Genişletildi
                    seat.setPrefHeight(60);  // 🔹 Yükseltildi

                    if (siraYapisi == 2) {
                        if ((j + 1) % 2 == 1) {
                            seat.setStyle("-fx-background-color:#0d7301; -fx-text-fill:white; -fx-background-radius:5; -fx-font-size:12px;");
                        }
                    } else if (siraYapisi % 2 == 1) {
                        if ((j + 1) % siraYapisi == 1 || (j + 1) % siraYapisi == 0 || (j + 1) % siraYapisi == 3) {
                            seat.setStyle("-fx-background-color:#0d7301; -fx-text-fill:white; -fx-background-radius:5; -fx-font-size:12px;");
                        }
                    } else if (siraYapisi == 4) {
                        if ((j + 1) % siraYapisi == 1 || (j + 1) % siraYapisi == 0) {
                            seat.setStyle("-fx-background-color:#0d7301; -fx-text-fill:white; -fx-background-radius:5; -fx-font-size:12px;");
                        }
                    }

                    grid.add(seat, colIndex++, i);
                }
                if (blok < blokSayisi - 1) {
                    Region bosluk = new Region();
                    bosluk.setPrefWidth(boslukGenisligi);
                    grid.add(bosluk, colIndex++, i);
                }
            }
        }

        Random rand = new Random();

        while (!liste.isEmpty()) {
            List<Button> yesilButonlar = grid.getChildren().stream()
                    .filter(n -> n instanceof Button)
                    .map(n -> (Button) n)
                    .filter(b -> b.getStyle().contains("#0d7301"))
                    .collect(Collectors.toList());

            if (!yesilButonlar.isEmpty()) {
                Button secilen = yesilButonlar.get(rand.nextInt(yesilButonlar.size()));
                String s = secilen.getText();
                String[] parts = s.split("-");
                String oturdugusatir = parts[0];
                String  oturdugusutun = parts[1];
                secilen.setStyle("-fx-background-color:#86a0ff; -fx-text-fill:white; -fx-background-radius:5; -fx-font-size:12px;");
                secilen.setWrapText(true);
                secilen.setText(liste.get(0).getOgrenciadi());
                OturmaDuzeniModel duzen = new OturmaDuzeniModel(
                        liste.get(0).getOgrencino(),
                        liste.get(0).getOgrenciadi(),
                        derslikAdi,
                        oturdugusatir,
                        oturdugusutun
                );
                oturmaDuzeniListesi.add(duzen);
                tamoturmaDuzeniListesi.add(duzen);
                liste.remove(0);
            } else {
                break;
            }
        }

        return grid;
    }


    private void DerslikBilgileriniAl(String derslikAdi, List<OgrenciModel> liste)
    {
        String sql = "SELECT enine_sira_sayisi, boyuna_sira_sayisi, sira_yapisi FROM derslikler WHERE bolum = ? AND derslik_adi = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, LoginController.bolum);
            stmt.setString(2, derslikAdi);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                oturmaDuzeniListesi.clear();
                GridPane grid = SinavOturmaDuzeniOlustur(rs.getInt("enine_sira_sayisi"), rs.getInt("boyuna_sira_sayisi"), rs.getInt("sira_yapisi"), liste, derslikAdi);

                ScrollPane scroll = new ScrollPane(grid);
                scroll.setFitToWidth(true);
                scroll.setFitToHeight(true);

                Stage stage = new Stage();
                stage.setTitle("Oturma Düzeni" + " - " + derslikAdi);
                Scene scene = new Scene(scroll, 1000, 550);
                stage.setScene(scene);
                try {
                    Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/desk.png")));
                    stage.getIcons().add(icon);
                } catch (Exception e) {
                    System.err.println("İkon yüklenemedi: " + e.getMessage());
                }
                stage.show();
                PDFOlusturucu.pdfOlustur(scene, oturmaDuzeniListesi, derslikAdi + "_oturma_duzeni.pdf");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
