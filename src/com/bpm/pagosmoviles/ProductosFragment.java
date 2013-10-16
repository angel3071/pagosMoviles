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

public class ProductosFragment extends Fragment {

	private static ArrayList<Producto> productos;
	static JSONArray articles;
	private static final String BACKGROUND_COLOR = "color";
	private static final String INDEX = "index";
	private String usuario;

	//private int color, index;

	public static ProductosFragment newInstance(int color, int index, JSONObject jObject) {
		ProductosFragment fragment = new ProductosFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(BACKGROUND_COLOR, color);
		bundle.putInt(INDEX, index);
		fragment.setArguments(bundle);
		fragment.setRetainInstance(true);
		
		productos = new ArrayList<Producto>();
		try {
			articles = jObject.getJSONArray("productos");
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootViewProd = (ViewGroup) inflater.inflate(R.layout.fragment_productos, container, false);

			try {
		    	int n = articles.length();
		    	for(int i = 0 ; i < n ; i++) {
		    		JSONObject article = articles.getJSONObject(i);
		    		productos.add(new Producto(article.getString("nombre"),R.drawable.smartphone));
		    	}
		    	
		    	GridView gv = (GridView) rootViewProd.findViewById(R.id.grid_view_productos);
				gv.setAdapter(new MyAdapter(getActivity(),productos));
				gv.setOnItemClickListener(new OnItemClickListener() {
					@Override
		            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		            	Toast.makeText(getActivity().getBaseContext(), String.valueOf(position), Toast.LENGTH_LONG).show();
		            }
		        });
			} catch(Exception e) {
				Log.w("ERROR", "Algo pasó");
			}
		return rootViewProd;
	}
	
	@Override
	public String toString() {
		return "Productos";
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    super.onCreateOptionsMenu(menu, inflater);
	    getActivity().getMenuInflater().inflate(R.menu.productos, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.add_producto:            
	    	Intent i = new Intent(getActivity().getBaseContext(), RegistrarProducto.class);
            i.putExtra("usuario", usuario);
            startActivityForResult(i, 1);
            return true;
	    default:
	        break;
	    }

	    return false;
	}
}
