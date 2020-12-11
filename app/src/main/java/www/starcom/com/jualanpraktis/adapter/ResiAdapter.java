package www.starcom.com.jualanpraktis.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.feature.pembayaran.FormatText;
import www.starcom.com.jualanpraktis.feature.resi_pengiriman.DetailResiPengirimanActivity;


public class ResiAdapter extends RecyclerView.Adapter<ResiAdapter.ViewHolder>  {
    private ArrayList<HashMap<String, String>> data;
    private Activity activity;

    public ResiAdapter(Activity activity, ArrayList<HashMap<String, String>> data) {
        this.data=data;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //inflate your layout and pass it to view holder
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_resi, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        HashMap<String, String> item = new HashMap<>();
        item = this.data.get(i);

       // viewHolder.lbl_server.setText(item.get("perangkat"));
        viewHolder.lbl_nama.setText(item.get("id_pesanan"));
        viewHolder.lbl_nominal.setText(FormatText.rupiahFormat(Double.parseDouble(item.get("jumlah"))));

        viewHolder.lbl_status.setText(item.get("no_resi"));

        viewHolder.lbl_kurir.setText(item.get("kurir"));


        final HashMap<String, String> finalItem = item;
        viewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailResiPengirimanActivity.class);
                intent.putExtra("no_resi",finalItem.get("no_resi"));
                intent.putExtra("kode_kurir",finalItem.get("kurir"));
                activity.startActivity(intent);
            }
        });
        viewHolder.btn_lihat_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailResiPengirimanActivity.class);
                intent.putExtra("no_resi",finalItem.get("no_resi"));
                intent.putExtra("kode_kurir",finalItem.get("kurir"));
                activity.startActivity(intent);
            }
        });

    }

    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }


    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }


    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView lbl_nama,lbl_status,lbl_nominal,lbl_kurir;
        private CardView card_view,btn_lihat_detail;

        public ViewHolder(View view) {
            super(view);
            lbl_nama =  view.findViewById(R.id.lbl_nama);
            lbl_kurir = view.findViewById(R.id.lbl_kurir);
            lbl_status=view.findViewById(R.id.lbl_nomor_resi);
            lbl_nominal=view.findViewById(R.id.lbl_nominal);
            btn_lihat_detail = view.findViewById(R.id.btn_lihat_detail);

            card_view = view.findViewById(R.id.card_view);
        }
    }


}

