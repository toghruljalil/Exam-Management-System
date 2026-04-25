import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("Sınav Yönetim Sistemi");
        stage.setScene(scene);
        try {
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png")));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("İkon yüklenemedi: " + e.getMessage());
        }
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
