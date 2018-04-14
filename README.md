Welcome to the Android-Flexible-Carousel wiki!
### Responsive Android carousel
### Introduction
Flexible carousel comprise of a 
* view pager
* Relative layouts 
* fragment manager

The Parent layout is a relative layout with three children, The view pager, the indicator and Left right buttons

A basic fragment is provided to hold images
 `public static class CarouselPage extends Fragment {

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

    }`

To add the fragment to the adapter
` CarouselJava.CarouselPage a=new CarouselJava.CarouselPage();
        Bundle b_A=new Bundle();
        b_A.putInt("image",R.drawable.a);
        a.setArguments(b_A);

        CarouselJava.CarouselPage b=new CarouselJava.CarouselPage();
        Bundle b_B=new Bundle();
        b_B.putInt("image",R.drawable.b);
        b.setArguments(b_B);

      
        CarouselJava.CarouselAdapter _adapt=new CarouselJava.CarouselAdapter(getSupportFragmentManager());
        _adapt.addFragment(a,"Page 1");
        _adapt.addFragment(b,"Page 1");`

Initialize the carousel with an adapter 
`carousel_java.initCarousel(_adapt,3000);
        carousel_java2.initCarousel(_adapt4,4000);`
