public class EklenecekOgrenciModel {
    private String ogrencikodu;
    private String ogrenciisim;
    private String sinif;
    private String aldigiders;

    public EklenecekOgrenciModel(String ogrencikodu, String ogrenciisim, String sinif, String aldigiders) {
        this.ogrencikodu = ogrencikodu;
        this.ogrenciisim = ogrenciisim;
        this.sinif = sinif;
        this.aldigiders = aldigiders;
    }

    public String getOgrencikodu() { return ogrencikodu; }
    public String getOgrenciisim() { return ogrenciisim; }
    public String getSinif() { return sinif; }
    public String getAldigiders() { return aldigiders; }
}
