package www.starcom.com.jualanpraktis.feature.pembayaran;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import www.starcom.com.jualanpraktis.R;


public class PembayaranAdapter extends RecyclerView.Adapter<PembayaranAdapter.ViewHolder>  {
    private ArrayList<HashMap<String, String>> data;
    private Activity activity;

    public PembayaranAdapter(Activity activity, ArrayList<HashMap<String, String>> data) {
        this.data=data;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //inflate your layout and pass it to view holder
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_pembayaran, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        HashMap<String, String> item = new HashMap<>();
        item = this.data.get(i);

       // viewHolder.lbl_server.setText(item.get("perangkat"));
        viewHolder.lbl_nama.setText(item.get("tipe"));
        viewHolder.lbl_nominal.setText(FormatText.rupiahFormat(Double.parseDouble(item.get("jumlah"))));
        viewHolder.lbl_status.setText(item.get("status"));

        String tanggal = item.get("tanggal");
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat fmtOut = new SimpleDateFormat("EE, dd MMM yyyy");
        try {
            Date date = fmt.parse(tanggal);
            String tanggalFormat = fmtOut.format(date);
            viewHolder.lbl_tanggal.setText(tanggalFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (item.get("kode_status").equals("0")){
            viewHolder.lbl_status.setBackground(activity.getResources().getDrawable(R.drawable.buttonstylegrey));
        }else if (item.get("kode_status").equals("1")){
            viewHolder.lbl_status.setBackground(activity.getResources().getDrawable(R.drawable.buttonstylegreen));
        }else if (item.get("kode_status").equals("2")){
            viewHolder.lbl_status.setBackground(activity.getResources().getDrawable(R.drawable.buttonstylered));
        }



        final HashMap<String, String> finalItem = item;
        /**
        viewHolder.btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"Berhasil download file",Toast.LENGTH_LONG).show();
            }
        }); */

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
//        private ImageView img_dokumen;

        public ViewHolder(View view) {
            super(view);
            lbl_nama =  view.findViewById(R.id.lbl_nama);
            lbl_tanggal = view.findViewById(R.id.lbl_tanggal);
            lbl_status=view.findViewById(R.id.lbl_status);
            lbl_nominal=view.findViewById(R.id.lbl_nominal);
        }
    }


}

