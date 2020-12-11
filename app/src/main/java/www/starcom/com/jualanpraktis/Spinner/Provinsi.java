package www.starcom.com.jualanpraktis.Spinner;

import java.util.List;

public class Provinsi {
    private String province_id;
    private String province;
    private List<Provinsi> kecamatanList;

    @Override
    public String toString() {
        return province;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<Provinsi> getKecamatanList() {
        return kecamatanList;
    }

    public void setKecamatanList(List<Provinsi> kecamatanList) {
        this.kecamatanList = kecamatanList;
    }
}
