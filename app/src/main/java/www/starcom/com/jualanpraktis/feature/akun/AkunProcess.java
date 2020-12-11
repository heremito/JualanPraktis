package www.starcom.com.jualanpraktis.feature.akun;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import www.starcom.com.jualanpraktis.Login.Pref;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.databinding.FragmentAkunCoorperateBinding;
import www.starcom.com.jualanpraktis.feature.pembayaran.FormatText;
import www.starcom.com.jualanpraktis.utils.CustomProgressDialog;

class AkunProcess {

    private FragmentAkunCoorperateBinding binding;
    private Activity activity;
    private CustomProgressDialog progressDialog;

    loginuser user ;
    Pref pref;
    public AkunProcess(FragmentAkunCoorperateBinding binding, Activity activity){
        this.binding  = binding;
        this.activity = activity;
        AndroidNetworking.initialize(activity);
        progressDialog = new CustomProgressDialog(activity);

        user = SharedPrefManager.getInstance(activity).getUser();
        pref = new Pref(activity.getApplicationContext());

    }

    void getLimit(){
        binding.txtLimit.setVisibility(View.GONE);
        binding.progress.setVisibility(View.VISIBLE);
        AndroidNetworking.post("https://jualanpraktis.net/android/limit-belanja.php")
                .addBodyParameter("id_customer",user.getId())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        binding.txtLimit.setVisibility(View.VISIBLE);
                        binding.progress.setVisibility(View.GONE);
                        try {
                            JSONArray array = response.getJSONArray("data");
                            JSONObject item = array.getJSONObject(0);

                            String limit_belanja = item.getString("limit_belanja");
                            binding.txtLimit.setText(FormatText.rupiahFormat(Double.parseDouble(limit_belanja)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        binding.txtLimit.setVisibility(View.VISIBLE);
                        binding.progress.setVisibility(View.GONE);

                    }
                });
    }


    void ubahPassword(String passwordLama,String passwordBaru){
        progressDialog.progress("Ubah Password","Loading...");

        HashMap<String,String> params = new HashMap<>();
        params.put("id_customer",user.getId());
        params.put("password1",passwordLama);
        params.put("password2",passwordBaru);

        AndroidNetworking.post("https://trading.my.id/android/update_password-corporate.php")
                .addBodyParameter(params)
                .setTag(activity)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();

                        try {
                            Toast.makeText(activity,response.getString("response"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(activity,"Gagal Ubah Password, Silahkan coba lagi",Toast.LENGTH_SHORT).show();
                    }
                });


    }


    void ubahProfile(HashMap<String,String>params){
        progressDialog.progress("Ubah Profile","Loading...");


        AndroidNetworking.post("https://trading.my.id/android/update_profile-corporate.php")
                .addBodyParameter(params)
                .setTag(activity)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressDialog.dismiss();
                        try {
                          //  JSONArray jsonArray = response.getJSONArray("response");
                            //JSONObject userJson = obj.getJSONObject("login");

                            JSONObject object = response.getJSONObject(0);
                                loginuser user = new loginuser(
                                        object.getString("id_member"),
                                        object.getString("kode"),
                                        object.getString("nama"),
                                        object.getString("nama_toko"),
                                        object.getString("provinsi"),
                                        object.getString("kota"),
                                        object.getString("kecamatan"),
                                        object.getString("alamat"),
                                        object.getString("no_ktp"),
                                        object.getString("no_npwp"),
                                        object.getString("no_hp"),
                                        object.getString("email"),


                                        object.getString("nama_bank"),
                                        object.getString("atas_nama"),
                                        object.getString("no_rek"),
                                        object.getString("foto")

//                                        object.getString("id_customer"),
//                                        object.getString("nama"),
//                                        object.getString("email"),
//                                        object.getString("no_hp"),
//                                        object.getString("jk"),
//                                        object.getString("alamat"),
//
//
//                                        object.getString("nama_provinsi"),
//                                        object.getString("nama_kota"),
//                                        object.getString("nama_kecamatan")
                                );
                                SharedPrefManager.getInstance(activity).userLogin(user);

                                Toast.makeText(activity,"Berhasil Ubah Profile",Toast.LENGTH_SHORT).show();



                        } catch (JSONException e) {
                            Toast.makeText(activity,"Gagal Ubah Profile, Silahkan coba lagi",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(activity,"Gagal Ubah Profile, Silahkan coba lagi",Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
