package www.starcom.com.jualanpraktis.SubKategori;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import www.starcom.com.jualanpraktis.R;

/**
 * Created by ADMIN on 06/02/2018.
 */

public class holdersub extends RecyclerView.ViewHolder {

    public ImageView gambar ;
    public TextView nama_produk,harga_jual ;
    public CardView cardView;

    public holdersub(View itemView) {
        super(itemView);
        gambar = itemView.findViewById(R.id.gambar_kategori);
        nama_produk = itemView.findViewById(R.id.nama_produk);
        harga_jual = itemView.findViewById(R.id.harga_jual);
        cardView = itemView.findViewById(R.id.cardview);
    }
}
