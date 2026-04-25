import javafx.beans.property.*;

public class SinavaDahilDerslerModel {
    public final StringProperty dersAdi;
    public final StringProperty dersKodu;
    public final StringProperty dersSuresi;

    public SinavaDahilDerslerModel(String dersAdi, String dersKodu, String dersSuresi) {
        this.dersAdi = new SimpleStringProperty(dersAdi);
        this.dersKodu = new SimpleStringProperty(dersKodu);
        this.dersSuresi = new SimpleStringProperty(dersSuresi) {
        };
    }

    public String getDersAdi() { return dersAdi.get(); }
    public StringProperty dersAdiProperty() { return dersAdi; }

    public String getDersKodu() { return dersKodu.get(); }
    public StringProperty dersKoduProperty() { return dersKodu; }

    public String getDersSuresi() { return dersSuresi.get(); }
    public void setDersSuresi(String value) { this.dersSuresi.set(value); }
    public StringProperty dersSuresiProperty() { return dersSuresi; }
}

