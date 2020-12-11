package www.starcom.com.jualanpraktis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import www.starcom.com.jualanpraktis.Database.Database;
import www.starcom.com.jualanpraktis.IDTansaksi.Shared;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.VolleySingleton;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.feature.pembayaran.PembayaranActivity;

/**
 * Created by ADMIN on 19/02/2018.
 */

public class konfirmasi extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getName();

    public static final String EXTRA_ID = "id_transaksi";
    public static final String EXTRA_TOTAL = "total";

    TextView id,total,tanggal;
    EditText catatan;
    Button btnKirim ;
    loginuser user ;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi);

        user = SharedPrefManager.getInstance(this).getUser();
        progressDialog = new ProgressDialog(konfirmasi.this);

        id = findViewById(R.id.id_transaksi);
        total = findViewById(R.id.total);
        tanggal = findViewById(R.id.tanggal);
        catatan = findViewById(R.id.catatan);
        catatan.setFocusable(true);
        btnKirim = findViewById(R.id.button);

        int tot = Integer.parseInt(getIntent().getExtras().getString(EXTRA_TOTAL)) ;
        NumberFormat nf = new DecimalFormat("#,###");

        btnKirim.setOnClickListener(this);

        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey(EXTRA_ID)) {
                id.setText(getIntent().getExtras().getString(EXTRA_ID));
            }
            if (getIntent().getExtras().containsKey(EXTRA_TOTAL)) {
                total.setText("Rp "+ nf.format(tot));
            }

        }

        Calendar calendar = Calendar.getInstance();
        Date tomorrow = calendar.getTime();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
        String tgl = dateFormat.format(tomorrow);
        tanggal.setText(tgl);


    }

    @Override
    public void onClick(View v) {//di
        //di by pass d;u yaa xixixixi
       startActivity(new Intent(konfirmasi.this, PembayaranActivity.class));
    }

    void konfirmasiProses(){
        progressDialog.setTitle("Konfirmasi Pemesanan");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, "https://jualanpraktis.net/android/konfirmasi.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response.equals("Data Berhasil Di Kirim")){
                    Intent intent = new Intent(konfirmasi.this,MainActivity.class);
                    startActivity(intent);
                    new Database(getBaseContext()).cleanAllChart();
                    Shared.getInstance(getApplicationContext()).logout();
                    finish();
                    Toast.makeText(getApplicationContext(), "Pemesanan telah diKonfirmasi", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Gagal Melakukan Konfirmasi", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Gagal Melakukan Konfirmasi", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String nama = user.getNama() ;
                String ID = user.getId();
                Map<String,String> params = new HashMap<String,String>();
                params.put("id_transaksi",id.getText().toString());
                params.put("id_customer",ID);
                params.put("jumlah_bayar",getIntent().getExtras().getString(EXTRA_TOTAL));
                params.put("catatan",catatan.getText().toString());
                params.put("user_konfirmasi",nama);
                return params ;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

}
