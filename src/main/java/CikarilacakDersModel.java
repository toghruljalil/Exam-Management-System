import javafx.beans.property.*;

public class CikarilacakDersModel{
    private final StringProperty dersAdi;
    private final StringProperty dersKodu;
    private final BooleanProperty secildi;

    public CikarilacakDersModel(String dersAdi, String dersKodu, boolean secildi) {
        this.dersAdi = new SimpleStringProperty(dersAdi);
        this.dersKodu = new SimpleStringProperty(dersKodu);
        this.secildi = new SimpleBooleanProperty(secildi);
    }

    public String getDersAdi() { return dersAdi.get(); }
    public void setDersAdi(String value) { dersAdi.set(value); }
    public StringProperty dersAdiProperty() { return dersAdi; }

    public String getDersKodu() { return dersKodu.get(); }
    public void setDersKodu(String value) { dersKodu.set(value); }
    public StringProperty dersKoduProperty() { return dersKodu; }

    public boolean isSecildi() { return secildi.get(); }
    public void setSecildi(boolean value) { secildi.set(value); }
    public BooleanProperty secildiProperty() { return secildi; }
}

