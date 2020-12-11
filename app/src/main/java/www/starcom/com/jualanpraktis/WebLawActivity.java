package www.starcom.com.jualanpraktis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import www.starcom.com.jualanpraktis.databinding.ActivityWebLawBinding;

public class WebLawActivity extends AppCompatActivity {

    String title,url;
    ActivityWebLawBinding binding;
    Activity activity = WebLawActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_web_law);
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_web_law);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            title = bundle.getString("title");
            url = bundle.getString("url");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

        binding.webView.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
