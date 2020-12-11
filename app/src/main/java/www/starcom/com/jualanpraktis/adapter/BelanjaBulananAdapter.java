package www.starcom.com.jualanpraktis.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.HashMap;

import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.feature.belanja_bulanan.ListBelanjaBulananFragment;
import www.starcom.com.jualanpraktis.feature.pembayaran.FormatText;
import www.starcom.com.jualanpraktis.utils.Enums.ActionEnum;
import www.starcom.com.jualanpraktis.utils.Interface.ValueChangedListener;
import www.starcom.com.jualanpraktis.utils.NumberPicker;


public class BelanjaBulananAdapter extends RecyclerView.Adapter<BelanjaBulananAdapter.ViewHolder>  {
    private ArrayList<HashMap<String, String>> data;
    private Activity activity;
    private ListBelanjaBulananFragment keranjang;


    public BelanjaBulananAdapter(Activity activity, ArrayList<HashMap<String, String>> data, ListBelanjaBulananFragment keranjang) {
        this.data=data;
        this.activity = activity;
        this.keranjang = keranjang;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //inflate your layout and pass it to view holder
        LayoutInflater inflater = activity.getLayoutInflater();  View view;
        if (keranjang==null){
            view = inflater.inflate(R.layout.item_cart, viewGroup, false);
        }else {
            view = inflater.inflate(R.layout.item_cart_new, viewGroup, false);
        }
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        HashMap<String, String> item = new HashMap<>();
        item = this.data.get(i);

        if (keranjang==null){
            viewHolder.card_item_jumlah.setVisibility(View.VISIBLE);
            viewHolder.number_picker.setVisibility(View.GONE);
            viewHolder.number_button.setVisibility(View.GONE);
            viewHolder.txt_sisa.setVisibility(View.GONE);
        }else {

            viewHolder.card_item_jumlah.setVisibility(View.GONE);
            viewHolder.number_picker.setVisibility(View.VISIBLE);
            viewHolder.number_button.setVisibility(View.GONE);
            viewHolder.txt_sisa.setVisibility(View.VISIBLE);

            viewHolder.txt_sisa.setText("Stok tersedia "+item.get("stok"));
        }


        TextDrawable drawable = TextDrawable.builder()
                .buildRound(""+item.get("jumlah"), Color.RED);
        viewHolder.card_item_jumlah.setImageDrawable(drawable);


        viewHolder.number_picker.setValue(Integer.parseInt(item.get("jumlah")));
        viewHolder.number_picker.setMin(1);
       String label = item.get("nama") +" - "+item.get("variasi");
       viewHolder.lbl_nama.setText(label);

        int hargaItem = Integer.parseInt(item.get("harga"))*Integer.parseInt(item.get("jumlah"));
        viewHolder.lbl_nominal.setText(FormatText.rupiahFormat(hargaItem));

        String urlImage = "https://batammall.co.id/img/"+item.get("gambar");
       // Picasso.get().load(urlImage).into(viewHolder.gambar);
        Glide.with(activity.getApplicationContext())
                .load(urlImage)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(150,150).skipMemoryCache(false))
                .into(viewHolder.gambar);


        if (keranjang==null){
            viewHolder.buang.setVisibility(View.GONE);
        }
        final HashMap<String, String> finalItem = item;
        viewHolder.buang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    new MaterialAlertDialogBuilder(activity)
                            .setTitle("Hapus Item")
                            .setMessage("Anda yakin ingin menghapus pesanan ini?")

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteItem(finalItem.get("nomor"));
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();

            }
        });
         int stok = 0;
        if (keranjang!=null){
           stok = Integer.parseInt(finalItem.get("stok")) + Integer.parseInt(finalItem.get("jumlah"));
        }

        final int finalStok = stok;
        viewHolder.number_picker.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                if (finalStok <value){
                    viewHolder.number_picker.setValue(Integer.parseInt(finalItem.get("jumlah")));
                  //  finalItem.put("jumlah",String.valueOf(oldValue));
                    Toast.makeText(activity,"Stok tidak cukup",Toast.LENGTH_SHORT).show();
                }else{

                    finalItem.put("jumlah",String.valueOf(value));
                    int hargaItem = Integer.parseInt(finalItem.get("harga"))*Integer.parseInt(finalItem.get("jumlah"));
                    viewHolder.lbl_nominal.setText(FormatText.rupiahFormat(hargaItem));
                    keranjang.onChangeData();
                }
            }
        });

        viewHolder.number_button.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                if (Integer.parseInt(finalItem.get("stok"))<newValue){
                    view.setNumber(String.valueOf(oldValue));
                    finalItem.put("jumlah",String.valueOf(oldValue));
                    Toast.makeText(activity,"Stok tidak cukup",Toast.LENGTH_SHORT).show();
                }else{

                    finalItem.put("jumlah",String.valueOf(newValue));
                    keranjang.onChangeData();
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
        private TextView lbl_nama,lbl_status,lbl_nominal,lbl_tanggal,txt_sisa;
        private ImageView gambar,buang,card_item_jumlah;
        private ElegantNumberButton number_button;
        private NumberPicker number_picker;

        public ViewHolder(View view) {
            super(view);
            lbl_nama =  view.findViewById(R.id.card_item_nama);
            lbl_nominal=view.findViewById(R.id.card_item_harga);
            gambar = view.findViewById(R.id.gambar);
            number_button = view.findViewById(R.id.number_button);
            number_picker = view.findViewById(R.id.number_picker);
            txt_sisa = view.findViewById(R.id.txt_sisa);
            buang = view.findViewById(R.id.buang);
            card_item_jumlah = view.findViewById(R.id.card_item_jumlah);
        }
    }

    void deleteItem(String nomor){
        AndroidNetworking.initialize(activity);
        AndroidNetworking.post("https://batammall.co.id/ANDROID/delete_item.php")
                .addBodyParameter("nomor",nomor)
                .setTag(activity)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("Berhasil Hapus")){
                            activity.finish();
                            activity.overridePendingTransition(0, 0);
                            activity.startActivity(activity.getIntent().setFlags(activity.getIntent().FLAG_ACTIVITY_NO_ANIMATION));
                            activity.overridePendingTransition(0, 0);
                            Toast.makeText(activity,"Berhasil Hapus",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }


}

