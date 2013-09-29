package com.bpm.pagosmoviles;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bpm.pagosmoviles.LoginActivity.UserLoginTask;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ClientesFragment extends Fragment {

	/**
	 * Key to insert the background color into the mapping of a Bundle.
	 */
	private ArrayList<Cliente> clientes;
	
	private UserLoginTask mAuthTask = null;
	private JSONObject jsonClientes = null;
	private ViewGroup containverVG = null;

	private static final String BACKGROUND_COLOR = "color";

	/**
	 * Key to insert the index page into the mapping of a Bundle.
	 */
	private static final String INDEX = "index";

	private int color;
	private int index;

	/**
	 * Instances a new fragment with a background color and an index page.
	 * 
	 * @param color
	 *            background color
	 * @param index
	 *            index page
	 * @return a new page
	 */
	public static ClientesFragment newInstance(int color, int index) {
		// Instantiate a new fragment
		ClientesFragment fragment = new ClientesFragment();

		// Save the parameters
		Bundle bundle = new Bundle();
		bundle.putInt(BACKGROUND_COLOR, color);
		bundle.putInt(INDEX, index);
		fragment.setArguments(bundle);
		fragment.setRetainInstance(true);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Load parameters when the initial creation of the fragment is done
		this.color = (getArguments() != null) ? getArguments().getInt(BACKGROUND_COLOR) : Color.BLACK;
		this.index = (getArguments() != null) ? getArguments().getInt(INDEX) : -1;
		
		Intent intent = getActivity().getIntent();
		String usuario = intent.getStringExtra("usuario");
		
		mAuthTask = new UserLoginTask();
		mAuthTask.execute("http://bpmcart.com/bpmpayment/php/modelo/getCPF.php?email="+ usuario + "&obtener=clientes");
		
		Log.w("OnCreate", "Entró al método");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		clientes = new ArrayList<Cliente>();
		containverVG = container;
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_clientes, container, false);
		
		Log.w("STATUS A", mAuthTask.getStatus().name());
		
		if(mAuthTask.getStatus() == AsyncTask.Status.FINISHED){
			try {
				JSONArray clients = jsonClientes.getJSONArray("clientes");
		    	int n = clients.length();
		    	for(int i = 0 ; i < n ; i++) {
		    		JSONObject person = clients.getJSONObject(i);		    	    
		    	    clientes.add(new Cliente(person.getString("nombre"),R.drawable.michael200));
		    	}
		    	
		    	GridView gv = (GridView) rootView.findViewById(R.id.grid_view);
				gv.setAdapter(new MyAdapter(getActivity(),clientes));
				
				gv.setOnItemClickListener(new OnItemClickListener() {
					@Override
		            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		            	Toast.makeText(getActivity().getBaseContext(), String.valueOf(position), Toast.LENGTH_LONG).show();
		            }
		        });
			} catch(Exception e) {
				Log.w("ERROR", e.getMessage());
			}	
		}

		return rootView;
	}
	
	public void createGUI(){
		
	}
	
	@Override
	public String toString() {
		return "Clientes (" + clientes.size() + ")";
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
	                	JSONObject jObject  = new JSONObject(result);
	                	jsonClientes = jObject;
	                	createGUI();
	                	
	                	
	                	FragmentManager manager = getActivity().getSupportFragmentManager();
	                    FragmentTransaction ft = manager.beginTransaction();
	                    Fragment newFragment = ClientesFragment.this;
	                    ClientesFragment.this.onDestroy();
	                    ft.remove(ClientesFragment.this);
	                    ft.replace(containverVG.getId(),newFragment);
	                    ft.addToBackStack(null);   
	                    ft.commit();
	                	
	                	
	                }
	                else {
	                	Toast.makeText(getActivity().getBaseContext(), "Credenciales inválidas",Toast.LENGTH_LONG).show();
	                }
	            } catch (Exception e) {
	                Log.d("ReadJSONFeedTask", e.getLocalizedMessage());
	                Toast.makeText(getActivity().getBaseContext(), "Imposible conectarse a la red",Toast.LENGTH_LONG).show();
	            }          
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
		}
	}
}
