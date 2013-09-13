package com.bpm.pagosmoviles;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class Registro extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        
       AlertDialog.Builder dl  = new AlertDialog.Builder(this);
       dl.setTitle("¡Bienvenido!");
       dl.setIcon(R.drawable.bpm);
       dl.setMessage("“Bienvenido al más moderno y novedoso sistema de pagos móviles, a continuación te invitamos a que realices los siguientes pasos para activar tu cuenta y puedas recibir y solicitar pagos de cualquier parte del mundo con tarjetas de crédito, débito y servicios.“");
       dl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}
	});
       dl.create().show();
    
       Button b = (Button)findViewById(R.id.btnLogin);
       b.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			Intent i = new Intent(getApplicationContext(),LoginActivity.class);
			startActivity(i);
			
		}
	});
    
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.registro, menu);
        return true;
    }
    
}
