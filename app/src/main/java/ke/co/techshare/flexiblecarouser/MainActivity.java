package ke.co.techshare.flexiblecarouser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private CarouselJava carousel_java,carousel_java2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CarouselJava.CarouselPage a=new CarouselJava.CarouselPage();
        Bundle b_A=new Bundle();
        b_A.putInt("image",R.drawable.a);
        a.setArguments(b_A);

        CarouselJava.CarouselPage b=new CarouselJava.CarouselPage();
        Bundle b_B=new Bundle();
        b_B.putInt("image",R.drawable.b);
        b.setArguments(b_B);

        carousel_java =(CarouselJava)findViewById(R.id.carousel_java);
        carousel_java2 =(CarouselJava)findViewById(R.id.carousel_java_two);

        CarouselJava.CarouselAdapter _adapt=new CarouselJava.CarouselAdapter(getSupportFragmentManager());
        _adapt.addFragment(a,"Page 1");
        _adapt.addFragment(b,"Page 1");

        CarouselJava.CarouselAdapter _adapt4=new CarouselJava.CarouselAdapter(getSupportFragmentManager());
        _adapt4.addFragment(new CarouselJava.CarouselPage(),"Page 1");
        _adapt4.addFragment(new CarouselJava.CarouselPage(),"Page 1");

        carousel_java.initCarousel(_adapt,3000);
        carousel_java2.initCarousel(_adapt4,4000);
    }
}
