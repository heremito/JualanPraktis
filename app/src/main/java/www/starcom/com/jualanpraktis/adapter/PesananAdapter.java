package www.starcom.com.jualanpraktis.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import www.starcom.com.jualanpraktis.Login.Pref;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.feature.form_pemesanan.ResultTransferActivity;
import www.starcom.com.jualanpraktis.feature.pembayaran.FormatText;
import www.starcom.com.jualanpraktis.feature.pesanan_saya.DetailPesananActivity;


public class PesananAdapter extends RecyclerView.Adapter<PesananAdapter.ViewHolder>  {
    private ArrayList<HashMap<String, String>> data;
    private Activity activity;
    private Pref pref;

    public PesananAdapter(Activity activity, ArrayList<HashMap<String, String>> data) {
        this.data=data;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //inflate your layout and pass it to view holder
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_pesanan, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        HashMap<String, String> item = new HashMap<>();
        item = this.data.get(i);
        pref = new Pref(activity.getApplicationContext());

       // viewHolder.lbl_server.setText(item.get("perangkat"));
        viewHolder.lbl_nama.setText(item.get("id_pesanan"));
        viewHolder.lbl_nominal.setText(FormatText.rupiahFormat(Double.parseDouble(item.get("jumlah"))));

        String status_label = "";
        if (!pref.getLoginMethod().equals("coorperate")){

            if (item.get("opsi_bayar").equals("transfer")){

                if (item.get("status_verifikasi").equals("null")){
                    status_label = "Pending";
                    viewHolder.lbl_status.setBackground(activity.getResources().getDrawable(R.drawable.buttonstylegrey));
                }else {
                    if (item.get("no_resi").equals("")||item.get("no_resi").equals("null")){
                        status_label = "Diproses";
                        viewHolder.lbl_status.setBackground(activity.getResources().getDrawable(R.drawable.buttonstyleblue));
                    }else if (item.get("no_resi")!=null){
                        status_label = "No Resi : "+item.get("no_resi");
                        viewHolder.lbl_status.setBackground(activity.getResources().getDrawable(R.drawable.buttonstylegreen));
                    } else {

                        status_label = "Diproses";
                        viewHolder.lbl_status.setBackground(activity.getResources().getDrawable(R.drawable.buttonstyleblue));
                    }
                }
            }else {
                    if (item.get("status_kirim").equals("")||item.get("status_kirim").equals("null")){
                        status_label = "Pending";
                        viewHolder.lbl_status.setBackground(activity.getResources().getDrawable(R.drawable.buttonstylegrey));
                    }else if (item.get("status_kirim").equals("0")){

                        status_label = "Dalam Pengantaran";
                        viewHolder.lbl_status.setBackground(activity.getResources().getDrawable(R.drawable.buttonstyleblue));
                    }else if (item.get("status_kirim").equals("1")){

                        status_label = "Sudah diterima";
                        viewHolder.lbl_status.setBackground(activity.getResources().getDrawable(R.drawable.buttonstylegreen));
                    }

            }

        }else {
            if (item.get("opsi_bayar").equals("transfer")){
                if (item.get("status_verifikasi").equals("null")){
                    status_label = "Pending";
                    viewHolder.lbl_status.setBackground(activity.getResources().getDrawable(R.drawable.buttonstylegrey));
                }else {
                    if (item.get("no_resi").equals("")||item.get("no_resi").equals("null")){
                        status_label = "Diproses";
                        viewHolder.lbl_status.setBackground(activity.getResources().getDrawable(R.drawable.buttonstyleblue));
                    }else if (item.get("no_resi")!=null){
                        status_label = "No Resi : "+item.get("no_resi");
                        viewHolder.lbl_status.setBackground(activity.getResources().getDrawable(R.drawable.buttonstylegreen));
                    } else {

                        status_label = "Diproses";
                        viewHolder.lbl_status.setBackground(activity.getResources().getDrawable(R.drawable.buttonstyleblue));
                    }
                }
            }else {
                if (item.get("status_kirim").equals("")||item.get("status_kirim").equals("null")){
                    status_label = "Pending";
                    viewHolder.lbl_status.setBackground(activity.getResources().getDrawable(R.drawable.buttonstylegrey));
                }else if (item.get("status_kirim").equals("0")){

                    status_label = "Dalam Pengantaran";
                    viewHolder.lbl_status.setBackground(activity.getResources().getDrawable(R.drawable.buttonstyleblue));
                }else if (item.get("status_kirim").equals("1")){

                    status_label = "Sudah diterima";
                    viewHolder.lbl_status.setBackground(activity.getResources().getDrawable(R.drawable.buttonstylegreen));
                }
            }

        }
        viewHolder.lbl_status.setText(status_label);

        String tanggal = item.get("tanggal");
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",new Locale("id","ID"));
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd MMM yyyy HH:mm",new Locale("id","ID"));
        try {
            Date date = fmt.parse(tanggal);
            String tanggalFormat = fmtOut.format(date);
            viewHolder.lbl_tanggal.setText(tanggalFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        final HashMap<String, String> finalItem = item;
        viewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,DetailPesananActivity.class);
                intent.putExtra("id_pesanan",finalItem.get("id_pesanan"));
                activity.startActivity(intent);
            }
        });
        viewHolder.btn_lihat_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,DetailPesananActivity.class);
                intent.putExtra("id_pesanan",finalItem.get("id_pesanan"));
                activity.startActivity(intent);
            }
        });

        if (item.get("opsi_bayar").equals("transfer manual")){
            if (item.get("status_konfirmasi").equals("1")){
                viewHolder.btn_konfirmasi.setVisibility(View.GONE);
            }else {
                viewHolder.btn_konfirmasi.setVisibility(View.VISIBLE);
            }

        }else {
            viewHolder.btn_konfirmasi.setVisibility(View.GONE);
        }

        viewHolder.btn_konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, ResultTransferActivity.class)
                        .putExtra("id_transaksi",finalItem.get("id_pesanan"))
                        .putExtra("status","konfirmasi")
                        .putExtra("total",finalItem.get("jumlah")));
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
        private TextView lbl_nama,lbl_status,lbl_nominal,lbl_tanggal;
        private CardView card_view,btn_lihat_detail,btn_konfirmasi;

        public ViewHolder(View view) {
            super(view);
            lbl_nama =  view.findViewById(R.id.lbl_nama);
            lbl_tanggal = view.findViewById(R.id.lbl_tanggal);
            lbl_status=view.findViewById(R.id.lbl_status);
            lbl_nominal=view.findViewById(R.id.lbl_nominal);
            btn_lihat_detail = view.findViewById(R.id.btn_lihat_detail);
            btn_konfirmasi = view.findViewById(R.id.btn_konfirmasi);

            card_view = view.findViewById(R.id.card_view);
        }
    }


}

