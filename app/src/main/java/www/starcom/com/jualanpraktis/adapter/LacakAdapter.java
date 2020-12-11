package www.starcom.com.jualanpraktis.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import www.starcom.com.jualanpraktis.R;


public class LacakAdapter extends RecyclerView.Adapter<LacakAdapter.ViewHolder>  {
    private ArrayList<HashMap<String, String>> data;
    private Activity activity;

    public LacakAdapter(Activity activity, ArrayList<HashMap<String, String>> data) {
        this.data=data;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //inflate your layout and pass it to view holder
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_lacak, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        HashMap<String, String> item = new HashMap<>();
        item = this.data.get(i);

        viewHolder.lbl_status.setText(item.get("status"));

        String tanggal = item.get("tanggal")+" "+item.get("jam");
        viewHolder.lbl_tanggal.setText(tanggal);

        final HashMap<String, String> finalItem = item;

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
        private TextView lbl_tanggal,lbl_status,lbl_nominal,lbl_kurir;
        private CardView card_view;

        public ViewHolder(View view) {
            super(view);
            lbl_status=view.findViewById(R.id.lbl_status);
            lbl_tanggal=view.findViewById(R.id.lbl_tanggal);

        }
    }


}

