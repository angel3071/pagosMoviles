package com.bpm.pagosmoviles;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrarProducto extends Activity{
	private UserLoginTask mAuthTask = null;
	private ProgressDialog pd = null;
	private EditText nombreView, precioView, descripcionView;
	private String nombre, precio, descripcion, usuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar_producto);
        
        Intent intent = getIntent();
        usuario = intent.getStringExtra("usuario");
        
        nombreView = (EditText) findViewById(R.id.productName);
        precioView = (EditText) findViewById(R.id.productPrice);
        descripcionView = (EditText) findViewById(R.id.productDescription);
        
        Button addProducto = (Button)findViewById(R.id.btnAddProduct);
        addProducto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				nombre = eliminaEspacios(nombreView.getText().toString());
				precio = eliminaEspacios(precioView.getText().toString());
				descripcion = eliminaEspacios(descripcionView.getText().toString());
				
				String url = "http://bpmcart.com/bpmpayment/php/modelo/addProduct.php?name=" + nombre +
						     "&price=" + precio + "&desc=" + descripcion + "&email=q@q";
				
				RegistrarProducto.this.pd = ProgressDialog.show(RegistrarProducto.this, "Procesando...", "Registrando datos...", true, false);
				mAuthTask = new UserLoginTask();
				mAuthTask.execute(url);
			}
	});
	}
	
	private String eliminaEspacios(String palabras) {
    	return palabras.replaceAll("\\s", "~");
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
	                if(!result.equals("false")) {	                	
	                	if (RegistrarProducto.this.pd != null) {
	                		RegistrarProducto.this.pd.dismiss();
	                		
	                		Intent i = new Intent(getApplicationContext(), Principal.class);
			                i.putExtra("usuario", usuario);
							startActivity(i);
							finish();
		   	            }
	                	
	                	Toast.makeText(getBaseContext(), "Producto Agregado", Toast.LENGTH_SHORT).show();
	                }
	                else {
	                	Toast.makeText(getBaseContext(), "Credenciales inválidas",Toast.LENGTH_LONG).show();
	                }
	            } catch (Exception e) {
	                Log.d("ReadJSONFeedTask", e.getLocalizedMessage());
	                Toast.makeText(getBaseContext(), "Imposible conectarse a la red",Toast.LENGTH_LONG).show();
	            }          
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
		}
	}
}
