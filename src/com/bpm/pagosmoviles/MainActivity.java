package com.bpm.pagosmoviles;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
 
public class MainActivity extends FragmentActivity {
 
    /**
     * The pager widget, which handles animation and allows swiping horizontally
     * to access previous and next pages.
     */
    ViewPager pager = null;
 
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    FragmentPagerViewAdapter pagerAdapter;
 
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        this.setContentView(R.layout.main);
 
        // Instantiate a ViewPager
        this.pager = (ViewPager) this.findViewById(R.id.pager);
 
        // Create an adapter with the fragments we show on the ViewPager
        FragmentPagerViewAdapter adapter = new FragmentPagerViewAdapter(
                getSupportFragmentManager());
        adapter.addFragment(FragmentPrueba.newInstance(android.graphics.Color.BLACK, 0));
        adapter.addFragment(FragmentPrueba.newInstance(android.graphics.Color.YELLOW, 1));
        adapter.addFragment(FragmentPrueba.newInstance(android.graphics.Color.GREEN, 2));
        adapter.addFragment(FragmentPrueba.newInstance(android.graphics.Color.CYAN, 3));
        adapter.addFragment(FragmentPrueba.newInstance(android.graphics.Color.RED, 4));
        this.pager.setAdapter(adapter);
 
    }
 
    @Override
    public void onBackPressed() {
 
        // Return to previous page when we press back button
        if (this.pager.getCurrentItem() == 0)
            super.onBackPressed();
        else
            this.pager.setCurrentItem(this.pager.getCurrentItem() - 1);
 
    }
 
}