package com.bpm.pagosmoviles;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.app.AlertDialog;
import android.app.ProgressDialog;

public class ClientesFragment extends Fragment {

	private static ArrayList<Cliente> clientes;
	static JSONArray clients;
	private static final String BACKGROUND_COLOR = "color";
	private static final String INDEX = "index";
	private String usuario;
	UserLoginTask mAuthTask = null;
	public ProgressDialog pd = null;

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
		    	    clientes.add(new Cliente(person.getString("nombre"), R.drawable.michael200, person.getString("email")));
		    	}
		    			    	
				GridView gv = (GridView) rootView.findViewById(R.id.grid_view_clientes);
				gv.setAdapter(new MyAdapter(getActivity(),clientes));				
				gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						final int user = arg2;
					   	   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						   builder.setTitle(clientes.get(arg2).getNombre());
						   builder.setItems(R.array.opciones_clientes, new DialogInterface.OnClickListener() {
							   public void onClick(DialogInterface dialog, int item) {
								   if (item == 0) {
									   Toast.makeText(getActivity().getBaseContext(), "Hacer factura", Toast.LENGTH_SHORT).show();
								   }
								   else if(item == 1) {
									   Toast.makeText(getActivity().getBaseContext(), clientes.get(user).getCorreo() , Toast.LENGTH_SHORT).show();
								   }
								   else if(item == 2) {
									   ClientesFragment.this.pd= ProgressDialog.show(getActivity(), "Procesando...", "Eliminando cliente...", true, false);
									   mAuthTask = new UserLoginTask();
									   mAuthTask.execute("http://bpmcart.com/bpmpayment/php/modelo/deleteCliente.php?emailCliente="+ clientes.get(user).getCorreo());
								   }
							    }
							});
							AlertDialog alert = builder.create();
							alert.show();
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
			mAuthTask = null;
        	try{
                if(!result.equals("false") || !result.equals("Argumentos invalidos")) {	                	
                	if (ClientesFragment.this.pd != null) {
                		ClientesFragment.this.pd.dismiss();
                		String temp = usuario;
                		Intent returnIntent = new Intent(getActivity(), Principal.class);
                		returnIntent.putExtra("usuario", temp);
                		getActivity().setResult(android.app.Activity.RESULT_OK,returnIntent);
                		startActivity(returnIntent);
                		getActivity().finish();                		
	   	            }
                }
                else {
                	Toast.makeText(getActivity().getBaseContext(), "Hubo algún error",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.d("ReadJSONFeedTask", e.getLocalizedMessage());
                Toast.makeText(getActivity().getBaseContext(), "Imposible conectarse a la red",Toast.LENGTH_LONG).show();
            }       
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {	
		}
	}
}
