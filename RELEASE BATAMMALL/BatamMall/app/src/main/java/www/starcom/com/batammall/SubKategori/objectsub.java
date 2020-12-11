package www.starcom.com.jualanpraktis.SubKategori;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ADMIN on 06/02/2018.
 */

public class objectsub {

    public class ObjectSub{
        @SerializedName("sub1_kategori1")
        public List<Results> sub1_kategori1;

        public class Results {

            @SerializedName("id_produk")
            public String id_produk;

            @SerializedName("image_o")
            public String gambar;

            @SerializedName("nama_produk")
            public String nama_produk;

            @SerializedName("harga_jual")
            public String harga_jual;

            @SerializedName("berat")
            public String berat;

            @SerializedName("keterangan_produk")
            public String keterangan;
        }
    }
}
