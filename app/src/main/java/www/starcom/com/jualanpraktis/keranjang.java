package www.starcom.com.jualanpraktis;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import www.starcom.com.jualanpraktis.Database.Database;
import www.starcom.com.jualanpraktis.Keranjang.keranjangAdapter;
import www.starcom.com.jualanpraktis.Login.Pref;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.SubKategori.order;
import www.starcom.com.jualanpraktis.adapter.CartAdapter;
import www.starcom.com.jualanpraktis.feature.form_pemesanan.FormTransaksiActivity;
import www.starcom.com.jualanpraktis.feature.pembayaran.FormatText;

/**
 * Created by ADMIN on 05/02/2018.
 */

public class keranjang extends Fragment implements View.OnClickListener{

    private final String TAG = this.getClass().getName();

    RecyclerView recyclerView ;
    LinearLayoutManager linearLayoutManager ;

    TextView total;
    Button btnSubmit,btn_belanja_lagi ;
    loginuser user ;

    List<order>  list = new ArrayList<>();
    ArrayList<HashMap<String,String>> dataList = new ArrayList<>();
    ArrayList<HashMap<String,String>> cartList = new ArrayList<>();
    ArrayList<HashMap<String,String>> isBblList = new ArrayList<>();
    keranjangAdapter adapter ;
    ProgressDialog progressDialog;
    //private order order ;

    String totalbelanja;

    int berat = 0 ;
    www.starcom.com.jualanpraktis.IDTansaksi.idtransaksi idtransaksi ;

    String harga_dropshpper;
    private ShimmerFrameLayout shimmer;
    int grandTotal = 0;
    Pref pref;
    public keranjang() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_keranjang,container,false);
//        View rootView2 = inflater.inflate(R.layout.item_cart_new, container, false);
        user = SharedPrefManager.getInstance(getActivity()).getUser();
        AndroidNetworking.initialize(getActivity().getApplicationContext());
        progressDialog = new ProgressDialog(getActivity());
        pref = new Pref(getContext());

        recyclerView = rootView.findViewById(R.id.list_belanja);
        shimmer = rootView.findViewById(R.id.shimmer);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        total = rootView.findViewById(R.id.total);
        btnSubmit = rootView.findViewById(R.id.submitOrder);

        btn_belanja_lagi = rootView.findViewById(R.id.btn_belanja_lagi);

        //harga_dropshipper = rootView.findViewById(R.id.harga_dropshipper);

