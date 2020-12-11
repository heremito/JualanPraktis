package www.starcom.com.jualanpraktis.Spinner;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ADMIN on 15/02/2018.
 */

public class SObject3 {

    public class Object3 {
        @SerializedName("object3")
        public List<Results> object3;

        public class Results {
            @SerializedName("id_ongkir")
            public String id_ongkir;

            @SerializedName("id_expedisi")
            public String id_expedisi;


            @SerializedName("nama_expedisi")
            public String nama_expedisi;

            @SerializedName("harga")
            public String harga;
        }
    }
}
