package www.starcom.com.jualanpraktis.Keranjang;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import www.starcom.com.jualanpraktis.Database.Database;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.SubKategori.order;
import www.starcom.com.jualanpraktis.keranjang;
import www.starcom.com.jualanpraktis.keranjang2;

/**
 * Created by ADMIN on 08/02/2018.
 */

public class keranjangAdapter extends RecyclerView.Adapter<keranjangHolder> {

    private List<order> list = new ArrayList<>();
    private Context context ;
    private keranjang keranjang;
    private keranjang2 keranjang2;

    public keranjangAdapter(List<order> list, Context context,keranjang keranjang, keranjang2 keranjang2) {
        this.list = list;
        this.context = context;
        this.keranjang=keranjang;
        this.keranjang2=keranjang2;
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
            .buildRound(""+list.get(position).getJumlah(), Color.RED);
        holder.img_jumlah.setImageDrawable(drawable);

        NumberFormat nf = new DecimalFormat("#,###");
        int price = (Integer.parseInt(list.get(position).getHarga()))*(Integer.parseInt(list.get(position).getJumlah()));
        holder.txt_harga.setText("Rp "+nf.format(price));
        holder.txt_nama.setText(list.get(position).getNamaProduk());
        holder.tongSampah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(context).cleanChart(list.get(position).getID());
                if (keranjang != null) {
                    keranjang.listItem(0, 0);
                }else {
                    keranjang2.listItem(0, 0);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
