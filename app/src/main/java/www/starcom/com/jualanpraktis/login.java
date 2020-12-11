package www.starcom.com.jualanpraktis;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import www.starcom.com.jualanpraktis.Login.Pref;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.VolleySingleton;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.feature.akun.AkunFragment;
import www.starcom.com.jualanpraktis.utils.CustomProgressDialog;

/**
 * Created by ADMIN on 12/02/2018.
 */

public class login extends AppCompatActivity implements View.OnClickListener {

    private static final String URL_LOGIN = "https://jualanpraktis.net/android/login.php";
    private final String TAG = this.getClass().getName();

    private Button btn_masuk,btn_daftar,btn_coorperate;
    private ImageView coba;
    private EditText loginEmail,loginPass;
    private TextView txt_lupa_password;
    ProgressBar progressBar;
    String username, password;

    private AkunFragment AkunFragment;
    ProgressDialog progressDialog;
    private TextView term_and_condition,kebijakan_privasi;


    //google
    GoogleSignInClient googleSignInClient;
    private int RC_SIGN_IN = 1;

    //facebook
    CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private ImageView btnFacebook,btnGoogleLogin;

    Pref pref;
    CustomProgressDialog progress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog =new ProgressDialog(login.this);
        progress = new CustomProgressDialog(login.this);
        AndroidNetworking.initialize(getApplicationContext());
        pref = new Pref(getApplicationContext());
        //FirebaseApp.initializeApp(getApplicationContext());
        //facebook
       // callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBar);
        loginEmail = findViewById(R.id.login_email);
        loginPass = findViewById(R.id.login_pass);
        txt_lupa_password = findViewById(R.id.txt_lupa_password);
        term_and_condition = findViewById(R.id.term_and_condition);
        kebijakan_privasi = findViewById(R.id.kebijakan_privasi);
        btn_masuk = findViewById(R.id.btn_masuk);
        btn_daftar = findViewById(R.id.btn_daftar);
        btn_coorperate = findViewById(R.id.btn_coorperate);
        coba = findViewById(R.id.coba);
        coba.setVisibility(View.GONE);

        txt_lupa_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(login.this);
                alertdialog.setTitle("Masukan Email yang terdaftar");

