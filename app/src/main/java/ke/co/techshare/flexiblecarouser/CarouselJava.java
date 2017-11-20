package ke.co.techshare.flexiblecarouser;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.view.ContextThemeWrapper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by USER on 11/11/2017.
 */

public class CarouselJava extends RelativeLayout {

    private ViewPager _view_pager;
    private CarouselAdapter _cAdapter;
    private int current_position=0;
    private boolean is_cyclic=true;
    private int delay_time = 2000;
    private CarouselJava base;
    private DotsIndicator indicators;
    private ImageView right,left;
    private ViewPager.PageTransformer[] transformers={new BackgroundToForegroundTransformer(),new ZoomOutSlideTransformer(),new DepthPageTransformer()};

    public CarouselJava(Context context) {
        super(context);
        setUpLayout(context,null);
    }

    public CarouselJava(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpLayout(context,attrs);

    }

    public CarouselJava(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpLayout(context,attrs);

    }

    public void setUpLayout(Context context,AttributeSet attributeSet){

        base=this;
        _view_pager=new ViewPager(context);
        this.addView(_view_pager);

        //setup the indicators
        RelativeLayout.LayoutParams indicator_params=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        indicator_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        indicator_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        indicators=new DotsIndicator(context);
        this.addView(indicators,indicator_params);


        //add the right button
        right=new ImageView(context);
        right.setImageResource(R.drawable.ic_action_right);
        RelativeLayout.LayoutParams right_params=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        right_params.addRule(RelativeLayout.CENTER_VERTICAL);
        right_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNext(false);
            }
        });

        this.addView(right,right_params);

        //Left button
        //add the right button
        left=new ImageView(context);
        left.setImageResource(R.drawable.ic_action_left);
        RelativeLayout.LayoutParams left_params=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        left_params.addRule(RelativeLayout.CENTER_VERTICAL);
        left_params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToPrevious(false);
            }
        });

        this.addView(left,left_params);


        autoload();

    }



    public void autoload(){
        int index=new Random().nextInt(2 - 0 + 1) + 0;
        _view_pager.setPageTransformer(true,transformers[index]);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                base.moveToNext(true);
            }
        },delay_time);

    }

    public void initCarousel(CarouselAdapter c,int delay_time_interval){
        _cAdapter=c;
        this.delay_time=delay_time_interval;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            _view_pager.setId(View.generateViewId());
        }else{
            _view_pager.setId(new Random().nextInt());
        }
        _view_pager.setAdapter(_cAdapter);
        indicators.setViewPager(_view_pager);

        _view_pager.setPageTransformer(true,new ZoomOutSlideTransformer());
    }

    public int getCurrent_position(){
        return current_position;
    }

    public void moveToNext(boolean restart){
        if(_cAdapter!=null){
            if(current_position<_cAdapter.getCount()){
                current_position++;
                _view_pager.setCurrentItem(current_position,true);
                if(restart)autoload();
            }else{
                if(is_cyclic){
                    current_position=0;
                    _view_pager.setCurrentItem(current_position,true);
                    if(restart)autoload();
                }
            }
        }else{
            Log.e("Carousel","attatch a CarouselAdapter to your Carousel");
        }
    }

    public void moveToPrevious(boolean restart){
        if(_cAdapter!=null){
            if(current_position>0){
                current_position--;
                _view_pager.setCurrentItem(current_position,true);
                if(restart)autoload();
            }else{
                if(is_cyclic){
                    current_position=0;
                    _view_pager.setCurrentItem(current_position,true);
                    if(restart)autoload();
                }
            }
        }else{
            Log.e("Carousel","attatch a CarouselAdapter to your Carousel");
        }
    }

    public static class CarouselAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public CarouselAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    public static class CarouselPage extends Fragment {

        private int image=0;
        ImageView c;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            // create ContextThemeWrapper from the original Activity Context with the custom theme
            final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme);
            // clone the inflater using the ContextThemeWrapper
            LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
            View view = localInflater.inflate(R.layout.carousel_page, container, false);

            c=view.findViewById(R.id.carousel_image);

            return view;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            Bundle g=getArguments();
            if(g!=null){
                image=g.getInt("image");
            }
            try{
                c.setImageResource(image);
            }catch (Exception e){

            }
        }

    }

    //The code below are transformers. Only modify gently
    public abstract class BaseTransformer implements ViewPager.PageTransformer {

        /**
         * Called each {@link #transformPage(android.view.View, float)}.
         *
         * @param view
         * @param position
         */
        protected abstract void onTransform(View view, float position);

        @Override
        public void transformPage(View view, float position) {
            onPreTransform(view, position);
            onTransform(view, position);
            onPostTransform(view, position);
        }

        /**
         * If the position offset of a fragment is less than negative one or greater than one, returning true will set the
         * visibility of the fragment to {@link android.view.View#GONE}. Returning false will force the fragment to {@link android.view.View#VISIBLE}.
         *
         * @return
         */
        protected boolean hideOffscreenPages() {
            return true;
        }

        /**
         * Indicates if the default animations of the view pager should be used.
         *
         * @return
         */
        protected boolean isPagingEnabled() {
            return false;
        }

        /**
         * Called each {@link #transformPage(android.view.View, float)} before {{@link #onTransform(android.view.View, float)} is called.
         *
         * @param view
         * @param position
         */
        protected void onPreTransform(View view, float position) {
            final float width = view.getWidth();

            view.setRotationX(0);
            view.setRotationY(0);
            view.setRotation(0);
            view.setScaleX(1);
            view.setScaleY(1);
            view.setPivotX(0);
            view.setPivotY(0);
            view.setTranslationY(0);
            view.setTranslationX(isPagingEnabled() ? 0f : -width * position);

            if (hideOffscreenPages()) {
                view.setAlpha(position <= -1f || position >= 1f ? 0f : 1f);
            } else {
                view.setAlpha(1f);
            }
        }

        /**
         * Called each {@link #transformPage(android.view.View, float)} call after {@link #onTransform(android.view.View, float)} is finished.
         *
         * @param view
         * @param position
         */
        protected void onPostTransform(View view, float position) {
        }

    }
    public class BackgroundToForegroundTransformer extends BaseTransformer {

        @Override
        protected void onTransform(View view, float position) {
            final float height = view.getHeight();
            final float width = view.getWidth();
            final float scale = min(position < 0 ? 1f : Math.abs(1f - position), 0.5f);

            view.setScaleX(scale);
            view.setScaleY(scale);
            view.setPivotX(width * 0.5f);
            view.setPivotY(height * 0.5f);
            view.setTranslationX(position < 0 ? width * position : -width * position * 0.25f);
        }

        private  final float min(float val, float min) {
            return val < min ? min : val;
        }

    }
    public class ZoomOutSlideTransformer extends BaseTransformer {

        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        @Override
        protected void onTransform(View view, float position) {
            if (position >= -1 || position <= 1) {
                // Modify the default slide transition to shrink the page as well
                final float height = view.getHeight();
                final float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                final float vertMargin = height * (1 - scaleFactor) / 2;
                final float horzMargin = view.getWidth() * (1 - scaleFactor) / 2;

                // Center vertically
                view.setPivotY(0.5f * height);

                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            }
        }

    }
    public class DepthPageTransformer extends BaseTransformer {

        private static final float MIN_SCALE = 0.75f;

        @Override
        protected void onTransform(View view, float position) {
            if (position <= 0f) {
                view.setTranslationX(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);
            } else if (position <= 1f) {
                final float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setAlpha(1 - position);
                view.setPivotY(0.5f * view.getHeight());
                view.setTranslationX(view.getWidth() * -position);
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            }
        }

        @Override
        protected boolean isPagingEnabled() {
            return true;
        }

    }
}
