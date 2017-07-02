package com.marklordan.brewski;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements BeerFragment.OnFragmentInteractionListener{

    public static final String BEER_LIST = "com.marklordan.brewski.BEER_LIST";
    public static final String RECYCLERVIEW_LAYOUT = "com.marklordan.brewski.LAYOUT";

    private boolean isListSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_nav);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_beer:
                        break;
                    case R.id.action_brewery:
                        break;
                    case R.id.action_search:
                        break;
                }
                return true;
            }
        });

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        String layoutName = (isListSelected ? getString(R.string.list_options) : getString(R.string.grid_options));
        MenuItem layoutOption = menu.findItem(R.id.view_option);
        layoutOption.setTitle(layoutName);
        layoutOption.setIcon((isListSelected ? getDrawable(R.drawable.ic_format_list_bulleted) : getDrawable(R.drawable.ic_grid_on)));
        return true;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save the beers to the bundle for use on rotation
//        outState.putSerializable(BEER_LIST, beerList);
        outState.putBoolean(RECYCLERVIEW_LAYOUT, isListSelected);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class PagerAdapter extends FragmentPagerAdapter{

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return BeerFragment.newInstance();
                default:
                    return BeerFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
