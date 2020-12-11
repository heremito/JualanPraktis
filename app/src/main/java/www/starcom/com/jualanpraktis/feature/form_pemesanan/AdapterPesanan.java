package www.starcom.com.jualanpraktis.feature.form_pemesanan;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import www.starcom.com.jualanpraktis.Keranjang.keranjangHolder;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.feature.pembayaran.FormatText;

/**
 * Created by ADMIN on 08/02/2018.
 */

public class AdapterPesanan extends RecyclerView.Adapter<keranjangHolder> {

    private List<HashMap<String,String>> list = new ArrayList<>();
    private Context context ;

    public AdapterPesanan(List<HashMap<String,String>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public keranjangHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_keranjang, null);
        keranjangHolder holder = new keranjangHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(keranjangHolder holder, final int position) {
        TextDrawable drawable = TextDrawable.builder()
            .buildRound(""+list.get(position).get("quantity"), Color.RED);
        holder.img_jumlah.setImageDrawable(drawable);

       // NumberFormat nf = new DecimalFormat("#,###");
        int price = (Integer.parseInt(list.get(position).get("price")))*(Integer.parseInt(list.get(position).get("quantity")));
        holder.txt_harga.setText(FormatText.rupiahFormat(price));
        holder.txt_nama.setText(list.get(position).get("product"));
        holder.tongSampah.setVisibility(View.GONE);
        holder.tongSampah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
