package www.starcom.com.jualanpraktis.Spinner;

import java.util.List;

public class KotaKabupaten {
    private String city_id;
    private String city_name;
    private List<KotaKabupaten> kotaKabupatenList;

    @Override
    public String toString() {
        return city_name;
    }
    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public List<KotaKabupaten> getKotaKabupatenList() {
        return kotaKabupatenList;
    }

    public void setKotaKabupatenList(List<KotaKabupaten> kotaKabupatenList) {
        this.kotaKabupatenList = kotaKabupatenList;
    }
}