//        harga_dropshipper = (EditText) rootView.findViewById(R.id.harga_dropshipper);
//        coba = harga_dropshipper.getText().toString();

        btn_belanja_lagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
                getActivity().startActivity(getActivity().getIntent().setFlags(getActivity().getIntent().FLAG_ACTIVITY_NO_ANIMATION));
                getActivity().overridePendingTransition(0, 0);
            }
        });
        return rootView;
    }



    @Override
    public void onResume() {
        super.onResume();
        total.setText(FormatText.rupiahFormat(Double.parseDouble("0")));
        getCart();
      //  listItem(0,0);

        btnSubmit.setOnClickListener(this);
    }


    public void listItem(int Total,int Berat){
        list = new Database(getContext()).getPesan();
        adapter = new keranjangAdapter(list,getActivity(),keranjang.this,null);
        recyclerView.setAdapter(adapter);

        for (order order:list)
                Total += (Integer.parseInt(order.getHarga())) * (Integer.parseInt(order.getJumlah()));
                NumberFormat nf = new DecimalFormat("#,###");
                //total.setText(nf.format(Total));
        total.setText(FormatText.rupiahFormat(Total));
        totalbelanja = String.valueOf(Total);
                Log.d(TAG, Integer.toString(Total));

        for (order order:list)
            Berat+=(Integer.parseInt(order.getBerat()));
            berat = Berat;
        Log.d(TAG,Integer.toString(Berat));

        for (order item : list){
            HashMap<String,String> data = new HashMap<>();
            data.put("id",item.getID());
            data.put("product",item.getNamaProduk());
            data.put("quantity",item.getJumlah());
            data.put("price",item.getHarga());
            dataList.add(data);
        }



    }

    public void onChangeData(){
            int Total=0; int Berat = 0;
            for (HashMap<String,String> item:cartList){
                Total += (Integer.parseInt(item.get("harga"))) * (Integer.parseInt(item.get("jumlah")));
                //  NumberFormat nf = new DecimalFormat("#,###");
                //total.setText(nf.format(Total));
                total.setText(FormatText.rupiahFormat(Total));
                totalbelanja = String.valueOf(Total);

                Berat+=(Integer.parseInt(item.get("berat_item")))* (Integer.parseInt(item.get("jumlah")));
                berat = Berat;
        }
    }

    @Override
    public void onClick(View v) {

        if (SharedPrefManager.getInstance(getActivity()).isLoggedIn() && !Objects.equals(total.getText().toString(), "Rp. 0")) {
            if (Integer.parseInt(totalbelanja)<5000){
                new AlertDialog.Builder(getActivity())
                       // .setTitle("Tidak bisa melanjutkan pemesanan")
                        .setMessage("Minimal pemesanan sejumlah Rp. 5000")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }else {
                if (pref.getLoginMethod().equals("coorperate")){
                    if (Integer.parseInt(totalbelanja)>Integer.parseInt(pref.getLimitBelanja())){
                        new AlertDialog.Builder(getActivity())
                                 .setTitle("Perhatian")
                                .setMessage("Limit Belanja anda tidak cukup")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                    }else {
                        proccess();
                    }
                }else {
                    proccess();
                }
            }

        }else if (Objects.equals(total.getText().toString(), "Rp. 0")){
            Toast.makeText(getActivity(), "Anda Belum Belanja", Toast.LENGTH_SHORT).show();
        }
        else {
            startActivity(new Intent(getActivity(), login.class));
            Toast.makeText(getActivity(), "Harap masuk terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
    }

    public void getHargaDropshipper(String test){
        harga_dropshpper = test;
    }


    public void getCart(){
        recyclerView.setVisibility(View.GONE);
        shimmer.setVisibility(View.VISIBLE);
        shimmer.startShimmerAnimation();
        HashMap<String,String> param = new HashMap<>();
        param.put("customer", user.getId());

        AndroidNetworking.post("https://jualanpraktis.net/android/cart.php")
                .addBodyParameter(param)
                .setTag(getActivity())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                      //  progressDialog.dismiss();
                        try {
                            cartList.clear();

                            shimmer.setVisibility(View.GONE);
                            shimmer.stopShimmerAnimation();
                            JSONArray array = response.getJSONArray("data");
                            for (int i = 0;i<array.length();i++){
                                JSONObject object = array.getJSONObject(i);
                                HashMap<String,String> item = new HashMap<>();
                                item.put("nomor",object.getString("nomor"));
                                item.put("nama",object.getString("nama_produk"));
                                item.put("id_variasi", object.getString("ket1"));
                                item.put("variasi",object.getString("ket2"));
                                item.put("gambar",object.getString("image_o"));
                                item.put("harga",object.getString("harga_item"));
                                item.put("jumlah",object.getString("jumlah"));
                                item.put("berat",object.getString("berat"));
                                item.put("berat_item",object.getString("berat_item"));
                                item.put("stok",object.getString("stok"));
                                item.put("fee",object.getString("fee"));
//                                item.put("bbl",object.getString("bbl"));
                                cartList.add(item);
                            }
/**

                            isBblList.clear();
                            for (HashMap<String,String> item :cartList){
                                if (item.get("bbl")==null || item.get("bbl").equals("")|| item.get("bbl").equals("null")){
                                    HashMap<String,String> data = new HashMap<>();
                                    data.put("nomor",item.get("nomor"));
                                    data.put("nama",item.get("nama"));
                                    data.put("id_variasi",item.get("id_variasi"));
                                    data.put("variasi",item.get("variasi"));
                                    data.put("gambar",item.get("gambar"));
                                    data.put("harga",item.get("harga"));
                                    data.put("jumlah",item.get("jumlah"));
                                    data.put("berat",item.get("berat"));
                                    data.put("stok",item.get("stok"));
                                  //  data.put("bbl",object.getString("bbl"));
                                    isBblList.add(data);
                                }
                            } */
                            recyclerView.setVisibility(View.VISIBLE);
                            CartAdapter adapter = new CartAdapter(getActivity(),cartList,keranjang.this);
                            recyclerView.setAdapter(adapter);

                            int Total=0; int Berat = 0;
                            for (HashMap<String,String> item:cartList){
                                Total += (Integer.parseInt(item.get("harga"))) * (Integer.parseInt(item.get("jumlah")));
                                //  NumberFormat nf = new DecimalFormat("#,###");
                                //total.setText(nf.format(Total));
                                total.setText(FormatText.rupiahFormat(Total));
                                totalbelanja = String.valueOf(Total);

                                Berat+=(Integer.parseInt(item.get("berat_item")));
                                berat = Berat;

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
//                        progressDialog.dismiss();
                        shimmer.setVisibility(View.GONE);
                        shimmer.stopShimmerAnimation();

                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(getContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")){
                                Toast.makeText(getContext(), "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
    }
 
    private void proccess(){
        progressDialog.setTitle("Melanjutkan Pemesanan");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String test = "";
      //  idtransaksi = Shared.getInstance(getApplicationContext()).getIdT();

        HashMap<String,String> param = new HashMap<>();
        param.put("customer", user.getId());

        int i = 0;
        for (HashMap<String, String> data : cartList){
            param.put("nomor["+i+"]", data.get("nomor"));
            param.put("jumlah["+i+"]", data.get("jumlah"));
            param.put("berat["+i+"]", data.get("berat_item"));
            param.put("fee["+i+"]", data.get("fee"));
            param.put("harga_dropshipper["+i+"]", harga_dropshpper);
            param.put("harga_jual_item["+i+"]", data.get("harga"));
            param.put("ket1["+i+"]", data.get("id_variasi"));
            i++;
        }

        AndroidNetworking.post("https://jualanpraktis.net/android/pesan2.php")
                .addBodyParameter(param)
                .setTag(getActivity())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        String id_transaksi = "";

                        try {
                             id_transaksi = response.getString("id_transaksi");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("Belanja", param.toString());

                        Intent intent = new Intent(getActivity(), FormTransaksiActivity.class);
                        intent.putExtra("total",totalbelanja);
                        intent.putExtra("berat",Integer.toString(berat));
                        intent.putExtra("id_transaksi",id_transaksi);
                        intent.putExtra("dataList",cartList);
                        startActivity(intent);
                        Log.d(TAG,"Total = "+totalbelanja);
                        Log.d(TAG,"Berat = "+Integer.toString(berat));
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),"Gagal, silahkan coba beberapa saat lagi.",Toast.LENGTH_SHORT).show();
                        Log.d("Belanja", param.toString());
                    }
                });

    }


}
