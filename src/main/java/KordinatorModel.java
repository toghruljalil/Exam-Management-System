public class KordinatorModel {
    private String bolumadi;
    private String eposta;
    private String adsoyad;

    public KordinatorModel(String bolumadi, String eposta, String adsoyad) {
        this.bolumadi = bolumadi;
        this.eposta = eposta;
        this.adsoyad = adsoyad;
    }

    public String getBolumadi() { return bolumadi; }
    public String getEposta() { return eposta; }
    public String getAdsoyad() { return adsoyad; }
}
