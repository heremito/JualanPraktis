package www.starcom.com.jualanpraktis.Login;

/**
 * Created by ADMIN on 13/02/2018.
 */

public class loginuser {

    private String id ;
    private String nama,email,no_hp, nama_toko;
    private String no_ktp,no_npwp;
    private String provinsi,kecamatan,kota,alamat;
    //private String idProvinsi,idKota,idKecamatan;
    //private String namaProvinsi,namaKota,namaKecamatan;
    private String nama_bank,atas_nama,no_rek;
    private String foto;
    private String kode;


    public loginuser(String id, String kode ,String nama,String nama_toko,
                     String provinsi, String kota, String kecamatan,String alamat,
                     String no_ktp, String no_npwp, String no_hp,
                     String email,
                     String atas_nama, String no_rek, String nama_bank, String foto
//        String idProvinsi, String idKota,String idKecamatan,
//        String namaProvinsi, String namaKota, String namaKecamatan
    ) {
        this.id = id;
        this.kode = kode;
        this.nama = nama;
        this.nama_toko = nama_toko;
        this.provinsi = provinsi;
        this.kota = kota;
        this.kecamatan = kecamatan;
        this.alamat = alamat;
        this.no_ktp = no_ktp;
        this.no_npwp = no_npwp;
        this.no_hp = no_hp;
        this.email = email;
        //this.jk = jk;
        this.atas_nama = atas_nama;
        this.no_rek = no_rek;
        this.nama_bank = nama_bank;
        this.foto = foto;

//        this.idProvinsi = idProvinsi;
//        this.idKota = idKota;
//        this.idKecamatan = idKecamatan;
//
//        this.namaProvinsi = namaProvinsi;
//        this.namaKota = namaKota;
//        this.namaKecamatan = namaKecamatan;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama_toko() {
        return nama_toko;
    }

    public void setNama_toko(String nama_toko) {
        this.nama_toko = nama_toko;
    }

    public String getNo_rek() {
        return no_rek;
    }

    public void setNo_rek(String no_rek) {
        this.no_rek = no_rek;
    }

    public String getAtas_nama() {
        return atas_nama;
    }

    public void setAtas_nama(String atas_nama) {
        this.atas_nama = atas_nama;
    }

    public void setNama_bank(String nama_bank) {
        this.nama_bank = nama_bank;
    }

    public String getNama_bank() {
        return nama_bank;
    }

    public String getNo_npwp() {
        return no_npwp;
    }

    public void setNo_npwp(String no_npwp) {
        this.no_npwp = no_npwp;
    }

    public String getNo_ktp() {
        return no_ktp;
    }

    public void setNo_ktp(String no_ktp) {
        this.no_ktp = no_ktp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

//    public String getJk() {
//        return jk;
//    }
//
//    public void setJk(String jk) {
//        this.jk = jk;
//    }
//
//    public String getIdProvinsi() {
//        return idProvinsi;
//    }
//
//    public void setIdProvinsi(String idProvinsi) {
//        this.idProvinsi = idProvinsi;
//    }
//
//    public String getIdKota() {
//        return idKota;
//    }
//
//    public void setIdKota(String idKota) {
//        this.idKota = idKota;
//    }
//
//    public String getIdKecamatan() {
//        return idKecamatan;
//    }
//
//    public void setIdKecamatan(String idKecamatan) {
//        this.idKecamatan = idKecamatan;
//    }
//
//    public String getNamaProvinsi() {
//        return namaProvinsi;
//    }
//
//    public void setNamaProvinsi(String namaProvinsi) {
//        this.namaProvinsi = namaProvinsi;
//    }
//
//    public String getNamaKota() {
//        return namaKota;
//    }
//
//    public void setNamaKota(String namaKota) {
//        this.namaKota = namaKota;
//    }
//
//    public String getNamaKecamatan() {
//        return namaKecamatan;
//    }
//
//    public void setNamaKecamatan(String namaKecamatan) {
//        this.namaKecamatan = namaKecamatan;
//    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
