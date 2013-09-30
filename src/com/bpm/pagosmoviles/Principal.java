package com.bpm.pagosmoviles;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;
import com.viewpagerindicator.TitlePageIndicator;

public class Principal extends FragmentActivity  {
	public ViewPager pager = null;
	MyFragmentPagerAdapter pagerAdapter;
	JSONObject jObjectClientes = null;
	JSONObject jObjectProductos = null;
	int corrida = 0;
	UserLoginTask mAuthTaskClientes = null;
	UserLoginTask mAuthTaskProductos = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_principal);

		this.pager = (ViewPager) this.findViewById(R.id.pager);
		Intent intent = getIntent();
		String usuario = intent.getStringExtra("usuario");
		corrida = 1;
		mAuthTaskClientes = new UserLoginTask();
		mAuthTaskClientes.execute("http://bpmcart.com/bpmpayment/php/modelo/getCPF.php?email="+ usuario + "&obtener=clientes");
		mAuthTaskProductos = new UserLoginTask();
		mAuthTaskProductos.execute("http://bpmcart.com/bpmpayment/php/modelo/getCPF.php?email="+ usuario + "&obtener=productos");
	}

	@Override
	public void onBackPressed() {
		if (this.pager.getCurrentItem() == 0)
			super.onBackPressed();
		else
			this.pager.setCurrentItem(this.pager.getCurrentItem() - 1);
	}
	
	public class UserLoginTask extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... urls) {
			try {
				return new JsonCont().readJSONFeed(urls[0]);
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {
            	try{
	                if(!result.equals("false")) {	                	
	                	if(corrida == 1) {
	                		corrida = 2;
	                		jObjectClientes  = new JSONObject(result);
	                	}
	                	
	                	else if(corrida == 2) {
	                		jObjectProductos = new JSONObject(result);
	                		MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
		            		adapter.addFragment(ClientesFragment.newInstance(Color.BLACK, 1, jObjectClientes));
		            		adapter.addFragment(ProductosFragment.newInstance(Color.BLACK, 2, jObjectProductos));
		            		pager.setAdapter(adapter);
		            		TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
		            		titleIndicator.setBackgroundColor(Color.BLACK);
		            		titleIndicator.setViewPager(pager);
	                	}
	                }
	                else {
	                	Toast.makeText(getBaseContext(), "Credenciales inválidas",Toast.LENGTH_LONG).show();
	                }
	            } catch (JSONException e) {
	                Log.d("ReadJSONFeedTask", "BLAAA");
	                Toast.makeText(getBaseContext(), "Imposible conectarse a la red",Toast.LENGTH_LONG).show();
	            }          
		}

		@Override
		protected void onCancelled() {
		}
	}
}