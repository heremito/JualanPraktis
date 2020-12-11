package www.starcom.com.jualanpraktis.Keranjang;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import www.starcom.com.jualanpraktis.R;

/**
 * Created by ADMIN on 08/02/2018.
 */

public class keranjangHolder extends RecyclerView.ViewHolder {
    public TextView txt_nama,txt_harga;
    public ImageView img_jumlah ;
    public CardView cardView ;
    public ImageView tongSampah ;



    public keranjangHolder(View itemView) {
        super(itemView);

        txt_nama = itemView.findViewById(R.id.card_item_nama);
        txt_harga = itemView.findViewById(R.id.card_item_harga);
        img_jumlah = itemView.findViewById(R.id.card_item_jumlah);
        cardView = itemView.findViewById(R.id.cardview_keranjang);
        tongSampah = itemView.findViewById(R.id.buang);
    }


}

