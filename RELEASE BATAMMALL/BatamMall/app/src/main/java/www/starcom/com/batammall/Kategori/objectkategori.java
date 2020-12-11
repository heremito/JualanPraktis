package www.starcom.com.jualanpraktis.Kategori;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ADMIN on 06/02/2018.
 */

public class objectkategori {
    public class ObjectKategori{
        @SerializedName("kategori")
        public List<Results> kategori;

        public class Results {
            @SerializedName("image")
            public String gambar;

            @SerializedName("sub_kategori_produk")
            public String judul;

            @SerializedName("id_sub_kategori_produk")
            public String id_sub;
        }
    }
}
