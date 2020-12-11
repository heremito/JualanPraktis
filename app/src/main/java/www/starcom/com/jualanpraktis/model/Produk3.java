package www.starcom.com.jualanpraktis.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import www.starcom.com.jualanpraktis.SubKategori.objectsub;

public class Produk3 {
    public class ObjectSub{
        @SerializedName("produk3")
        public List<objectsub.ObjectSub.Results> sub1_kategori1;

        public class Results {

            @SerializedName("id_produk")
            public String id_produk;

            @SerializedName("image_o")
            public String gambar;

            @SerializedName("nama_produk")
            public String nama_produk;

            @SerializedName("harga")
            public String harga_asli;

            @SerializedName("stok")
            public String stok;

            @SerializedName("harga_jual")
            public String harga_jual;

            @SerializedName("berat")
            public String berat;

            @SerializedName("disc")
            public String diskon;

            @SerializedName("keterangan_produk")
            public String keterangan;
        }
    }
}
