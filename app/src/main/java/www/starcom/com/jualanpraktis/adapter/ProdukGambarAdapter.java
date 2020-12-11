package www.starcom.com.jualanpraktis.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;

import www.starcom.com.jualanpraktis.R;

public class ProdukGambarAdapter extends RecyclerView.Adapter<ProdukGambarAdapter.ViewHolder> {
    private ArrayList<HashMap<String, String>> data;
    private Activity activity;
    private ImageView imageView;

    public ProdukGambarAdapter(Activity activity, ArrayList<HashMap<String, String>> data, ImageView imageView) {
        this.data = data;
        this.activity = activity;
        this.imageView = imageView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //inflate your layout and pass it to view holder
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_image_detail, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        HashMap<String, String> item = new HashMap<>();
        item = this.data.get(i);

        final String url = "https://trading.my.id/img/" ;
        Glide.with(activity.getApplicationContext())
                .load(url+ item.get("gambar"))
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(200,200).skipMemoryCache(false))
                .into(viewHolder.gambar);

        final HashMap<String, String> finalItem = item;
        viewHolder.gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(activity.getApplicationContext())
                        .load(url+ finalItem.get("gambar"))
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(512,512).skipMemoryCache(false))
                        .into(imageView);
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
        private ImageView gambar;

        public ViewHolder(View view) {
            super(view);
            gambar = view.findViewById(R.id.gambar_detail);
        }
    }
}
