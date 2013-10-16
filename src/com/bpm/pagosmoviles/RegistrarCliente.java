package com.bpm.pagosmoviles;

import java.util.ArrayList;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RegistrarCliente extends Activity {
	private UserLoginTask mAuthTask = null;
	private ProgressDialog pd = null;
	private EditText nombresView, apellidpPView, apellidpMView, emailView, direccionView, telefonoView;
	private String nombres, apellidpP, apellidpM, direccion, email, usuario;
	private LinearLayout ll1;
	private ArrayList<EditText> telefonos;
	
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
        ll1 = (LinearLayout) findViewById(R.id.linearLayout1);
        telefonoView = (EditText) findViewById(R.id.clienteTelefono);
        
        telefonos = new ArrayList<EditText>();
        telefonos.add(telefonoView);
        
        
        Button addPhone = (Button) findViewById(R.id.btnAddPhone);
        addPhone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LinearLayout A = new LinearLayout(RegistrarCliente.this);
				A.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			    A.setOrientation(LinearLayout.HORIZONTAL);
			    
			    EditText tv = new EditText(RegistrarCliente.this);
				tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				tv.setEms(10);
				
				Button b = new Button(RegistrarCliente.this);
				b.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				b.setText("X");
				b.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ll1.removeView((View) v.getParent());						
					}
				});
				
			    A.addView(tv);
			    A.addView(b);
				
				ll1.addView(A,ll1.getChildCount()-2);
				telefonos.add(tv);
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
				
				String phones = "";
				
				for(int i = 0 ; i < RegistrarCliente.this.telefonos.size() ; i++) {
					phones = phones + "telefono" + String.valueOf(i+1) + "=" + RegistrarCliente.this.telefonos.get(i).getText().toString() + "&";
				}
				
				String url = "http://bpmcart.com/bpmpayment/php/modelo/addClient.php?names=" + nombres +
						     "&apellidop=" + apellidpP + "&apellidom=" + apellidpM + "&emailCliente=" + email +
						     "&direccion=" + direccion + "&numTelefonos=" + String.valueOf(telefonos.size()) + 
						     "&" + phones + "emailUser=" + usuario;
				
				RegistrarCliente.this.pd = ProgressDialog.show(RegistrarCliente.this, "Procesando...", "Registrando datos...", true, false);
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
