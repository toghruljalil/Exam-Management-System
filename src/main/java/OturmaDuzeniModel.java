public class OturmaDuzeniModel {
    private String ogrencino;
    private String ogrenciadi;
    private String derslik;
    private String satir;
    private String sutun;

    public OturmaDuzeniModel(String ogrencino, String ogrenciadi, String derslik, String satir, String sutun) {
        this.ogrencino = ogrencino;
        this.ogrenciadi = ogrenciadi;
        this.derslik = derslik;
        this.satir = satir;
        this.sutun = sutun;
    }

    public String getOgrencino() { return ogrencino; }
    public String getOgrenciadi() { return ogrenciadi; }
    public String getDerslik() { return derslik; }
    public String getSatir() { return satir; }
    public String getSutun() { return sutun; }
}
