package www.starcom.com.jualanpraktis.feature.belanja_bulanan;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.adapter.BelanjaBulananAdapter;
import www.starcom.com.jualanpraktis.databinding.FragmentListBelanjaBulananBinding;
import www.starcom.com.jualanpraktis.feature.form_pemesanan.FormTransaksiActivity;
import www.starcom.com.jualanpraktis.feature.pembayaran.FormatText;
import www.starcom.com.jualanpraktis.login;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListBelanjaBulananFragment extends Fragment {

    public ListBelanjaBulananFragment() {
        // Required empty public constructor
    }

    FragmentListBelanjaBulananBinding binding;

    ArrayList<HashMap<String,String>> cartList = new ArrayList<>();
    ArrayList<HashMap<String,String>> isBblList = new ArrayList<>();
    String totalbelanja;
    int berat = 0 ;
    loginuser user ;
    ProgressDialog progressDialog;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = binding.inflate(inflater,container,false);
        AndroidNetworking.initialize(getActivity().getApplicationContext());
        user = SharedPrefManager.getInstance(getActivity()).getUser();
        progressDialog = new ProgressDialog(getActivity());

        //0 = belanja lagi,1 = saran, 2 checkout
        onClick(binding.btnBelanjaLagi,0);
        onClick(binding.btnSaran,1);
        onClick(binding.submitOrder,2);

        AndroidNetworking.initialize(getActivity().getApplicationContext());
       // user = SharedPrefManager.getInstance(getActivity()).getUser();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        binding.listBelanja.setLayoutManager(llm);
        binding.listBelanja.setHasFixedSize(false);

      //  getCart();

        return binding.getRoot();
    }

    public void onChangeData(){
        int Total=0; int Berat = 0;
        for (HashMap<String,String> item:cartList){
            Total += (Integer.parseInt(item.get("harga"))) * (Integer.parseInt(item.get("jumlah")));
            //  NumberFormat nf = new DecimalFormat("#,###");
            //total.setText(nf.format(Total));
            binding.total.setText(FormatText.rupiahFormat(Total));
            totalbelanja = String.valueOf(Total);

            Berat+=(Integer.parseInt(item.get("berat")))* (Integer.parseInt(item.get("jumlah")));
            berat = Berat;
        }
    }


    private void onClick(Button button, final int code){

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (code){

                    case 0:
                        getActivity().finish();
                        getActivity().overridePendingTransition(0, 0);
                        getActivity().startActivity(getActivity().getIntent().setFlags(getActivity().getIntent().FLAG_ACTIVITY_NO_ANIMATION));
                        getActivity().overridePendingTransition(0, 0);
                        break;
                    case 1:
                        if (SharedPrefManager.getInstance(getActivity()).isLoggedIn()){
                            startActivity(new Intent(getActivity(),SaranActivity.class));
                        }else {
                            startActivity(new Intent(getActivity(), login.class));
                            Toast.makeText(getActivity(), "Harap masuk terlebih dahulu", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        if (SharedPrefManager.getInstance(getActivity()).isLoggedIn() && !Objects.equals(binding.total.getText().toString(), "Rp. 0")) {
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
                                proccess();
                            }

                        }else if (Objects.equals(binding.total.getText().toString(), "Rp. 0")){
                            Toast.makeText(getActivity(), "Anda Belum Belanja", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            startActivity(new Intent(getActivity(), login.class));
                            Toast.makeText(getActivity(), "Harap masuk terlebih dahulu", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
    }


    public void getCart(){
        binding.listBelanja.setVisibility(View.GONE);
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmerAnimation();
        HashMap<String,String> param = new HashMap<>();
        param.put("customer", user.getId());

        AndroidNetworking.post("https://trading.my.id/android/android/bbl.php")
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

                            binding.shimmer.setVisibility(View.GONE);
                            binding.shimmer.stopShimmerAnimation();
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
                                item.put("stok",object.getString("stok"));
                               // item.put("bbl",object.getString("bbl"));
                                cartList.add(item);
                            }

                         /**   isBblList.clear();
                            for (HashMap<String,String> item :cartList){
                                if (item.get("bbl")!=null || !item.get("bbl").equals("")|| !item.get("bbl").equals("null")){
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
                            binding.listBelanja.setVisibility(View.VISIBLE);
                            BelanjaBulananAdapter adapter = new BelanjaBulananAdapter(getActivity(),cartList, ListBelanjaBulananFragment.this);
                            binding.listBelanja.setAdapter(adapter);

                            if (adapter.getItemCount()==0){
                                binding.scrollKosong.setVisibility(View.VISIBLE);
                            }

                            int Total=0; int Berat = 0;
                            for (HashMap<String,String> item:cartList){
                                Total += (Integer.parseInt(item.get("harga"))) * (Integer.parseInt(item.get("jumlah")));
                                //  NumberFormat nf = new DecimalFormat("#,###");
                                //total.setText(nf.format(Total));
                                binding.total.setText(FormatText.rupiahFormat(Total));
                                totalbelanja = String.valueOf(Total);

                                Berat+=(Integer.parseInt(item.get("berat")));
                                berat = Berat;

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
//                        progressDialog.dismiss();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.shimmer.stopShimmerAnimation();

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
        //  idtransaksi = Shared.getInstance(getApplicationContext()).getIdT();

        HashMap<String,String> param = new HashMap<>();
        param.put("customer", user.getId());

        int i = 0;
        for (HashMap<String, String> data : cartList){
            param.put("nomor["+i+"]", data.get("nomor"));
            param.put("jumlah["+i+"]", data.get("jumlah"));
            param.put("berat["+i+"]", data.get("berat"));
            param.put("ket1["+i+"]", data.get("id_variasi"));
            param.put("harga_jual_item["+i+"]", data.get("harga"));
            i++;
        }

        AndroidNetworking.post("https://trading.my.id/android/android/pesan2.php")
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


                        Intent intent = new Intent(getActivity(), FormTransaksiActivity.class);
                        intent.putExtra("total",totalbelanja);
                        intent.putExtra("berat",Integer.toString(berat));
                        intent.putExtra("id_transaksi",id_transaksi);
                        intent.putExtra("dataList",cartList);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();

                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();

        binding.total.setText(FormatText.rupiahFormat(Double.parseDouble("0")));
        getCart();
    }
}
