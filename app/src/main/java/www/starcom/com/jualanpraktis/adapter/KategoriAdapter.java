package www.starcom.com.jualanpraktis.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.feature.produk.ListProdukActivity;


public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.ViewHolder>  {
    private ArrayList<HashMap<String, String>> data;
    private Activity activity;

    public KategoriAdapter(Activity activity, ArrayList<HashMap<String, String>> data) {
        this.data=data;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //inflate your layout and pass it to view holder
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_kategori, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        HashMap<String, String> item = new HashMap<>();
        item = this.data.get(i);

        viewHolder.title.setText(item.get("kategori"));

        String img_url = "https://trading.my.id/img2/"+item.get("gambar");
        Uri uri  = Uri.parse(img_url);

        Glide.with(activity.getApplicationContext())
                .load(img_url)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false))
                .into(viewHolder.image);


        final Calendar cal = Calendar.getInstance();
        final Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, -1);
        final Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DATE, -2);

        /**  String tanggalupload = "";
        String tanggal = item.get("created_at");
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = fmt.parse(tanggal);
             tanggalupload = fmtOut.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String tanggalhariini = DateFormat.format("dd-MM-yyyy",cal).toString();
        String tanggalhariminus2 = DateFormat.format("dd-MM-yyyy",cal1).toString();
        String tanggalhariminus3 = DateFormat.format("dd-MM-yyyy",cal2).toString();

        if (tanggalhariini.equals(tanggalupload)){
            viewHolder.new_indicatior.setVisibility(View.VISIBLE);
        }else if (tanggalhariminus2.equals(tanggalupload)){
            viewHolder.new_indicatior.setVisibility(View.VISIBLE);
        }/*else if (tanggalhariminus3.equals(tanggalupload)){
            viewHolder.new_indicatior.setVisibility(View.VISIBLE);
        } else{
            viewHolder.new_indicatior.setVisibility(View.GONE);
        } **/

        if (item.get("jumlah").equals("0")){

            viewHolder.new_indicatior.setVisibility(View.VISIBLE);
        }else {
            viewHolder.new_indicatior.setVisibility(View.GONE);
        }
        final HashMap<String, String> finalItem = item;

        viewHolder.ll_kategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalItem.get("jumlah").equals("0")){
                    new AlertDialog.Builder(activity)
                            .setTitle("Segera Hadir")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            })
                            .setIcon(R.drawable.ic_info_black_24dp)
                            .show();
                }else {
                    Intent intent = new Intent(activity, ListProdukActivity.class);
                    intent.putExtra("status","produkKategori");
                    intent.putExtra("id",finalItem.get("id"));
                    intent.putExtra("kategori",finalItem.get("kategori"));
                    activity.startActivity(intent);
                }


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
        private TextView title,new_indicatior;
        private ImageView image;
        private RelativeLayout ll_kategori;

        public ViewHolder(View view) {
            super(view);
            title=view.findViewById(R.id.title);
            new_indicatior=view.findViewById(R.id.new_indicatior);
            image=view.findViewById(R.id.image);
            ll_kategori = view.findViewById(R.id.ll_kategori);

        }
    }


}

