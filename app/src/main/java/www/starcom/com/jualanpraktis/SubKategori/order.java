package www.starcom.com.jualanpraktis.SubKategori;

/**
 * Created by ADMIN on 08/02/2018.
 */

public class order {

    private String ID ;
    private String IdProduk ;
    private String NamaProduk ;
    private String jumlah ;
    private String harga ;
    private String berat ;

    public order(){

    }

    public order(String ID,String idProduk, String namaProduk, String jumlah, String harga,String berat) {
        this.ID = ID ;
        IdProduk = idProduk;
        NamaProduk = namaProduk;
        this.jumlah = jumlah;
        this.harga = harga;
        this.berat = berat;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIdProduk() {
        return IdProduk;
    }

    public void setIdProduk(String idProduk) {
        IdProduk = idProduk;
    }

    public String getNamaProduk() {
        return NamaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        NamaProduk = namaProduk;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getBerat() {
        return berat;
    }

    public void setBerat(String berat) {
        this.berat = berat;
    }
}
