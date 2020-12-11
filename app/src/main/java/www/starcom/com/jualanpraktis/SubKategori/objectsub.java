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


        @SerializedName("produk")
        public List<Results> produk;

        public class Results {

            @SerializedName("id_sub_kategori_produk")
            public String id_sub_kategori_produk;
            @SerializedName("id_produk")
            public String id_produk;


            @SerializedName("kode")
            public String kode;



            @SerializedName("id_member")
            public String id_member;

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


            @SerializedName("start_disc") public  String start_disc;
            @SerializedName("end_disc") public  String end_disc;

            @Override
            public String toString() {
                return nama_produk;
            }
        }
    }
}
