package www.starcom.com.jualanpraktis.SubKategori;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import www.starcom.com.jualanpraktis.R;

/**
 * Created by ADMIN on 06/02/2018.
 */

public class holdersub extends RecyclerView.ViewHolder {

    public ImageView gambar ;
    public TextView nama_produk,harga_jual,harga_asli,diskon ;
    public CardView cardView;
   // public SimpleDraweeView draweeView;

    public holdersub(View itemView) {
        super(itemView);
        gambar = itemView.findViewById(R.id.gambar_kategori);
        nama_produk = itemView.findViewById(R.id.nama_produk);
        harga_jual = itemView.findViewById(R.id.harga_jual);
        harga_asli = itemView.findViewById(R.id.harga_asli);
        diskon = itemView.findViewById(R.id.diskon);
        cardView = itemView.findViewById(R.id.cardview);
    }
}
