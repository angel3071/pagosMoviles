package com.bpm.pagosmoviles;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class ClientesFragment extends Fragment {

	/**
	 * Key to insert the background color into the mapping of a Bundle.
	 */
	private ArrayList<Cliente> clientes = new ArrayList<Cliente>();

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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_clientes, container, false);
				
		clientes.add(new Cliente("Pedro",R.drawable.michael200));
		clientes.add(new Cliente("Pablo",R.drawable.ema200));
		clientes.add(new Cliente("Federico",R.drawable.johnlennon200));
		clientes.add(new Cliente("Angel",R.drawable.ritchie200));
		clientes.add(new Cliente("Claudia",R.drawable.halle200));

		GridView gv = (GridView) rootView.findViewById(R.id.grid_view);
		gv.setAdapter(new MyAdapter(getActivity(),clientes));
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

            	int a =1;	
            }
        });
		

		// Show the current page index in the view
//		TextView tvIndex = (TextView) rootView.findViewById(R.id.tvIndex);
//		tvIndex.setText(String.valueOf(this.index));

		// Change the background color
//		rootView.setBackgroundColor(this.color);
		//rootView.setBackgroundColor(Color.BLACK);
		return rootView;
	}
	
	@Override
	public String toString() {
		return "Clientes (" + clientes.size() + ")";
	}
}
