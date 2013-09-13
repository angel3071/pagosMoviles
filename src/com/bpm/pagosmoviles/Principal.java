package com.bpm.pagosmoviles;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.TitlePageIndicator;

public class Principal extends FragmentActivity  {

	/**
	 * The pager widget, which handles animation and allows swiping horizontally
	 * to access previous and next pages.
	 */
	ViewPager pager = null;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	MyFragmentPagerAdapter pagerAdapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_principal);

		// Instantiate a ViewPager
		this.pager = (ViewPager) this.findViewById(R.id.pager);

		// Set a custom animation
		// this.pager.setPageTransformer(true, new ZoomOutPageTransformer());

		// Create an adapter with the fragments we show on the ViewPager
		MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(
				getSupportFragmentManager());
		adapter.addFragment(ClientesFragment.newInstance(Color.BLACK, 1));
		adapter.addFragment(ProductosFragment.newInstance(Color.BLACK, 1));
		
		this.pager.setAdapter(adapter);

		// Bind the title indicator to the adapter
		TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
		titleIndicator.setBackgroundColor(Color.BLACK);
		titleIndicator.setViewPager(pager);

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







