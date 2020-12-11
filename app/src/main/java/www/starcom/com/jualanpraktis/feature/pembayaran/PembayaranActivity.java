package www.starcom.com.jualanpraktis.feature.pembayaran;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.databinding.ActivityPembayaranBinding;

public class PembayaranActivity extends AppCompatActivity {

    Activity activity = PembayaranActivity.this;
    ActivityPembayaranBinding binding;
    PembayaranProccess proccess;
    String total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_pembayaran);
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_pembayaran);
       proccess = new PembayaranProccess(binding,activity);

       Bundle bundle = getIntent().getExtras();
       if (bundle!=null){

           proccess.dataList = (ArrayList<HashMap<String, String>>) bundle.getSerializable("dataList");
           proccess.id_transaksi = bundle.getString("id_transaksi");
           proccess.total = bundle.getString("total");
           proccess.no_hp = bundle.getString("no_hp");
       }

        onClick(binding.cvVirutalAccount,0);
        onClick(binding.cvStore,1);
        onClick(binding.cvQris,2);
        onClick(binding.cvPayLater,3);
        onClick(binding.cvTransfer,4);
        onClick(binding.cvTransferBanksumut,5);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(PembayaranActivity.this,"Tidak bisa kembali, harap selesaikan pembayaran",Toast.LENGTH_LONG).show();
    }

    private void onClick(CardView cardView, final int code){
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 0 = va; 1 = store; 2 = qris; 3 = paylater;

                if (code==0){
                    proccess.dialog(0);
                }else if (code==1){
                    proccess.dialog(1);
                }else if (code==2){
                    proccess.alertDialog("QRIS",6);
                }else if (code==3){
                    proccess.alertDialog("PayLater",7);
                }else if (code==4){
                    proccess.alertDialog("Transfer BCA", 3);
                }else if (code==5){

                    proccess.alertDialog("Transfer Bank Sumut",8);
                }
            }
        });
    }





}
