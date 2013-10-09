package com.bpm.pagosmoviles;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registro extends Activity {
	
	private UserLoginTask mAuthTask = null;
	
	private EditText aPaternoView;
	private EditText aMaternoView;
	private EditText nombresView;
	private EditText ciudadView;
	private EditText estadoView;
	private EditText paisView;
	private EditText sectorIdustrialView;
	private EditText giroEmpresaView;
	private EditText descripcionView;
	
	private String aPaterno;
	private String aMaterno;
	private String nombres;
	private String ciudad;
	private String estado;
	private String pais;
	private String sectorIdustrial;
	private String giroEmpresa;
	private String descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        
        aPaternoView = (EditText) findViewById(R.id.paterno);
        aMaternoView = (EditText) findViewById(R.id.materno);
        nombresView = (EditText) findViewById(R.id.nombre);
        ciudadView = (EditText) findViewById(R.id.ciudad);
        estadoView = (EditText) findViewById(R.id.estado);
        paisView = (EditText) findViewById(R.id.pais);
        sectorIdustrialView = (EditText) findViewById(R.id.sector);
        giroEmpresaView = (EditText) findViewById(R.id.giro);
        descripcionView = (EditText) findViewById(R.id.uso);
        
        AlertDialog.Builder dl  = new AlertDialog.Builder(this);
        dl.setTitle("¡Bienvenido!");
        dl.setIcon(R.drawable.bpm);
        dl.setMessage("Bienvenido al más moderno y novedoso sistema de pagos móviles, a continuación te invitamos a que realices los siguientes pasos para activar tu cuenta y puedas recibir y solicitar pagos de cualquier parte del mundo con tarjetas de crédito, debito y servicios.");
        dl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
	});
       dl.create().show();
    
       Button b = (Button)findViewById(R.id.btnLogin);
       b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				aPaterno = eliminaEspacios(aPaternoView.getText().toString());
				aMaterno = eliminaEspacios(aMaternoView.getText().toString());
				nombres = eliminaEspacios(nombresView.getText().toString());
				ciudad = eliminaEspacios(ciudadView.getText().toString());
				estado = eliminaEspacios(estadoView.getText().toString());
				pais = eliminaEspacios(paisView.getText().toString());
				sectorIdustrial = eliminaEspacios(sectorIdustrialView.getText().toString());
				giroEmpresa = eliminaEspacios(giroEmpresaView.getText().toString());
				descripcion = eliminaEspacios(descripcionView.getText().toString());
				
				String url = "http://bpmcart.com/bpmpayment/php/modelo/editUser.php?ap=" + aPaterno +
						     "&am=" + aMaterno + "&names=" + nombres + "&city=" + ciudad + "&state=" + estado +
						     "&pais=" + pais + "&si=" + sectorIdustrial + "&ge=" + giroEmpresa + "&desc=" + descripcion +
						     "&email=q@q";
				
				mAuthTask = new UserLoginTask();
				mAuthTask.execute(url);
			}
	});
    
    }
    
    private String eliminaEspacios(String palabras) {
    	return palabras.replaceAll("\\s", "~");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.registro, menu);
        return true;
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
	                	/*JSONObject jObject  = new JSONObject(result);
	                	String usuario = jObject.getString("email");
		                
		                Intent i = new Intent(getApplicationContext(), Principal.class);
		                i.putExtra("usuario", usuario);
						startActivity(i);
						//finish();*/
	                	
	                	Toast.makeText(getBaseContext(), "Actualización satisfactoria", Toast.LENGTH_SHORT).show();
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