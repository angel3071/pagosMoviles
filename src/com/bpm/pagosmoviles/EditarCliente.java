package com.bpm.pagosmoviles;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EditarCliente extends Activity {
    private ProgressDialog pd = null;
    private DownloadTask mAuthTask = null;
	private EditText aPaternoView, aMaternoView, nombresView, emailView, direccionView;
	private LinearLayout layoutTelefonosAeditar;
	private String nombres, apellidpP, apellidpM, direccion, email, id_cliente;
	private String usuario;
	private boolean flag;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_cliente);
        
        Intent intent = getIntent();
		String cliente = intent.getStringExtra("cliente");
		usuario = intent.getStringExtra("usuario");
        
        aPaternoView = (EditText) findViewById(R.id.clienteApellidoP);
        aMaternoView = (EditText) findViewById(R.id.clienteApellidoM);
        nombresView = (EditText) findViewById(R.id.clienteNombres);
        emailView = (EditText) findViewById(R.id.clienteEmail);
        direccionView = (EditText) findViewById(R.id.clienteDireccion);
        layoutTelefonosAeditar = (LinearLayout)findViewById(R.id.layoutTelefonos);
        
        ImageButton addPhone = (ImageButton) findViewById(R.id.imageAddCliente);
        addPhone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LinearLayout A = new LinearLayout(EditarCliente.this);
				A.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			    A.setOrientation(LinearLayout.HORIZONTAL);
			    A.setGravity(Gravity.CENTER | Gravity.BOTTOM);
			    
			    EditText tv = new EditText(EditarCliente.this);
				tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				tv.setEms(10);
				tv.setInputType(InputType.TYPE_CLASS_NUMBER);
				
				ImageButton ib = new ImageButton(EditarCliente.this);
				ib.setImageResource(R.drawable.delete16);
				ib.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						layoutTelefonosAeditar.removeView((View) v.getParent());
					}
				});
				
			    A.addView(tv);
			    A.addView(ib);
			    layoutTelefonosAeditar.addView(A,layoutTelefonosAeditar.getChildCount());
			}
		});
        
        Button updateCliente = (Button)findViewById(R.id.btnUpdateClient);
        updateCliente.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				nombres = eliminaEspacios(nombresView.getText().toString());
				apellidpP = eliminaEspacios(aPaternoView.getText().toString());
				apellidpM = eliminaEspacios(aMaternoView.getText().toString());
				email = emailView.getText().toString();
				direccion = eliminaEspacios(direccionView.getText().toString());
				
				ArrayList<String> tels = new ArrayList<String>();
				
				int childcount = layoutTelefonosAeditar.getChildCount();
				for (int i=1; i < childcount; i++){
				      LinearLayout tempView = (LinearLayout)layoutTelefonosAeditar.getChildAt(i);
				      int hijos = tempView.getChildCount();
				      
				      for(int j = 0 ;j < hijos ; j++) {
				    	  if( tempView.getChildAt(j) instanceof EditText ) {
				    		  if(!((EditText)tempView.getChildAt(j)).getText().toString().equals("")) {
				    			  String telefono =  ((EditText)tempView.getChildAt(j)).getText().toString();
					    		  tels.add(telefono);
				    		  }
				    	  }
				      }
				}
				
				String phones = "";
				for(int i = 0 ; i < tels.size() ; i++) {
					phones = phones + "telefono" + String.valueOf(i+1) + "=" + tels.get(i) + "&";
				}
								
				String url = "http://bpmcart.com/bpmpayment/php/modelo/updateClient.php?names=" + nombres +
						     "&apellidop=" + apellidpP + "&apellidom=" + apellidpM + "&emailCliente=" + email +
						     "&direccion=" + direccion + "&numTelefonos=" + String.valueOf(tels.size()) + 
						     "&" + phones + "idCliente=" + id_cliente;
								
				flag = false;
				esconderTeclado();
				EditarCliente.this.pd = ProgressDialog.show(EditarCliente.this, "Procesando...", "Actualizando datos del cliente...", true, false);
				EditarCliente.this.mAuthTask = new DownloadTask();
     			mAuthTask.execute(url);
			}
        });
        
        Button calcel = (Button)findViewById(R.id.btnCancelar);
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
        new DownloadTask().execute("http://bpmcart.com/bpmpayment/php/modelo/editCliente.php?emailCliente=" + cliente);
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
    	             JSONArray jArray = jObject.getJSONArray("telefonos");
    	             for (int i=0; i<jArray.length(); i++){
    	            	 JSONObject anotherjsonObject = jArray.getJSONObject(i);
    	                
    	            	 LinearLayout A = new LinearLayout(EditarCliente.this);
    	 				 A.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    	 			     A.setOrientation(LinearLayout.HORIZONTAL);
    	 			     A.setGravity(Gravity.CENTER | Gravity.BOTTOM);
    	 			    
    	 			     EditText tv = new EditText(EditarCliente.this);
    	 				 tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    	 				 tv.setEms(10);
    	 				 tv.setText(anotherjsonObject.getString("telefono"));
    	 				
    	 				 ImageButton ib = new ImageButton(EditarCliente.this);
    	 				 ib.setImageResource(R.drawable.delete16);
    	 				 ib.setOnClickListener(new View.OnClickListener() {
    	 					
    	 				 	 @Override
    	 			 		 public void onClick(View v) {
    	 				 		layoutTelefonosAeditar.removeView((View) v.getParent());
    	 					 }
    	 				 });
    	 				
    	 			     A.addView(tv);
    	 			     A.addView(ib);
    	 			   layoutTelefonosAeditar.addView(A,layoutTelefonosAeditar.getChildCount());
    	             }
    	             
    	             EditarCliente.this.id_cliente = jObject.getString("id_cliente");
    	         	 EditarCliente.this.aPaternoView.setText(jObject.getString("apellidop").equals("null") ? "" : jObject.getString("apellidop"));
    	         	 EditarCliente.this.aMaternoView.setText(jObject.getString("apellidom").equals("null") ? "" : jObject.getString("apellidom"));
    	         	 EditarCliente.this.nombresView.setText(jObject.getString("nombres").equals("null") ? "" : jObject.getString("nombres"));
    	         	 EditarCliente.this.emailView.setText(jObject.getString("email").equals("null") ? "" : jObject.getString("email"));
    	         	 EditarCliente.this.direccionView.setText(jObject.getString("direccion").equals("null") ? "" : jObject.getString("direccion"));
    	         	 
    	             if (EditarCliente.this.pd != null) {
    	            	 EditarCliente.this.pd.dismiss();
    	             }
            	 }
            	 catch(Exception e) {
            		 Log.w("Error", e.getMessage());
            	 }
        	 }
        	 else {
        		 if (EditarCliente.this.pd != null) {
	            	 EditarCliente.this.pd.dismiss();
	            	 Toast.makeText(getBaseContext(), "Cliente Actualizado", Toast.LENGTH_SHORT).show();
             		 Intent returnIntent = new Intent();
             		 returnIntent.putExtra("result", usuario);
             		 setResult(RESULT_OK,returnIntent);     
             		 finish();
	             }
        	 }
         }
    }    
}