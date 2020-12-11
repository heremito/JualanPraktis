package www.starcom.com.jualanpraktis.Kategori;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import www.starcom.com.jualanpraktis.R;

/**
 * Created by ADMIN on 06/02/2018.
 */

public class holderkategori extends RecyclerView.ViewHolder {

    public ImageView gambar ;
    public TextView judul ;
    public CardView cardView;

    public holderkategori(View itemView) {
        super(itemView);
        gambar = itemView.findViewById(R.id.gambar_kategori);
        judul = itemView.findViewById(R.id.nama_kategori);
        cardView = itemView.findViewById(R.id.cardview);
    }
}