                final EditText edt_email = new EditText(login.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                edt_email.setLayoutParams(lp);
                edt_email.setPadding(16,16,16,16);
                alertdialog.setView(edt_email);
                alertdialog.setIcon(R.drawable.ic_baseline_email_24);

                alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!edt_email.getText().toString().equals("")){
                            postLupaPassword(edt_email.getText().toString());
                        }else {
                            Toast.makeText(login.this,"Masukan Email",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                alertdialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertdialog.show();
            }
        });

        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = loginEmail.getText().toString();
                password = loginPass.getText().toString();

                //validating inputs
                if (TextUtils.isEmpty(username)) {
                    loginEmail.setError("Email Belum Di isi");
                    loginEmail.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    loginPass.setError("Password Belum Di isi");
                    loginPass.requestFocus();
                } else{
                    userLogin(username,password);
                }
            }
        });


        btn_daftar.setOnClickListener(this);
        //google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // .requestIdToken()
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

    //   SignInButton btnGoogle = findViewById(R.id.btnGoogle);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
      //  btnGoogle.setSize(SignInButton.SIZE_STANDARD);
        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // signIn();
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        //facebook
        callbackManager = CallbackManager.Factory.create();
        btnFacebook = findViewById(R.id.btnFacebook);
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(login.this, Arrays.asList("email","public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        progressDialog.setTitle("Login Facebook");
                        progressDialog.setMessage("Loading...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                        Toast.makeText(getApplicationContext(),"ke cancel",Toast.LENGTH_LONG).show();
                        // ...
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);

                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                        // ...
                    }
                });


            }
        });


        if(LoginManager.getInstance()!=null){
          //  LoginManager.getInstance().logOut();
        }


        term_and_condition.setPaintFlags(term_and_condition.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        kebijakan_privasi.setPaintFlags(kebijakan_privasi.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        term_and_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this,WebLawActivity.class);
                intent.putExtra("title","Term and Contitions");
                intent.putExtra("url","file:///android_asset/term_and_condition.html");
                startActivity(intent);
            }
        });
        kebijakan_privasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this,WebLawActivity.class);
                intent.putExtra("title","Kebijakan Privasi");
                intent.putExtra("url","file:///android_asset/kebijakan_privasi.html");
                startActivity(intent);

            }
        });




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Daftar
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(login.this,daftar.class);
        startActivity(intent);
        //getActivity().finish();
    }

    private void userLogin(final String username, final String password) {
        progressBar.setVisibility(View.VISIBLE);

        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pref.setLoginMethod("login");
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("response");
                            //JSONObject userJson = obj.getJSONObject("login");

                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String getObject = jsonObject.toString();
                                JSONObject object = new JSONObject(getObject);

                                if (object.getString("status").equals("1")){
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
                                            object.getString("atas_nama"),
                                            object.getString("no_rek"),
                                            object.getString("nama_bank"),
                                            object.getString("foto")

//                                            object.getString("jk"),
//

//
//                                            object.getString("nama_provinsi"),
//                                            object.getString("nama_kota"),
//                                            object.getString("nama_kecamatan")

                                    );
                                    //    progressBar.  setVisibility(View.GONE);
                                    progressDialog.dismiss();
                                    //storing the user in shared preferences
                                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                    startActivity(new Intent(login.this,MainActivity.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    finish();
                                    Toast.makeText(login.this, "Berhasil Login", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(login.this, "Harap lakukan verifikasi email terlebih dahulu.", Toast.LENGTH_SHORT).show();
                                }


                            }
                                //starting the profile activity


                        } catch (JSONException e) {
                            Toast.makeText(login.this, "Gagal Login", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(login.this, "Gagal Login", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                     //   progressDialog.dismiss();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", username);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    //facebook
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String id = user.getUid();
                            String nama = user.getDisplayName();
                            String email = user.getEmail();
                            String no_hp = user.getPhoneNumber();
                            if (no_hp==null){
                                no_hp = "";
                            }
                            Uri profile = user.getPhotoUrl();


                          if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                              new AlertDialog.Builder(login.this)
                                      .setTitle("Perhatian")
                                      .setMessage("Email facebook anda tidak terdeteksi, silahkan daftar biasa atau masukkan email anda pada akun facebook anda.")
                                      .setPositiveButton("Daftar", new DialogInterface.OnClickListener() {
                                          @Override
                                          public void onClick(DialogInterface dialog, int which) {
                                              Intent intent = new Intent(login.this,daftar.class);
                                              startActivity(intent);
                                          }
                                      })
                                      .setNegativeButton("Nanti", new DialogInterface.OnClickListener() {
                                          @Override
                                          public void onClick(DialogInterface dialog, int which) {
                                              progressDialog.dismiss();
                                          }
                                      }).show();
                          }else {
                              loginGoogleFb("facebook",id,email,"",user.getDisplayName(),"");
                          }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    //google
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        progressDialog.setTitle("Login Google");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            googleUserProfile(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            progressDialog.dismiss();
            // updateUI(null);
        }
    }
    void googleUserProfile(GoogleSignInAccount acct){
        acct = GoogleSignIn.getLastSignedInAccount(login.this);
        if (acct != null) {

            Uri profile = acct.getPhotoUrl();

            loginGoogleFb("google",acct.getId(),acct.getEmail(),"",acct.getGivenName(),acct.getFamilyName());
          //  loginGoogleFacebook(acct.getEmail(),"",acct.getDisplayName(),"");
        }else{
            progressDialog.dismiss();
         //   StyleableToast.makeText(getApplicationContext(), "Gagal Login melalui Google", Toast.LENGTH_LONG, R.style.error_toast).show();
        }
    }

    private void loginGoogleFacebook(final String username, final String password,final String nama, final String no_hp){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pref.setLoginMethod("fbgoogle");
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("response");
                            //JSONObject userJson = obj.getJSONObject("login");


                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String getObject = jsonObject.toString();
                                JSONObject object = new JSONObject(getObject);
                                loginuser user = new loginuser(
                                        object.getString("id_member"),
                                        object.getString("kode"),
                                        object.getString("nama"),
                                        object.getString("nama_toko"),
                                        object.getString("alamat"),
                                        object.getString("provinsi"),
                                        object.getString("kota"),
                                        object.getString("kecamatan"),
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
                           //     progressBar.setVisibility(View.GONE);
                                progressDialog.dismiss();
                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                finish();
                                Toast.makeText(login.this, "Berhasil Login", Toast.LENGTH_SHORT).show();


                                /**
                                Intent intent = new Intent(login.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();**/
                            }
                            //starting the profile activity


                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Toast.makeText(login.this, "Gagal Login", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                     //   Toast.makeText(login.this, "Gagal Login", Toast.LENGTH_SHORT).show();
                      //  progressBar.setVisibility(View.GONE);
                       // progressDialog.dismiss();
                        daftarGoogleFacebook(username,password,nama,no_hp);



                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", username);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
    private void daftarGoogleFacebook(final String email, final String password,final String nama, final String no_hp){

        final HashMap<String,String> postData = new HashMap<>();
        postData.put("nama",nama);
        postData.put("email",email);
        postData.put("no_hp",no_hp);
        postData.put("jk","");
        postData.put("alamat","");
        postData.put("provinsi","");
        postData.put("kota","");
        postData.put("kecamatan","");
        postData.put("nama_provinsi","");
        postData.put("nama_kota","");
        postData.put("nama_kecamatan","");
        postData.put("password",password);

        PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
              //  progressDialog.dismiss();
                if (s.contains("Data Berhasil Di Kirim")){
                    loginGoogleFacebook(email,password,nama,no_hp);
                }
            }
        });

        task.execute("https://jualanpraktis.net/android/daftar.php");
        task.setEachExceptionsHandler(new EachExceptionsHandler() {
            @Override
            public void handleIOException(IOException e) {
                Log.d(TAG,e.toString());
            }

            @Override
            public void handleMalformedURLException(MalformedURLException e) {
                Log.d(TAG,e.toString());
            }

            @Override
            public void handleProtocolException(ProtocolException e) {
                Log.d(TAG,e.toString());
            }

            @Override
            public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                Log.d(TAG,e.toString());
            }
        });

    }
    private void loginGoogleFb(final String oauthpro,final String oauthid,String email,String gender,String first_name,String last_name){
        HashMap<String,String> param = new HashMap<>();
        param.put("oauthpro",oauthpro);
        param.put("oauthid",oauthid);
        param.put("email",email);
        param.put("gender",gender);
        param.put("first_name",first_name);
        param.put("last_name",last_name);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post("https://jualanpraktis.net/android/login-google.php")
                .addBodyParameter(param)
                .setTag(login.this)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("data");
                            JSONObject object = array.getJSONObject(0);
                            loginuser user = new loginuser(

                                    object.getString("id_member"),
                                    object.getString("kode"),
                                    object.getString("nama"),
                                    object.getString("nama_toko"),
                                    object.getString("alamat"),
                                    object.getString("provinsi"),
                                    object.getString("kota"),
                                    object.getString("kecamatan"),
                                    object.getString("no_ktp"),
                                    object.getString("no_npwp"),
                                    object.getString("no_hp"),
                                    object.getString("email"),


                                    object.getString("nama_bank"),
                                    object.getString("atas_nama"),
                                    object.getString("no_rek"),

                                    object.getString("foto")

//                                    object.getString("id_customer"),
//                                    object.getString("nama"),
//                                    object.getString("email"),
//                                    object.getString("no_hp"),
//                                    object.getString("jk"),
//                                    object.getString("alamat"),
//
//
//                                    object.getString("nama_provinsi"),
//                                    object.getString("nama_kota"),
//                                    object.getString("nama_kecamatan")
                            );
                            //     progressBar.setVisibility(View.GONE);
                            progressDialog.dismiss();
                            //storing the user in shared preferences
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                            finish();
                            Toast.makeText(login.this, "Berhasil Login", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(login.this, "Login Gagal.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")){
                                Toast.makeText(login.this, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(login.this, "Login Gagal.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

   // @Override
//   public void dialogLoginCoorperate(View v){
//        AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(login.this);
//        View layoutView = getLayoutInflater().inflate(R.layout.dialog_login_coorperate, null);
//
//        final EditText edt_no_induk = layoutView.findViewById(R.id.edt_no_induk);
//        final EditText edt_password = layoutView.findViewById(R.id.login_pass);
//        final Button btn_masuk = layoutView.findViewById(R.id.btn_masuk);
//        final ImageView ic_close = layoutView.findViewById(R.id.ic_close);
//
//        dialogBuilder.setView(layoutView);
//
//        AlertDialog alertDialog = dialogBuilder.create();
//        alertDialog.setCancelable(false);
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alertDialog.show();
//
//        ic_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//            }
//        });
//
//        btn_masuk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loginCoorperate(edt_no_induk.getText().toString(), edt_password.getText().toString());
//                alertDialog.dismiss();
//            }
//        });
//
//    }

//    void loginCoorperate(String nik,String password){
//        progressDialog.setTitle("Login");
//        progressDialog.setMessage("Loading...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//
//        AndroidNetworking.post("https://batammall.co.id/ANDROID/login-corporate.php")
//                .addBodyParameter("nik",nik)
//                .addBodyParameter("password",password)
//                .setTag(login.this)
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        progressDialog.dismiss();
//                        try {
//                            JSONArray jsonArray = response.getJSONArray("response");
//                            //JSONObject userJson = obj.getJSONObject("login");
//
//                         //   for(int i=0;i<jsonArray.length();i++) {
//                                JSONObject object = jsonArray.getJSONObject(0);
//                            //    String getObject = jsonObject.toString();
//                            //    JSONObject object = new JSONObject(getObject);
//
//                                if (object.getString("status_member").equals("1")){
//                                    loginuser user = new loginuser(
//                                            object.getString("id_customer"),
//                                            object.getString("nama"),
//                                            object.getString("email"),
//                                            object.getString("no_hp"),
//                                            object.getString("jk"),
//                                            object.getString("alamat"),
//
//                                            object.getString("provinsi"),
//                                            object.getString("kota"),
//                                            object.getString("kecamatan"),
//
//                                            object.getString("nama_provinsi"),
//                                            object.getString("nama_kota"),
//                                            object.getString("nama_kecamatan")
//                                    );
//                                    //    progressBar.setVisibility(View.GONE);
//                                    progressDialog.dismiss();
//                                    //storing the user in shared preferences
//                                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
//                                    pref.setLoginMethod("coorperate");
//                                    pref.setLimitBelanja(object.getString("limit_belanja"));
//                                    pref.setNik(object.getString("nik"));
//
//                                    startActivity(new Intent(getApplicationContext(),MainActivity.class)
//                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                                    finish();
//                                    Toast.makeText(login.this, "Berhasil Login", Toast.LENGTH_SHORT).show();
//
//                                }else{
//                                    new AlertDialog.Builder(login.this).setTitle("Perhatian")
//                                            .setMessage("Akun anda telah di suspend silahkan hubungi admin")
//                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//
//                                                }
//                                            }).show();
//
//                                }
//
//
//                          //  }
//
//                        } catch (JSONException e) {
//                            Toast.makeText(login.this, "Gagal Login", Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.GONE);
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        progressDialog.dismiss();
//
//                        Toast.makeText(getApplicationContext(),"Gagal Login",Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//    }

    void postLupaPassword(String email){
        progress.progress("Mengirim Email","Loading...");
        AndroidNetworking.post("https://jualanpraktis.net/android/lupa_password.php")
                .addBodyParameter("email",email)
                .setPriority(Priority.MEDIUM)
                .setTag(login.this)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progress.dismiss();

                        new AlertDialog.Builder(login.this)
                                .setTitle("Berhasil Mengirim ke Email")
                                .setMessage("Silahkan cek email anda untuk melanjutkan proses dari lupa password.")
                                .setIcon(R.drawable.ic_baseline_email_24)
                                .setPositiveButton("Selesai", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        progress.dismiss();
                        Toast.makeText(login.this,"Gagal Mengirim ke Email, Silahkan coba lagi",Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
