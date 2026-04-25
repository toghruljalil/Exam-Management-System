public class EklenecekDersModel {
    private String derskodu;
    private String dersadi;
    private String hocaadi;
    private String sinif;
    private String yapi;

    public EklenecekDersModel(String derskodu, String dersadi, String hocaadi, String sinif, String yapi) {
        this.derskodu = derskodu;
        this.dersadi = dersadi;
        this.hocaadi = hocaadi;
        this.sinif = sinif;
        this.yapi = yapi;
    }

    public String getDerskodu() { return derskodu; }
    public String getDersadi() { return dersadi; }
    public String getHocaadi() { return hocaadi; }
    public String getSinif() { return sinif; }
    public String getYapi() { return yapi; }
}
