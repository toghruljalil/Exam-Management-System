public class SinavModel {
    private String tarih;
    private String saat;
    private String derskodu;
    private String dersadi;
    private String derslik;

    public SinavModel(String tarih, String saat, String derskodu, String dersadi, String derslik) {
        this.tarih = tarih;
        this.saat = saat;
        this.derskodu = derskodu;
        this.dersadi = dersadi;
        this.derslik = derslik;
    }

    public String getTarih() { return tarih; }
    public String getSaat() { return saat; }
    public String getDerskodu() { return derskodu; }
    public String getDersadi() { return dersadi; }
    public String getDerslik() { return derslik; }
}
