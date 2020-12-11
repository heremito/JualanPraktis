package www.starcom.com.jualanpraktis.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListProduk {
    public class ObjectSub{
        @SerializedName("data")
        public List<ListProduk.ObjectSub.Results> produksejenislist;
        @SerializedName("sub1_kategori1")
        public List<ListProduk.ObjectSub.Results> semuaproduk;
     //   @SerializedName("data")
    //    public List<ListProduk.ObjectSub.Results> produkKategori;
        @SerializedName("allproduct")
        public List<ListProduk.ObjectSub.Results> allproduct;
        @SerializedName("produk1")
        public List<ListProduk.ObjectSub.Results> produk1;
        @SerializedName("produk2")
        public List<ListProduk.ObjectSub.Results> produk2;
        @SerializedName("produk3")
        public List<ListProduk.ObjectSub.Results> produk3;
        @SerializedName("produk4")
        public List<ListProduk.ObjectSub.Results> produk4;
        @SerializedName("iklan1")
        public List<ListProduk.ObjectSub.ResultImage> iklan1;
        @SerializedName("iklan2")
        public List<ListProduk.ObjectSub.ResultImage> iklan2;
        @SerializedName("iklan3")
        public List<ListProduk.ObjectSub.ResultImage> iklan3;
        @SerializedName("iklan4")
        public List<ListProduk.ObjectSub.ResultImage> iklan4;

        public  class Results{
            @SerializedName("id_sub_kategori_produk")
            public String id_sub_kategori_produk;


            @SerializedName("id_member")
            public String id_member;

            @SerializedName("kode")
            public String kode;

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


            @SerializedName("start_disc") public  String start_disc;
            @SerializedName("end_disc") public  String end_disc;
        }
        public class ResultImage {
            @SerializedName("img")
            public String img;
            @SerializedName("id_member")
            public String id_member;
        }
    }
}
