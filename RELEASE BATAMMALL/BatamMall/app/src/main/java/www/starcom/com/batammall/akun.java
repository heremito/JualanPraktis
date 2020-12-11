package www.starcom.com.jualanpraktis;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.VolleySingleton;
import www.starcom.com.jualanpraktis.Login.loginuser;

/**
 * Created by ADMIN on 05/02/2018.
 */

public class akun extends Fragment implements View.OnClickListener{
    private TextView btn_logout,btn_login ;
    private TextView nama,email,nohp,jk,time;
    ImageView edit ;
    loginuser user;

    public akun() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_akun,container,false);

        nama = rootView.findViewById(R.id.nama);
        email = rootView.findViewById(R.id.email);
        nohp = rootView.findViewById(R.id.nohp);
        jk = rootView.findViewById(R.id.jk);
        time = rootView.findViewById(R.id.time);
        edit = rootView.findViewById(R.id.edit);
        edit.setOnClickListener(this);

        btn_login = rootView.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),login.class);
                startActivity(intent);
            }
        });
        btn_logout = rootView.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
                    SharedPrefManager.getInstance(getContext()).logout();
                    nama.setText("");
                    email.setText("");
                    nohp.setText("");
                    jk.setText("");
                }else {
                    Toast.makeText(getContext(), "Anda Belum Login", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
            //getting the current user
            user = SharedPrefManager.getInstance(getActivity()).getUser();
            //setting the values to the textviews
            nama.setText(user.getNama());
            email.setText(user.getEmail());
            nohp.setText(user.getNo_hp());
            jk.setText(user.getJk());
    }


    @Override
    public void onClick(View v) {
        if (SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
            DialogModify();
        }else {
            Toast.makeText(getContext(), "Anda Belum Login", Toast.LENGTH_SHORT).show();
        }
    }

    private void DialogModify() {
        LayoutInflater inflater;
        AlertDialog.Builder dialog;
        View dialogView;

        dialog = new AlertDialog.Builder(getActivity());
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_ubah_akun, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle("Ubah Data");


        final TextView txtNama = (EditText) dialogView.findViewById(R.id.input_nama);
        final TextView txtEmail = (EditText) dialogView.findViewById(R.id.input_email);
        final TextView txtNotelp = (EditText) dialogView.findViewById(R.id.input_notelp);

        final loginuser user = SharedPrefManager.getInstance(getActivity()).getUser();
        txtNama.setText(user.getNama());
        txtEmail.setText(user.getEmail());
        txtNotelp.setText(user.getNo_hp());

        dialog.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                update(txtNama.getText().toString(),txtNotelp.getText().toString(),txtEmail.getText().toString(),
                        user.getId());
                loginuser userr = new loginuser(
                        user.getId(),
                        txtNama.getText().toString(),
                        txtEmail.getText().toString(),
                        txtNotelp.getText().toString(),
                        user.getJk()
                );
                SharedPrefManager.getInstance(getContext()).userLogin(userr);

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

    private void update(final String Nama, final String No_hp, final String Email, final int ID){
        StringRequest request = new StringRequest(Request.Method.POST, "https://batammall.co.id/ANDROID/update_akun.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Data Berhasil Di Kirim")){
                    user = SharedPrefManager.getInstance(getActivity()).getUser();
                    //setting the values to the textviews
                    nama.setText(user.getNama());
                    email.setText(user.getEmail());
                    nohp.setText(user.getNo_hp());
                    jk.setText(user.getJk());

                    Toast.makeText(getContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("nama",Nama);
                params.put("no_hp",No_hp);
                params.put("email",Email);
                params.put("id_customer",Integer.toString(ID));
                return params ;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }


}
