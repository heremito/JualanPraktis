package www.starcom.com.jualanpraktis.Spinner;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ADMIN on 15/02/2018.
 */

public class SObject1 {

    public class Object1 {
        @SerializedName("object1")
        public List<Results> object1;

        public class Results {
            @SerializedName("idKota")
            public String idKota;

            @SerializedName("namaKota")
            public String namaKota;
        }
    }
}
