package com.bpm.pagosmoviles;

import java.util.ArrayList;
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

public class RegistrarCliente extends Activity {
	private UserLoginTask mAuthTask = null;
	private ProgressDialog pd = null;
	private EditText nombresView, apellidpPView, apellidpMView, emailView, direccionView;
	private String nombres, apellidpP, apellidpM, direccion, email, usuario;
	private LinearLayout layoutTelefonos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar_cliente);
        
        Intent intent = getIntent();
        usuario = intent.getStringExtra("usuario");
        
        nombresView = (EditText) findViewById(R.id.clienteNombres);
        apellidpPView = (EditText) findViewById(R.id.clienteApellidoP);
        apellidpMView = (EditText) findViewById(R.id.clienteApellidoM);
        emailView = (EditText) findViewById(R.id.clienteEmail);
        direccionView = (EditText) findViewById(R.id.clienteDireccion);
        layoutTelefonos = (LinearLayout)findViewById(R.id.layoutTelefonos);
        
        ImageButton addPhone = (ImageButton) findViewById(R.id.imageAddCliente);
        addPhone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LinearLayout A = new LinearLayout(RegistrarCliente.this);
				A.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			    A.setOrientation(LinearLayout.HORIZONTAL);
			    A.setGravity(Gravity.CENTER | Gravity.BOTTOM);
			    
			    EditText tv = new EditText(RegistrarCliente.this);
				tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				tv.setEms(10);
				tv.setInputType(InputType.TYPE_CLASS_NUMBER);
				
				ImageButton ib = new ImageButton(RegistrarCliente.this);
				ib.setImageResource(R.drawable.delete16);
				ib.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						layoutTelefonos.removeView((View) v.getParent());
					}
				});
				
			    A.addView(tv);
			    A.addView(ib);
				
			    layoutTelefonos.addView(A,layoutTelefonos.getChildCount());
			}
		});
        
        Button addCliente = (Button)findViewById(R.id.btnAddCliente);
        addCliente.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				nombres = eliminaEspacios(nombresView.getText().toString());
				apellidpP = eliminaEspacios(apellidpPView.getText().toString());
				apellidpM = eliminaEspacios(apellidpMView.getText().toString());
				email = emailView.getText().toString();
				direccion = eliminaEspacios(direccionView.getText().toString());
				
				ArrayList<String> tels = new ArrayList<String>();
				
				int childcount = layoutTelefonos.getChildCount();
				for (int i=1; i < childcount; i++){
				      LinearLayout tempView = (LinearLayout)layoutTelefonos.getChildAt(i);
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
								
				String url = "http://bpmcart.com/bpmpayment/php/modelo/addClient.php?names=" + nombres +
						     "&apellidop=" + apellidpP + "&apellidom=" + apellidpM + "&emailCliente=" + email +
						     "&direccion=" + direccion + "&numTelefonos=" + String.valueOf(tels.size()) + 
						     "&" + phones + "emailUser=" + usuario;
								
				esconderTeclado();
				RegistrarCliente.this.pd = ProgressDialog.show(RegistrarCliente.this, "Procesando...", "Registrando datos...", true, false);
				mAuthTask = new UserLoginTask();
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
	}
	
	private String eliminaEspacios(String palabras) {
    	return palabras.replaceAll("\\s", "~");
    }
	
	private void esconderTeclado() {
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
		inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),      
		InputMethodManager.HIDE_NOT_ALWAYS);
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
	                	if (RegistrarCliente.this.pd != null) {
	                		RegistrarCliente.this.pd.dismiss();
	                		
	                		Toast.makeText(getBaseContext(), "Cliente Agregado", Toast.LENGTH_SHORT).show();
	                		
	                		Intent returnIntent = new Intent();
	                		returnIntent.putExtra("result", usuario);
	                		setResult(RESULT_OK,returnIntent);     
	                		finish();
		   	            }
	                }
	                else {
	                	Toast.makeText(getBaseContext(), "Hubo algún error",Toast.LENGTH_LONG).show();
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
