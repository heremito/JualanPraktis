package www.starcom.com.jualanpraktis.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.feature.produk.ListSubKategoriProdukActivity;


public class SubKategoriAdapter extends RecyclerView.Adapter<SubKategoriAdapter.ViewHolder>  {
    private ArrayList<HashMap<String, String>> data;
    private Activity activity;
    private Button more;
    boolean userClicked=false;
    private  String status;

    public SubKategoriAdapter(Activity activity, ArrayList<HashMap<String, String>> data, Button  button,String status) {
        this.data=data;
        this.activity = activity;
        this.status= status;
        more = button;
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

        viewHolder.title.setText(item.get("subkategori"));
        viewHolder.new_indicatior.setVisibility(View.GONE);

        String img_url = "https://trading.my.id/img2/"+item.get("gambar");
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


       final HashMap<String, String> finalItem = item;
        /**   more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userClicked==false){

                    userClicked = true;
                    more.setText("Less");
                }else {
                    userClicked = false;
                    more.setText("More");
                }
                notifyDataSetChanged();
            }
        }); **/

        viewHolder.ll_kategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(activity, ListSubKategoriProdukActivity.class);
                    intent.putExtra("status","subkategori");
                    intent.putExtra("id",finalItem.get("id"));
                    intent.putExtra("subkategori",finalItem.get("subkategori"));
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
        if (status.equals("main")) {
            if (data.size() > 8) {
                if (userClicked) {
                    return (null != data ? data.size() : 0);

                } else {

                    return data.size() > 8 ? 8 : data.size();
                }
            } else {
              //  more.setVisibility(View.GONE);
                return (null != data ? data.size() : 0);
            }
            // return (null != data ? data.size() : 0);
        } else {

             return (null != data ? data.size() : 0);
        }

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

