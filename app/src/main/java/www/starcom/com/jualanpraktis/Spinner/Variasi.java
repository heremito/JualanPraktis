package www.starcom.com.jualanpraktis.Spinner;

import java.util.List;

public class Variasi {
    private String id;
    private String variasi;
    private String stok;
    private List<Variasi> variasiList;

    @Override
    public String toString() {
        return variasi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVariasi() {
        return variasi;
    }

    public void setVariasi(String variasi) {
        this.variasi = variasi;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public List<Variasi> getVariasiList() {
        return variasiList;
    }

    public void setVariasiList(List<Variasi> variasiList) {
        this.variasiList = variasiList;
    }
}
