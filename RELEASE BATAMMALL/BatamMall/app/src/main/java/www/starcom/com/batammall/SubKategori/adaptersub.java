package www.starcom.com.jualanpraktis.SubKategori;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import www.starcom.com.jualanpraktis.R;

/**
 * Created by ADMIN on 06/02/2018.
 */

public class adaptersub extends RecyclerView.Adapter<holdersub> {

    private List<objectsub.ObjectSub.Results> results ;
    public Context context;

    private static final String RP = "Rp.";

    public adaptersub(Context context,List<objectsub.ObjectSub.Results> results){
        this.context = context;
        this.results = results ;
    }

    @Override
    public holdersub onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_sub_kategori, null);
        holdersub holder = new holdersub(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(holdersub holder, final int position) {
        final String UrlImage = "https://batammall.co.id/img/";
        final String Image = results.get(position).gambar;
        final Uri uri = Uri.parse(UrlImage+Image);
        final String harga = results.get(position).harga_jual ;
        NumberFormat nf = new DecimalFormat("#,###");
        final String hrg = nf.format(Integer.parseInt(harga));
        holder.nama_produk.setText(results.get(position).nama_produk);
        holder.harga_jual.setText(String.format("%s%s", RP, hrg));
        Glide.with(context).load(uri).into(holder.gambar);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,produk_detail.class);
                intent.putExtra("id_produk",results.get(position).id_produk);
                intent.putExtra("nama_produk",results.get(position).nama_produk);
                intent.putExtra("harga_jual",results.get(position).harga_jual);
                intent.putExtra("keterangan_produk", util.stripHtml(results.get(position).keterangan));
                intent.putExtra("image_o",results.get(position).gambar);
                intent.putExtra("berat",results.get(position).berat);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }
}
