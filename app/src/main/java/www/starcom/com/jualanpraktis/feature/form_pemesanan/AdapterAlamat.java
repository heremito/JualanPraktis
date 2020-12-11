package www.starcom.com.jualanpraktis.feature.form_pemesanan;

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

public class AdapterAlamat extends RecyclerView.Adapter<AdapterAlamat.ViewHolder> {
    private ArrayList<HashMap<String, String>> data;
    private Activity activity;
    private String key;

    public AdapterAlamat(Activity activity, ArrayList<HashMap<String, String>> data, String key) {
        this.data = data;
        this.activity = activity;
        this.key = key;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //inflate your layout and pass it to view holder
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_alamat, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        HashMap<String, String> item = new HashMap<>();
        item = this.data.get(i);

        // viewHolder.lbl_server.setText(item.get("perangkat"));
        viewHolder.lbl_nama.setText(item.get("nama"));


        final HashMap<String, String> finalItem = item;
       viewHolder.container.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent();
               intent.putExtra("id",finalItem.get("id"));
               intent.putExtra("nama", finalItem.get("nama"));
               activity.setResult(activity.RESULT_OK, intent);
               activity.finish();
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
        private TextView lbl_nama;
        private CardView container;
//        private ImageView img_dokumen;

        public ViewHolder(View view) {
            super(view);
            lbl_nama = view.findViewById(R.id.lbl_nama);
            container = view.findViewById(R.id.card_view);
        }
    }
}
