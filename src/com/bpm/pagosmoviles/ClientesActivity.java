package com.bpm.pagosmoviles;



import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ClientesActivity extends Activity {

	private ArrayList<Cliente> clientes = new ArrayList<Cliente>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_clientes);
		
		clientes.add(new Cliente("Pedro",R.drawable.michael200));
		clientes.add(new Cliente("Pablo",R.drawable.ema200));
		clientes.add(new Cliente("Federico",R.drawable.johnlennon200));
		clientes.add(new Cliente("Angel",R.drawable.ritchie200));
		clientes.add(new Cliente("Claudia",R.drawable.halle200));

		
		
		
		GridView gv = (GridView) findViewById(R.id.grid_view);
		gv.setAdapter(new MyAdapter(this,clientes));
		gv.setOnItemClickListener(new OnItemClickListener() {@Override
            public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id) {

            	int a =1;
            	
            	
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.clientes, menu);
		return true;
	}

}
