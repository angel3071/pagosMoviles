package com.bpm.pagosmoviles;

import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditarProducto extends Activity {
    private ProgressDialog pd = null;
    private DownloadTask mAuthTask = null;
    private EditText nombreView, precioView, descripcionView;
	private String nombre, precio, descripcion, idProducto, usuario;
	private boolean flag;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_producto);
        
        Intent intent = getIntent();
        idProducto = intent.getStringExtra("idProducto");
        usuario = intent.getStringExtra("usuario");
        
		nombreView = (EditText) findViewById(R.id.productName);
		precioView = (EditText) findViewById(R.id.productPrice);
		descripcionView = (EditText) findViewById(R.id.productDescription);
        
        Button updateProducto = (Button)findViewById(R.id.btnUpdateProduct);
        updateProducto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				nombre = eliminaEspacios(nombreView.getText().toString());
				precio = precioView.getText().toString();
				descripcion = eliminaEspacios(descripcionView.getText().toString());
								
				String url = "http://bpmcart.com/bpmpayment/php/modelo/updateProducto.php?name=" + nombre +
						     "&price=" + precio + "&desc=" + descripcion +  "&idProducto=" + idProducto;
								
				EditarProducto.this.flag = false;
				esconderTeclado();
				EditarProducto.this.pd = ProgressDialog.show(EditarProducto.this, "Procesando...", "Actualizando datos del cliente...", true, false);
				EditarProducto.this.mAuthTask = new DownloadTask();
     			mAuthTask.execute(url);
			}
        });
        
        Button calcel = (Button)findViewById(R.id.btnCancel);
        calcel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			    Intent returnIntent = new Intent();
        		returnIntent.putExtra("result", usuario);
        		setResult(RESULT_CANCELED,returnIntent);     
        		finish();
			}
        });
        
        this.flag = true;
        this.pd = ProgressDialog.show(this, "Procesando...", "Descargando datos...", true, false);
        new DownloadTask().execute("http://bpmcart.com/bpmpayment/php/modelo/editProducto.php?id_producto=" + idProducto);
    }
    
    private String eliminaEspacios(String palabras) {
    	return palabras.replaceAll("\\s", "~");
    }
	
	private void esconderTeclado() {
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
		inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),      
		InputMethodManager.HIDE_NOT_ALWAYS);
	}

    private class DownloadTask extends AsyncTask<String, Void, String> {
         protected String doInBackground(String... urls) {
        	 try {
 				return new JsonCont().readJSONFeed(urls[0]);
 			} catch (Exception e) {
 				return null;
 			}
         }

         protected void onPostExecute(String result) {
        	 if(flag) {
        		 try {
        			 JSONObject jObject  = new JSONObject(result);
    	         	 EditarProducto.this.nombreView.setText(jObject.getString("nombre").equals("null") ? "" : jObject.getString("nombre"));
    	         	 EditarProducto.this.precioView.setText(jObject.getString("precio").equals("null") ? "" : jObject.getString("precio"));
    	         	 EditarProducto.this.descripcionView.setText(jObject.getString("descripcion").equals("null") ? "" : jObject.getString("descripcion"));
    	         	 
    	             if (EditarProducto.this.pd != null) {
    	            	 EditarProducto.this.pd.dismiss();
    	             }
            	 }
            	 catch(Exception e) {
            		 Log.w("Error", e.getMessage());
            	 }
        	 }
        	 else {
        		 if (EditarProducto.this.pd != null) {
	            	 EditarProducto.this.pd.dismiss();
	            	 Toast.makeText(getBaseContext(), "Producto Actualizado", Toast.LENGTH_SHORT).show();
             		 Intent returnIntent = new Intent();
             		 returnIntent.putExtra("result", usuario);
             		 setResult(RESULT_OK,returnIntent);     
             		 finish();
	             }
        	 }       	 
         }
    }    
}