package www.starcom.com.jualanpraktis.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.feature.pembayaran.FormatText;
import www.starcom.com.jualanpraktis.feature.produk.ProdukDetailActivity;
import www.starcom.com.jualanpraktis.model.ListProduk;

/**
 * Created by ADMIN on 06/02/2018.
 */

public class ProdukEndlessScrollAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private List<ListProduk.ObjectSub.Results> results ;
    private List<ListProduk.ObjectSub.Results> dataFilter;
    private String statusList;
    public Context context;

    private static final String RP = "Rp.";

    public ProdukEndlessScrollAdapter(Context context, List<ListProduk.ObjectSub.Results> results, String statusList){
        this.context = context;
        this.results = results ;
        this.dataFilter = results;
        this.statusList = statusList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_sub_kategori, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    @Override
    public int getItemViewType(int position) {
        return results.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {


        public ImageView gambar ;
        public TextView nama_produk,harga_jual,harga_asli,diskon ;
        public CardView cardView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            gambar = itemView.findViewById(R.id.gambar_kategori);
            nama_produk = itemView.findViewById(R.id.nama_produk);
            harga_jual = itemView.findViewById(R.id.harga_jual);
            harga_asli = itemView.findViewById(R.id.harga_asli);
            diskon = itemView.findViewById(R.id.diskon);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder holder, final int position) {
        final String UrlImage = "https://trading.my.id/img/";
        final String Image = results.get(position).gambar;
        final Uri uri = Uri.parse(UrlImage+Image);
        final String harga = results.get(position).harga_asli ;
        final String diskom;
        final int harga_disc;

        if ( results.get(position).diskon.equals("0")||results.get(position).diskon==null||results.get(position).diskon.equals("")){
            diskom = "1";
            harga_disc = Integer.parseInt(harga)*Integer.parseInt(diskom);
            holder.harga_asli.setVisibility(View.GONE);
            holder.diskon.setVisibility(View.GONE);
        }else {
            diskom  = results.get(position).diskon;
            int total_disc_harga = Integer.parseInt(harga)*Integer.parseInt(diskom)/100;
            harga_disc = Integer.parseInt(harga)+total_disc_harga;
        }
        //  final int harga_disc = Integer.parseInt(harga)*Integer.parseInt(diskom)/100;
        NumberFormat nf = new DecimalFormat("#,###");
        final String hrg = nf.format(Integer.parseInt(harga));
        holder.nama_produk.setText(results.get(position).nama_produk);
        //   holder.harga_jual.setText(String.format("%s%s", RP, hrg));
        holder.harga_asli.setText(FormatText.rupiahFormat(harga_disc));
        holder.diskon.setText("("+results.get(position).diskon +"%)");
        holder.harga_asli.setPaintFlags(holder.harga_asli.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.harga_jual.setText(FormatText.rupiahFormat(Double.parseDouble(results.get(position).harga_asli)));
        //  Picasso.get().load(UrlImage+Image).into(holder.gambar);

        Glide.with(context)
                .load(UrlImage+Image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.gambar);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProdukDetailActivity.class);
                intent.putExtra("id_sub_kategori_produk",results.get(position).id_sub_kategori_produk);
                intent.putExtra("id_produk",results.get(position).id_produk);
                intent.putExtra("nama_produk",results.get(position).nama_produk);
                intent.putExtra("harga_asli",Integer.toString(harga_disc));
                intent.putExtra("keterangan_produk", results.get(position).keterangan);
                intent.putExtra("image_o",results.get(position).gambar);
                intent.putExtra("berat",results.get(position).berat);
                intent.putExtra("harga_jual",results.get(position).harga_asli);
                intent.putExtra("stok",results.get(position).stok);
                intent.putExtra("diskon",results.get(position).diskon);
                v.getContext().startActivity(intent);
            }
        });

    }

}
