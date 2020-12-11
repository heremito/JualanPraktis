package www.starcom.com.jualanpraktis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

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
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.SubKategori.order;
import www.starcom.com.jualanpraktis.feature.form_pemesanan.FormTransaksiActivity;

/**
 * Created by ADMIN on 21/02/2018.
 */

public class keranjang2 extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getName();

    RecyclerView recyclerView ;
    LinearLayoutManager linearLayoutManager ;
    TextView total;
    Button btnSubmit ;

    List<order> list = new ArrayList<>();
    ArrayList<HashMap<String,String>> dataList = new ArrayList<>();
    keranjangAdapter adapter ;
    //private order order ;
    loginuser user ;

    //int Total = 0 ;
    int berat = 0 ;

    public keranjang2() {

    }

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_keranjang);
        user = SharedPrefManager.getInstance(keranjang2.this).getUser();
        AndroidNetworking.initialize(getApplicationContext());
        progressDialog = new ProgressDialog(keranjang2.this);
        recyclerView = findViewById(R.id.list_belanja);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        total = findViewById(R.id.total);
        btnSubmit = findViewById(R.id.submitOrder);
    }

    @Override
    public void onResume() {
        super.onResume();
        listItem(0,0);
        btnSubmit.setOnClickListener(this);
    }


    public void listItem(int Total,int Berat){
        list = new Database(this).getPesan();
        adapter = new keranjangAdapter(list,keranjang2.this,null,keranjang2.this);
        recyclerView.setAdapter(adapter);

        for (order order:list)
            Total += (Integer.parseInt(order.getHarga())) * (Integer.parseInt(order.getJumlah()));
        NumberFormat nf = new DecimalFormat("#,###");
        total.setText(nf.format(Total));
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

    @Override
    public void onClick(View v) {
        if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn() && !Objects.equals(total.getText().toString(), "0")) {
           proccess();
        }else if (Objects.equals(total.getText().toString(), "0")){
            Toast.makeText(getApplicationContext(), "Anda Belum Belanja", Toast.LENGTH_SHORT).show();
        }else {
            startActivity(new Intent(keranjang2.this, login.class));
        }
    }


    private void proccess(){
        progressDialog.setTitle("Melanjutkan Pemesanan");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    //    idtransaksi = Shared.getInstance(getApplicationContext()).getIdT();

        HashMap<String,String> param = new HashMap<>();
        param.put("customer", user.getId());
        int i = 0;
        for (HashMap<String, String> data : dataList){
            param.put("nomor["+i+"]", data.get("id"));
            i++;
        }

        AndroidNetworking.post("https://trading.my.id/android/pesan2.php")
                .addBodyParameter(param)
                .setTag(keranjang2.this)
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
                        String totalbelanja =total.getText().toString().replace(",","");
                        Intent intent = new Intent(keranjang2.this, FormTransaksiActivity.class);
                        intent.putExtra("total",totalbelanja);
                        intent.putExtra("berat",Integer.toString(berat));
                        intent.putExtra("id_transaksi",id_transaksi);
                        intent.putExtra("dataList",dataList);
                        startActivity(intent);
                        Log.d(TAG,"Total = "+total.getText().toString());
                        Log.d(TAG,"Berat = "+Integer.toString(berat));
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();

                    }
                });

    }

}
