package www.starcom.com.jualanpraktis.feature.belanja_bulanan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.adapter.AddSaranAdapter;
import www.starcom.com.jualanpraktis.databinding.ActivitySaranBinding;

public class SaranActivity extends AppCompatActivity {

    Activity activity = SaranActivity.this;
    ActivitySaranBinding binding;
    ArrayList<HashMap<String,String>> dataList = new ArrayList<>();

    ProgressDialog progressDialog;
    loginuser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_saran);
        AndroidNetworking.initialize(getApplicationContext());
        progressDialog = new ProgressDialog(activity);
        user = SharedPrefManager.getInstance(activity).getUser();
        setSupportActionBar(binding.toolbar);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        binding.btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kirimSaran();
            }
        });
        binding.btnTambahItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddItem();
            }
        });
    }

    private void setData(){
        AddSaranAdapter adapter = new AddSaranAdapter(activity,dataList);
        binding.recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void dialogAddItem(){
        AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        View layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_add_saran, null);

        final EditText edtNamaBarang = layoutView.findViewById(R.id.edtNamaBarang);
        final EditText edtJenisBarang = layoutView.findViewById(R.id.edtJenisBarang);
        final EditText edtKeterangan = layoutView.findViewById(R.id.edtKeterangan);
        final Button btn_simpan = layoutView.findViewById(R.id.btn_simpan);

        dialogBuilder.setView(layoutView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> data = new HashMap<>();
                data.put("nama",edtNamaBarang.getText().toString());
                data.put("jenis",edtJenisBarang.getText().toString());
                data.put("keterangan",edtKeterangan.getText().toString());
                dataList.add(data);
                alertDialog.dismiss();

                setData();
            }
        });

    }

    private void kirimSaran(){
        String url = "https://trading.my.id/android/android/saran_produk.php";
        HashMap<String,String> param = new HashMap<>();
        param.put("id_customer",user.getId());
        int f = 0;
        for (HashMap<String,String> item : dataList){
            param.put("nama_produk["+f+"]",item.get("nama"));
            param.put("jenis["+f+"]",item.get("jenis"));
            param.put("ket["+f+"]",item.get("keterangan"));
            f++;
        }

        AndroidNetworking.post(url)
                .addBodyParameter(param)
                .setTag(activity)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String message = null;
                        try {
                            message = response.getString("message");
                            if (message.equals("sukses")){
                                new MaterialAlertDialogBuilder(activity)
                                        .setTitle("Berhasil Mengirim Data")
                                        .setMessage("Terima kasih telah mengirimkan saran produk")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                finish();
                                            }
                                        }).show();
                            }else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }
}
