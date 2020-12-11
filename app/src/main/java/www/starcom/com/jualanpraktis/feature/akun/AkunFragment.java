package www.starcom.com.jualanpraktis.feature.akun;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystemNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import www.starcom.com.jualanpraktis.Login.Pref;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.VolleySingleton;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.Spinner.Kecamatan;
import www.starcom.com.jualanpraktis.Spinner.KotaKabupaten;
import www.starcom.com.jualanpraktis.Spinner.Provinsi;
import www.starcom.com.jualanpraktis.login;
import www.starcom.com.jualanpraktis.utils.CustomProgressDialog;

import static android.app.Activity.RESULT_OK;
import static www.starcom.com.jualanpraktis.alamat_pengiriman.BASE_URL;
import static www.starcom.com.jualanpraktis.alamat_pengiriman.KEY;

/**
 * Created by ADMIN on 05/02/2018.
 */

public class AkunFragment extends Fragment implements View.OnClickListener{
    private Button btn_logout,btn_login,btn_ubah_profil,btn_ubah_password,btn_update_photo ;
    private TextView nama,email,nohp,jk,time,alamat,nama2,email2, nohp2;
    private TextView namatoko, noktp, nonpwp, atasnama, namabank, norek;
    private ImageView imageProfile;
    private String PicturePath;
    //public static final String URL_Image = "https://trading.my.id/files/drp/";
    public static final String URL_Update = "https://jualanpraktis.net/android/update_akun.php";
    ImageView edit ;
    loginuser user;
    GoogleSignInClient googleSignInClient;


    //edit
    List<KotaKabupaten> kotaKabupatenList = new ArrayList<>();
    List<Kecamatan> kecamatanList = new ArrayList<>();
    List<Provinsi> provinsiList = new ArrayList<>();
    String id_kota,id_wilayah,id_provinsi ;
    String nama_kota,nama_wilayah,nama_provinsi;
    Pref pref;
    CustomProgressDialog progressDialog;

