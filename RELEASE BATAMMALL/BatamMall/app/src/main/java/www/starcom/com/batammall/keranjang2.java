package www.starcom.com.jualanpraktis;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import www.starcom.com.jualanpraktis.Database.Database;
import www.starcom.com.jualanpraktis.Keranjang.keranjangAdapter;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.SubKategori.order;

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
    keranjangAdapter adapter ;
    //private order order ;

    //int Total = 0 ;
    int berat = 0 ;

    public keranjang2() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_keranjang);

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
        adapter = new keranjangAdapter(list,getApplicationContext(),null,keranjang2.this);
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

    }

    @Override
    public void onClick(View v) {
        if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn() && !Objects.equals(total.getText().toString(), "0")) {
            Intent intent = new Intent(keranjang2.this, alamat_pengiriman.class);
            intent.putExtra("total",total.getText().toString());
            intent.putExtra("berat",Integer.toString(berat));
            startActivity(intent);
            Log.d(TAG,"Total = "+total.getText().toString());
            Log.d(TAG,"Berat = "+Integer.toString(berat));
        }else if (Objects.equals(total.getText().toString(), "0")){
            Toast.makeText(getApplicationContext(), "Anda Belum Belanja", Toast.LENGTH_SHORT).show();
        }
        else {
            startActivity(new Intent(keranjang2.this, login.class));
        }
    }

}
