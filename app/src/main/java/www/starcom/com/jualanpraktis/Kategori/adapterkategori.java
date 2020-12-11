package www.starcom.com.jualanpraktis.Kategori;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.SubKategori.produk;

/**
 * Created by ADMIN on 06/02/2018.
 */

public class adapterkategori extends RecyclerView.Adapter<holderkategori> {

    private List<objectkategori.ObjectKategori.Results> results ;
    public Context context;

    public adapterkategori(Context context,List<objectkategori.ObjectKategori.Results> results){
        this.context = context;
        this.results = results ;
    }
    @Override
    public holderkategori onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_dashboard, null);
        holderkategori holder = new holderkategori(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(holderkategori holder, final int position) {
        final String UrlImage = "https://trading.my.id/";
        final String Image = results.get(position).gambar;
        final Uri uri = Uri.parse(UrlImage+Image);
        holder.judul.setText(results.get(position).judul);
        Glide.with(context).load(uri).into(holder.gambar);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,produk.class);
                intent.putExtra("sub_kategori_produk", results.get(position).judul);
                intent.putExtra("id_sub_kategori_produk", results.get(position).id_sub);
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return results.size();
    }
}
