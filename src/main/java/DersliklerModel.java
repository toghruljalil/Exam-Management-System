public class DersliklerModel {
    private String bolumadi;
    private int derslikkodu;
    private String derslikadi;
    private int kapasite;
    private int eninesira;
    private int boyunasira;
    private int sirayapisi;

    public DersliklerModel(String bolumadi, int derslikkodu, String derslikadi, int kapasite, int eninesira, int boyunasira, int sirayapisi) {
        this.bolumadi = bolumadi;
        this.derslikkodu = derslikkodu;
        this.derslikadi = derslikadi;
        this.kapasite = kapasite;
        this.eninesira = eninesira;
        this.boyunasira = boyunasira;
        this.sirayapisi = sirayapisi;
    }

    public String getBolumadi() { return bolumadi; }
    public int getDerslikkodu() { return derslikkodu; }
    public String getDerslikadi() { return derslikadi; }
    public int getKapasite() { return kapasite; }
    public int getEninesira() { return eninesira; }
    public int getBoyunasira() { return boyunasira; }
    public int getSirayapisi() { return sirayapisi; }
}
