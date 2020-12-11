package www.starcom.com.jualanpraktis.Spinner;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ADMIN on 15/02/2018.
 */

public class SObject2 {

    public class Object2 {
        @SerializedName("object2")
        public List<Results> object2;

        public class Results {
            @SerializedName("idKecamatan")
            public String idKecamatan;

            @SerializedName("idKota")
            public String idKota;

            @SerializedName("namaKecamatan")
            public String namaKecamatan;
        }
    }
}
