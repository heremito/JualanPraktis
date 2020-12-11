package www.starcom.com.jualanpraktis.Kategori;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import www.starcom.com.jualanpraktis.R;

/**
 * Created by ADMIN on 09/02/2018.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context ;
    private LayoutInflater layoutInflater ;
    private List<SliderUtils> sliderimg ;
    private ImageLoader imageLoader ;

    public ViewPagerAdapter(List<SliderUtils> sliderimg, Context context) {
        this.sliderimg = sliderimg;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sliderimg.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        if (layoutInflater != null) {
            view = layoutInflater.inflate(R.layout.layout_slide_home, null);


            SliderUtils utils = sliderimg.get(position);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            ImageView imageView = view.findViewById(R.id.imageView);

            imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
            imageLoader.get(utils.getSliderImageUrl(), ImageLoader.getImageListener(imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
        }
        ViewPager vp = (ViewPager) container ;
        vp.addView(view,0);
        return view;
    }
}
