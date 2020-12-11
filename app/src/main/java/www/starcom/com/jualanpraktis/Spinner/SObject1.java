package www.starcom.com.jualanpraktis.Spinner;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ADMIN on 15/02/2018.
 */

public class SObject1 {

    public class Object1 {
        @SerializedName("results")
        public List<Results> object1;

        public class Results {
            @SerializedName("city_id")
            public String idKota;

            @SerializedName("city_name")
            public String namaKota;

            public String getIdKota() {
                return idKota;
            }

            public void setIdKota(String idKota) {
                this.idKota = idKota;
            }

            public String getNamaKota() {
                return namaKota;
            }

            public void setNamaKota(String namaKota) {
                this.namaKota = namaKota;
            }
        }

        public List<Results> getObject1() {
            return object1;
        }

        public void setObject1(List<Results> object1) {
            this.object1 = object1;
        }
    }

}
