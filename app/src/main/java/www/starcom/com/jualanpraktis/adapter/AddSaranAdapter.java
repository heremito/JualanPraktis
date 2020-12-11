package www.starcom.com.jualanpraktis.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import www.starcom.com.jualanpraktis.R;


public class AddSaranAdapter extends RecyclerView.Adapter<AddSaranAdapter.ViewHolder>  {
    private ArrayList<HashMap<String, String>> data;
    private Activity activity;


    public AddSaranAdapter(Activity activity, ArrayList<HashMap<String, String>> data) {
        this.data=data;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //inflate your layout and pass it to view holder
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_add_saran, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        HashMap<String, String> item = new HashMap<>();
        item = this.data.get(i);

        viewHolder.lbl_nama.setText(item.get("nama"));
        viewHolder.lbl_jenis.setText(item.get("jenis"));
        viewHolder.lbl_keterangan.setText(item.get("keterangan"));

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
        private TextView lbl_nama,lbl_jenis,lbl_keterangan,lbl_tanggal;

        public ViewHolder(View view) {
            super(view);
            lbl_nama =  view.findViewById(R.id.lbl_nama);
            lbl_jenis=view.findViewById(R.id.lbl_jenis);
            lbl_keterangan = view.findViewById(R.id.lbl_keterangan);
        }
    }




}

