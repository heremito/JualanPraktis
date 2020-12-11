package www.starcom.com.jualanpraktis.feature.akun;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.facebook.login.LoginManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.HashMap;

import www.starcom.com.jualanpraktis.Login.Pref;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.databinding.FragmentAkunCoorperateBinding;


public class AkunCoorperateFragment extends Fragment {

    FragmentAkunCoorperateBinding binding;
    AkunProcess process;

    loginuser user ;
    Pref pref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = binding.inflate(inflater,container,false);
        process = new AkunProcess(binding,getActivity());
        AndroidNetworking.initialize(getActivity().getApplicationContext());


        initView();
        klik();

        return binding.getRoot();
    }

    private void initView() {
        user = process.user;
        pref = process.pref;

        //binding.txtLimit.setText(FormatText.rupiahFormat(Double.parseDouble(pref.getLimitBelanja())));
        process.getLimit();
        binding.txtNoInduk.setText(pref.getNik());
        binding.nama.setText(user.getNama());
        binding.email.setText(user.getEmail());
        binding.nohp.setText(user.getNo_hp());
        //binding.jk.setText(user.getJk());
     /*   String alamat_lengkap = user.getAlamat() +", Kecamatan "+user.getNamaKecamatan()+", Kota/Kabupaten "+
                user.getNamaKota() +", Provinsi "+user.getNamaProvinsi() +".";
        if (user.getNamaProvinsi().equals("-")){
            alamat_lengkap = "-";
        }else if (user.getNamaProvinsi()==null){
            alamat_lengkap = "-";
        }else if (user.getNamaProvinsi().equals("null")){
            alamat_lengkap = "-";
        }else if (user.getNamaProvinsi().equals("")){
            alamat_lengkap = "-";
        } */
        binding.alamat.setText(user.getAlamat());
    }
    private void klik() {
        binding.btnUbahProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUbahProfile(v);
            }
        });
        binding.btnUbahSandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUbahPassword(v);
            }
        });
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
                    SharedPrefManager.getInstance(getContext()).logout();
                    pref.setLoginMethod("");
                    LoginManager.getInstance().logOut();

                   /* Intent intent = new Intent(getActivity(), login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish(); */
                    getActivity().finish();
                    getActivity().overridePendingTransition(0, 0);
                    getActivity().startActivity(getActivity().getIntent().setFlags(getActivity().getIntent().FLAG_ACTIVITY_NO_ANIMATION));
                    getActivity().overridePendingTransition(0, 0);
                    Toast.makeText(getContext(), "Anda Telah Logout", Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(getContext(), "Anda Belum Login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        process.getLimit();
    }

    public void dialogUbahPassword(View v){
        AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
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
                process.ubahPassword(edt_pass_lama.getText().toString(), edt_pass_baru.getText().toString());
                alertDialog.dismiss();
            }
        });

    }

    public void dialogUbahProfile(View v){
        LayoutInflater inflater;
        MaterialAlertDialogBuilder dialog;
        View dialogView;

        dialog = new MaterialAlertDialogBuilder(getActivity());
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_ubah_akun_coorperate, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle("Ubah Data");


        final EditText txtNama = (EditText) dialogView.findViewById(R.id.input_nama);
        final EditText txtEmail = (EditText) dialogView.findViewById(R.id.input_email);
        final EditText txtNotelp = (EditText) dialogView.findViewById(R.id.input_notelp);
        final RadioGroup jenis_kelamin = dialogView.findViewById(R.id.RG);
        final RadioButton laki = dialogView.findViewById(R.id.laki);
        final RadioButton perempuan = dialogView.findViewById(R.id.perempuan);
        final Spinner spinner1 = dialogView.findViewById(R.id.spin_kota);
        final Spinner spinner2 = dialogView.findViewById(R.id.spin_kecamatan);
        final Spinner spin_provinsi = dialogView.findViewById(R.id.spin_provinsi);
        final EditText input_alamat = dialogView.findViewById(R.id.input_alamat);
        final EditText input_password = dialogView.findViewById(R.id.input_password);

        //if (!pref.getLoginMethod().equals("login")){
        input_password.setVisibility(View.GONE);
        spinner1.setVisibility(View.GONE);
        spinner2.setVisibility(View.GONE);
        spin_provinsi.setVisibility(View.GONE);
            input_password.setText("");


        txtEmail.setEnabled(false);
        final loginuser user = SharedPrefManager.getInstance(getActivity()).getUser();
        txtNama.setText(user.getNama());
        txtEmail.setText(user.getEmail());
        txtNotelp.setText(user.getNo_hp());
        input_alamat.setText(user.getAlamat());

       // getProvinsi(spin_provinsi);
       // selectedItem(spin_provinsi,spinner1,spinner2);


        dialog.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String jk = "" ;
                if (jenis_kelamin.getCheckedRadioButtonId()!= 0 ) {
                    if (jenis_kelamin.getCheckedRadioButtonId() == laki.getId()) {
                        jk = "Laki-Laki";
                    } else if (jenis_kelamin.getCheckedRadioButtonId() == perempuan.getId()) {
                        jk = "Perempuan";
                    }
                }

                HashMap<String,String> params = new HashMap<>();
                params.put("id_customer",user.getId());
                params.put("nama",txtNama.getText().toString());
                params.put("jk",jk);
                params.put("no_hp",txtNotelp.getText().toString());
                params.put("email",txtEmail.getText().toString());
                params.put("alamat",input_alamat.getText().toString());

                /**
                loginuser userr = new loginuser(
                        user.getId(),
                        txtNama.getText().toString(),
                        txtEmail.getText().toString(),
                        txtNotelp.getText().toString(),
                        jk,
                        input_alamat.getText().toString(),"","","","","",""
                );
                SharedPrefManager.getInstance(getContext()).userLogin(userr);**/

                process.ubahProfile(params);
                dialog.dismiss();
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
}