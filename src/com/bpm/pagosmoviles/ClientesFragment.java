package com.bpm.pagosmoviles;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ClientesFragment extends Fragment {

	private static ArrayList<Cliente> clientes;
	static JSONArray clients;
	private static final String BACKGROUND_COLOR = "color";
	private static final String INDEX = "index";
	private static boolean cargado = false;
	private String usuario;

	//private int color, index;

	public static ClientesFragment newInstance(int color, int index, JSONObject jObject ) {
		ClientesFragment fragment = new ClientesFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(BACKGROUND_COLOR, color);
		bundle.putInt(INDEX, index);
		fragment.setArguments(bundle);
		fragment.setRetainInstance(true);
		
		clientes = new ArrayList<Cliente>();
		try {
			clients = jObject.getJSONArray("clientes");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		
		Intent intent = getActivity().getIntent();
        usuario = intent.getStringExtra("usuario");

		// Load parameters when the initial creation of the fragment is done
		//this.color = (getArguments() != null) ? getArguments().getInt(BACKGROUND_COLOR) : Color.BLACK;
		//this.index = (getArguments() != null) ? getArguments().getInt(INDEX) : -1;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_clientes, container, false);

		if(clientes.size() != 0)
			clientes.clear();
			try {
		    	int n = clients.length();
		    	for(int i = 0 ; i < n ; i++) {
		    		JSONObject person = clients.getJSONObject(i);		    	    
		    	    clientes.add(new Cliente(person.getString("nombre"),R.drawable.michael200));
		    	    ClientesFragment.cargado = true;
		    	}
				GridView gv = (GridView) rootView.findViewById(R.id.grid_view_clientes);
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
		return rootView;
	}
	
	@Override
	public String toString() {
		return "Clientes";
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    super.onCreateOptionsMenu(menu, inflater);
	    getActivity().getMenuInflater().inflate(R.menu.clientes, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case R.id.add_cliente:            
            Intent i = new Intent(getActivity().getBaseContext(), RegistrarCliente.class);
            i.putExtra("usuario", usuario);
            startActivityForResult(i, 1);
            return true;
	    default:
	        break;
	    }
	    return false;
	}
}