    public AkunFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_akun,container,false);
         progressDialog = new CustomProgressDialog(getActivity());

        pref = new Pref(getActivity().getApplicationContext());
        nama = rootView.findViewById(R.id.nama);
        email = rootView.findViewById(R.id.email);
        nohp = rootView.findViewById(R.id.nohp);
        jk = rootView.findViewById(R.id.jk);
        time = rootView.findViewById(R.id.time);
        alamat = rootView.findViewById(R.id.alamat);
        nama2 = rootView.findViewById(R.id.nama2);
        email2 = rootView.findViewById(R.id.email2);
        nohp2 = rootView.findViewById(R.id.nohp2);
        noktp = rootView.findViewById(R.id.no_ktp);
        nonpwp = rootView.findViewById(R.id.no_npwp);
        atasnama = rootView.findViewById(R.id.atas_nama);
        norek = rootView.findViewById(R.id.no_rek);
        namabank = rootView.findViewById(R.id.nama_bank);
        namatoko = rootView.findViewById(R.id.nama_toko);
        imageProfile = rootView.findViewById(R.id.image_profile);

        btn_ubah_profil = rootView.findViewById(R.id.btn_ubah_profil);
        btn_ubah_password = rootView.findViewById(R.id.btn_ubah_password);
        btn_update_photo = rootView.findViewById(R.id.btn_update_picture);
        edit = rootView.findViewById(R.id.edit);
        edit.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // .requestIdToken()
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        btn_login = rootView.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);
            }
        });

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    ImagePicker.Companion.with(AkunFragment.this)
                            .crop(340,340)
                            .compress(512)
                            .start();
            }
        });
        btn_ubah_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogModify();
            }
        });

        btn_update_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageProfile != null) {
                    updateImage();
                }else{
                    Toast.makeText(getContext(), "anda belum upload photo, silahkan upload terlebih dahulu", Toast.LENGTH_SHORT);
                }
            }
        });

        btn_ubah_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                View layoutView = getLayoutInflater().inflate(R.layout.dialog_ubah_password, null);

                final EditText edt_pass_lama = layoutView.findViewById(R.id.edt_password_lama);
                final EditText edt_pass_baru= layoutView.findViewById(R.id.edt_password_baru);
                final Button btn_submit = layoutView.findViewById(R.id.btn_submit);

                dialogBuilder.setView(layoutView);

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setCancelable(true);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ubahPassword(edt_pass_lama.getText().toString(), edt_pass_baru.getText().toString());
                        alertDialog.dismiss();
                    }
                });
            }
        });
        btn_logout = rootView.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
                    btn_logout.setVisibility(View.GONE);
                    btn_ubah_profil.setVisibility(View.GONE);
                    btn_ubah_password.setVisibility(View.GONE);
                    btn_login.setVisibility(View.GONE);
                    btn_update_photo.setVisibility(View.GONE);
                    SharedPrefManager.getInstance(getContext()).logout();
                    pref.setLoginMethod("");
                    LoginManager.getInstance().logOut();
                    googleSignInClient.signOut();
                    nama.setText("");
                    nama2.setText("");
                    //jk.setText("");
                    alamat.setText("");
                    email.setText("");
                    nohp.setText("");
                    noktp.setText("");
                    nonpwp.setText("");
                    atasnama.setText("");
                    norek.setText("");
                    namabank.setText("");
                    imageProfile.setClickable(false);
                    imageProfile.setImageResource(R.drawable.icon_profile_grey);
                    Intent intent = new Intent(getActivity(), login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    getActivity().finish();
//                    getActivity().finish();
//                    getActivity().overridePendingTransition(0, 0);
//                    getActivity().startActivity(getActivity().getIntent().setFlags(getActivity().getIntent().FLAG_ACTIVITY_NO_ANIMATION));
//                    getActivity().overridePendingTransition(0, 0);
                    Toast.makeText(getContext(), "Anda Telah Logout", Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(getContext(), "Anda Belum Login", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if (resultCode==RESULT_OK){
                Uri uri = data.getData();
                imageProfile.setImageURI(uri);
                PicturePath = uri.getPath();
            }else{
                Toast.makeText(getContext(), "anda belum upload photo, silahkan upload terlebih dahulu", Toast.LENGTH_SHORT);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
            //getting the current user
            user = SharedPrefManager.getInstance(getActivity()).getUser();
            //setting the values to the textviews
            nama.setText(user.getNama());
            nohp.setText(user.getNo_hp());
            email.setText(user.getEmail());
            nama2.setText(user.getNama());
            namatoko.setText(user.getNama_toko());
            //alamat.setText(user.getAlamat());
//        String alamat_lengkap = user.getAlamat() +", Kecamatan "+user.getKecamatan()+", Kota/Kabupaten "+
//                user.getKota() +", Provinsi "+user.getProvinsi() +".";
        String alamat_lengkap = user.getAlamat() + ", " +user.getKecamatan() +
                 ", " + user.getKota()  + ", " +user.getProvinsi() +".";
        if (user.getProvinsi().equals("-")){
            alamat_lengkap = "-";
        }else if (user.getProvinsi()==null){
            alamat_lengkap = "-";
        }else if (user.getProvinsi().equals("null")){
            alamat_lengkap = "-";
        }else if (user.getProvinsi().equals("")){
            alamat_lengkap = "-";
        }
            alamat.setText(alamat_lengkap);
            email2.setText(user.getEmail());
            noktp.setText(user.getNo_ktp());
            nonpwp.setText(user.getNo_npwp());
            nohp2.setText(user.getNo_hp());
            atasnama.setText(user.getAtas_nama());
            norek.setText(user.getNo_rek());
            namabank.setText(user.getNama_bank());

            String kode = user.getKode().toString();

            UrlImageViewHelper test = new UrlImageViewHelper();
            test.setUrlDrawable(imageProfile, "https://jualanpraktis.net/files/drp/"+ kode +"/" + user.getFoto());
            String LinkGambar = "https://jualanpraktis.net/files/drp/"+ kode +"/" + user.getFoto();

            Log.d(getTag(), LinkGambar);
            //imageProfile.setImageResource();
//            jk.setText(user.getJk());


        if (SharedPrefManager.getInstance(getActivity()).isLoggedIn()){
            btn_login.setVisibility(View.GONE);
            btn_logout.setVisibility(View.VISIBLE);
            btn_ubah_profil.setVisibility(View.VISIBLE);
            btn_ubah_password.setVisibility(View.VISIBLE);
            btn_update_photo.setVisibility(View.VISIBLE);
            imageProfile.setClickable(true);
        }else {
            btn_logout.setVisibility(View.GONE);
            btn_ubah_profil.setVisibility(View.GONE);
            btn_ubah_password.setVisibility(View.GONE);
            btn_update_photo.setVisibility(View.GONE);
            btn_login.setVisibility(View.GONE);
            imageProfile.setImageResource(R.drawable.icon_profile_grey);
            imageProfile.setClickable(false);
        }

    }


    public void updateImage(){
        progressDialog.progress("Upload Profile Picture","Loading...");

            HashMap<String, String> params = new HashMap<>();
            params.put("id_member", user.getId());
            params.put("kode", user.getKode());
            //params.put("password2",picturePath);

            try {
                OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .build();


                AndroidNetworking.upload("https://jualanpraktis.net/android/update_photo.php")
                        .addMultipartParameter(params)
                        .addMultipartFile("files", new File(PicturePath))
                        .setTag(getActivity())
                        .setPriority(Priority.HIGH)
                        .setOkHttpClient(okHttpClient)
                        .build()
                        .setUploadProgressListener(new UploadProgressListener() {
                            @Override
                            public void onProgress(long bytesUploaded, long totalBytes) {

                            }
                        })
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                if (response.contains("Perubahan profile berhasil disimpan")) {
                                    Toast.makeText(getContext(), "Profile Picture has succesfully changed.", Toast.LENGTH_LONG).show();
//                                    user = SharedPrefManager.getInstance(getActivity()).getUser();
//                                    String kode = user.getKode().toString();
//                                    UrlImageViewHelper test = new UrlImageViewHelper();
//                                    test.setUrlDrawable(imageProfile, "https://trading.my.id/files/drp/" + kode + "/" + PicturePath);
//                            new AlertDialog.Builder(getContext())
//                                    .setTitle("Check Your Email")
//                                    .setMessage("We've send a message to the email provided with a link to active your account. This helps us verify your identity.")
//                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            KosongField();
//                                            finish();
//                                        }
//                                    }).show();
                                } else {
                                    Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
//                        progressDialog.dismiss();
                                Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }catch (NullPointerException e){
                //kalau kosong
                e.printStackTrace();
                Toast.makeText(getActivity(), "Upload dulu Imagenya, tekan di bagian gambar.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

    }


    @Override
    public void onClick(View v) {
        if (SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
//            DialogModify();
        }else {
            imageProfile.setImageResource(R.drawable.icon_profile_grey);
            Toast.makeText(getContext(), "Anda Belum Login", Toast.LENGTH_SHORT).show();
        }
    }

    private void DialogModify() {
        LayoutInflater inflater;
        MaterialAlertDialogBuilder dialog;
        View dialogView;

        dialog = new MaterialAlertDialogBuilder(getActivity());
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_ubah_akun, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle("Ubah Data");


        final EditText txtNama = (EditText) dialogView.findViewById(R.id.input_nama_update);
        final EditText txtEmail = (EditText) dialogView.findViewById(R.id.input_email_update);
        final EditText txtNotelp = (EditText) dialogView.findViewById(R.id.input_notelp_update);
        final EditText txtNamaToko = (EditText) dialogView.findViewById(R.id.input_namatoko_update);
        //final RadioGroup jenis_kelamin = dialogView.findViewById(R.id.RG);
//        final RadioButton laki = dialogView.findViewById(R.id.laki);
//        final RadioButton perempuan = dialogView.findViewById(R.id.perempuan);
        final Spinner spinner1 = dialogView.findViewById(R.id.spin_kota_update);
        final Spinner spinner2 = dialogView.findViewById(R.id.spin_kecamatan_update);
        final Spinner spin_provinsi = dialogView.findViewById(R.id.spin_provinsi_update);
        final EditText txtalamat = (EditText) dialogView.findViewById(R.id.input_alamat_update);
        final EditText txtnoktp = (EditText) dialogView.findViewById(R.id.input_no_ktp_update);
        final EditText txtnonpwp = (EditText) dialogView.findViewById(R.id.input_no_npwp_update);
        //final EditText input_password = dialogView.findViewById(R.id.input_password);
        final EditText txtatas_nama = (EditText) dialogView.findViewById(R.id.input_atas_nama_update);
        final EditText txtno_rek = (EditText) dialogView.findViewById(R.id.input_no_rek_update);
        final EditText txtnama_bank = (EditText) dialogView.findViewById(R.id.input_nama_bank_update);


//        if (!pref.getLoginMethod().equals("login")){
//            input_password.setVisibility(View.GONE);
//            input_password.setText("");
//        }

        txtEmail.setEnabled(false);
        final loginuser user = SharedPrefManager.getInstance(getActivity()).getUser();
        txtNama.setText(user.getNama());
        txtNamaToko.setText(user.getNama_toko());
        getProvinsi(spin_provinsi);
        selectedItem(spin_provinsi,spinner1,spinner2);
        txtalamat.setText(user.getAlamat());
        txtnoktp.setText(user.getNo_ktp());
        txtnonpwp.setText(user.getNo_npwp());
        txtNotelp.setText(user.getNo_hp());
        txtEmail.setText(user.getEmail());
        txtatas_nama.setText(user.getAtas_nama());
        txtno_rek.setText(user.getNo_rek());
        txtnama_bank.setText(user.getNama_bank());


        dialog.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                loginuser userr = new loginuser(
                        user.getId(),
                        user.getKode(),
                        txtNama.getText().toString(),
                        txtNamaToko.getText().toString(),
                        nama_provinsi,nama_kota,nama_wilayah,txtalamat.getText().toString(),
                        txtnoktp.getText().toString(),
                        txtnonpwp.getText().toString(),
                        txtNotelp.getText().toString(),
                        txtEmail.getText().toString(),
                        txtatas_nama.getText().toString(),
                        txtno_rek.getText().toString(),
                        txtnama_bank.getText().toString(),
                        user.getFoto()
                );
               SharedPrefManager.getInstance(getContext()).userLogin(userr);

                updateTest(txtNama.getText().toString(),txtEmail.getText().toString(),txtNotelp.getText().toString(), txtNamaToko.getText().toString(),
                        user.getId(), txtalamat.getText().toString(), txtnoktp.getText().toString(), txtnonpwp.getText().toString(),
                        txtatas_nama.getText().toString(), txtno_rek.getText().toString(), txtnama_bank.getText().toString());

            }
        });
        dialog.setNegativeButton("Batalkan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

//    private void update(final String Nama, final String Email,final String No_hp, final String NamaToko , final String ID,final String Alamat, final String noKtp, final
//                        String noNpwp, final String atasNama, final String noRek, final String namaBank){
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_Update, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    try{
//                        if (response.getString("message").equals("Perubahan profile berhasil disimpan")){
//                            user = SharedPrefManager.getInstance(getActivity()).getUser();
//                            //setting the values to the textview
//                            nama.setText(user.getNama());
//                            email.setText(user.getEmail());
//                            nohp.setText(user.getNo_hp());
//                            namatoko.setText(user.getNama_toko());
//                            String alamat_lengkap = user.getAlamat() +", Kecamatan "+user.getKecamatan()+", Kota/Kabupaten "+
//                                    user.getKota() +", Provinsi "+user.getProvinsi() +".";
//                            alamat.setText(alamat_lengkap);
//                            noktp.setText(user.getNo_ktp());
//                            nonpwp.setText(user.getNo_npwp());
//                            atasnama.setText(user.getAtas_nama());
//                            norek.setText(user.getNo_rek());
//                            namabank.setText(user.getNama_bank());
//
//                                Toast.makeText(getContext(), "Berhasil", Toast.LENGTH_SHORT).show();
//                            }else {
//                                Log.d("ResponseUpdate", response.toString());
//                                Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();
//                            }
//                    }catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e("Response", error.toString());
//                }
//            }){
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("id_member", ID);
//                    params.put("nama", Nama);
//                    params.put("nama_toko", NamaToko);
//                    params.put("provinsi", nama_provinsi);
//                    params.put("kota", nama_kota);
//                    params.put("kecamatan", nama_wilayah);
//                    params.put("alamat", Alamat);
//                    params.put("no_ktp", noKtp);
//                    params.put("no_npwp", noNpwp);
//                    params.put("no_hp", No_hp);
//                    params.put("email", Email);
//                    params.put("atas_nama", atasNama);
//                    params.put("no_rek", noRek);
//                    params.put("nama_bank", namaBank);
//
//                    return params;
//                }
//            };
//      VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
//    }

    private void updateTest(final String Nama, final String Email,final String No_hp, final String NamaToko , final String ID,final String Alamat, final String noKtp, final
    String noNpwp, final String atasNama, final String noRek, final String namaBank){

        Map<String, String> params = new HashMap<String, String>();
        params.put("id_member", ID);
        params.put("nama", Nama);
        params.put("nama_toko", NamaToko);
        params.put("provinsi", nama_provinsi);
        params.put("kota", nama_kota);
        params.put("kecamatan", nama_wilayah);
        params.put("alamat", Alamat);
        params.put("no_ktp", noKtp);
        params.put("no_npwp", noNpwp);
        params.put("no_hp", No_hp);
        params.put("email", Email);
        params.put("atas_nama", atasNama);
        params.put("no_rek", noRek);
        params.put("nama_bank", namaBank);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        Log.d("ParamsUpdate", params.toString());
        AndroidNetworking.post(URL_Update)
                .addBodyParameter(params)
                .setTag(getContext())
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.getString("message").equals("Perubahan profile berhasil disimpan")){
                                user = SharedPrefManager.getInstance(getActivity()).getUser();
                                //setting the values to the textview
                                nama.setText(user.getNama());
                                email.setText(user.getEmail());
                                nohp.setText(user.getNo_hp());
                                namatoko.setText(user.getNama_toko());
                                String alamat_lengkap = user.getAlamat() +", Kecamatan "+user.getKecamatan()+", Kota/Kabupaten "+
                                        user.getKota() +", Provinsi "+user.getProvinsi() +".";
                                alamat.setText(alamat_lengkap);
                                noktp.setText(user.getNo_ktp());
                                nonpwp.setText(user.getNo_npwp());
                                atasnama.setText(user.getAtas_nama());
                                norek.setText(user.getNo_rek());
                                namabank.setText(user.getNama_bank());

                                Toast.makeText(getContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                    }
                });
    }



    //provinsi,kecamatan,kota

    private void selectedItem(final Spinner spin_provinsi, final Spinner spinner1, final Spinner spinner2){
        spin_provinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Provinsi provinsi = provinsiList.get(spin_provinsi.getSelectedItemPosition());
                id_provinsi = provinsi.getProvince_id();
                nama_provinsi = provinsi.getProvince();
                if (id_provinsi != null) {
                    //spinnerData2(id_kota);
                    getKotaKabupaten(id_provinsi,spinner1);
                   // Log.d(TAG, id_provinsi);
                }else {
                   // Log.d(TAG,""+ id_provinsi);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //   id_kota = ((SObject1.Object1.Results) spinner1.getSelectedItem()).idKota;
                KotaKabupaten kotaKabupaten = kotaKabupatenList.get(spinner1.getSelectedItemPosition());
                id_kota = kotaKabupaten.getCity_id();
                nama_kota = kotaKabupaten.getCity_name();
                if (nama_kota != null) {
                    //spinnerData2(id_kota);
                    getKecamatan(id_kota,spinner2);
                  //  Log.d(TAG, id_kota);
                }else {
                   // Log.d(TAG,""+ id_kota);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               // Log.d(TAG, id_kota);
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Kecamatan kecamatan = kecamatanList.get(spinner2.getSelectedItemPosition());
                id_wilayah = kecamatan.getSubdistrict_id();
                nama_wilayah = kecamatan.getSubdistrict_name();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getProvinsi(final Spinner spin_provinsi){
        AndroidNetworking.get(BASE_URL + "province")
                .addHeaders("key", KEY)
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        provinsiList.clear();
                        try {
                            JSONObject object = response.getJSONObject("rajaongkir");
                            JSONArray array = object.getJSONArray("results");

                            for (int i = 0; i < array.length(); i++){
                                JSONObject obj = array.getJSONObject(i);
                                Provinsi item = new Provinsi();
                                item.setProvince_id(obj.getString("province_id"));
                                item.setProvince(obj.getString("province"));
                                provinsiList.add(item);
                            }


                            //   List<JenisPerangkat> list =respone.getJenisPerangkatArrayList();
                            spin_provinsi.setVisibility(View.VISIBLE);
                            ArrayAdapter<Provinsi> adapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, provinsiList);
                            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                            //  spinner.setPrompt("Jenis Perangkat : ");
                            spin_provinsi.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }
    private void getKotaKabupaten(String id_provinsi, final Spinner spinner1){
        AndroidNetworking.get(BASE_URL + "city?province="+id_provinsi)
                .addHeaders("key", KEY)
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        kotaKabupatenList.clear();
                        try {
                            JSONObject object = response.getJSONObject("rajaongkir");
                            JSONArray array = object.getJSONArray("results");

                            for (int i = 0; i < array.length(); i++){
                                JSONObject obj = array.getJSONObject(i);
                                KotaKabupaten item = new KotaKabupaten();
                                item.setCity_id(obj.getString("city_id"));
                                item.setCity_name(obj.getString("city_name"));
                                kotaKabupatenList.add(item);
                            }


                            //   List<JenisPerangkat> list =respone.getJenisPerangkatArrayList();
                            spinner1.setVisibility(View.VISIBLE);
                            ArrayAdapter<KotaKabupaten> adapter = new ArrayAdapter<KotaKabupaten>(getActivity(), R.layout.support_simple_spinner_dropdown_item, kotaKabupatenList);
                            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                            //  spinner.setPrompt("Jenis Perangkat : ");
                            spinner1.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }
    private void getKecamatan(String idkota,final Spinner spinner2){
        AndroidNetworking.get(BASE_URL + "subdistrict?city="+idkota)
                .addHeaders("key", KEY)
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        kecamatanList.clear();
                        try {
                            JSONObject object = response.getJSONObject("rajaongkir");
                            JSONArray array = object.getJSONArray("results");

                            for (int i = 0; i < array.length(); i++){
                                JSONObject obj = array.getJSONObject(i);
                                Kecamatan item = new Kecamatan();
                                item.setSubdistrict_id(obj.getString("subdistrict_id"));
                                item.setSubdistrict_name(obj.getString("subdistrict_name"));
                                kecamatanList.add(item);
                            }


                            //   List<JenisPerangkat> list =respone.getJenisPerangkatArrayList();
                            spinner2.setVisibility(View.VISIBLE);
                            ArrayAdapter<Kecamatan> adapter = new ArrayAdapter<Kecamatan>(getActivity()
                                    , R.layout.support_simple_spinner_dropdown_item, kecamatanList);
                            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                            //  spinner.setPrompt("Jenis Perangkat : ");
                            spinner2.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    void ubahPassword(String passwordLama,String passwordBaru){
        progressDialog.progress("Ubah Password","Loading...");

        HashMap<String,String> params = new HashMap<>();
        params.put("id_member",user.getId());
        params.put("password1",passwordLama);
        params.put("password2",passwordBaru);

        AndroidNetworking.post("https://jualanpraktis.net/android/update_password.php")
                .addBodyParameter(params)
                .setTag(getActivity())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();

                        try {
                            Toast.makeText(getContext(),response.getString("response"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),"Gagal Ubah Password, Silahkan coba lagi",Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
