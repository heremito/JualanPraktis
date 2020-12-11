package www.starcom.com.jualanpraktis.Spinner;

import java.util.List;

public class Kecamatan {
    private String subdistrict_id;
    private String subdistrict_name;
    private List<Kecamatan> kecamatanList;

    @Override
    public String toString() {
        return subdistrict_name;
    }
    public String getSubdistrict_id() {
        return subdistrict_id;
    }

    public void setSubdistrict_id(String subdistrict_id) {
        this.subdistrict_id = subdistrict_id;
    }

    public String getSubdistrict_name() {
        return subdistrict_name;
    }

    public void setSubdistrict_name(String subdistrict_name) {
        this.subdistrict_name = subdistrict_name;
    }

    public List<Kecamatan> getKecamatanList() {
        return kecamatanList;
    }

    public void setKecamatanList(List<Kecamatan> kecamatanList) {
        this.kecamatanList = kecamatanList;
    }
}
