package www.starcom.com.jualanpraktis.Login;

/**
 * Created by ADMIN on 13/02/2018.
 */

public class loginuser {

    private int id ;
    private String nama,email,no_hp,jk;

    public loginuser(int id, String nama, String email, String no_hp, String jk) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.no_hp = no_hp;
        this.jk = jk;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getJk() {
        return jk;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }

}
