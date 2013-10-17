package com.bpm.pagosmoviles;

import org.json.JSONObject;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private UserLoginTask mAuthTask = null;
	public static String mEmail;
	private String mPassword;

	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	public boolean login;
	public String storedMail;
	public String storedPass;
	public String pass;
	public String email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences storedPreferences = getSharedPreferences("datos",Context.MODE_PRIVATE);
		storedMail = storedPreferences.getString("mail", "");
		storedPass = storedPreferences.getString("pass", "");
		
		if(!storedMail.equals("") && !storedPass.equals("")){
			pass = storedPass;
			email = storedMail;
			mAuthTask = new UserLoginTask();
			mAuthTask.execute("http://bpmcart.com/bpmpayment/php/modelo/login.php?email="+storedMail+
					                                                            "&password="+storedPass);
		}
		
		setContentView(R.layout.activity_login);
		mEmailView = (EditText) findViewById(R.id.email);
		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();					
						pass = (storedPass.equals(""))? mPasswordView.getText().toString():storedPass;
						email = (storedMail.equals(""))? mEmailView.getText().toString():storedMail;
						mAuthTask = new UserLoginTask();
						mAuthTask.execute("http://bpmcart.com/bpmpayment/php/modelo/login.php?email="+email+
								                                                            "&password="+pass);
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void attemptLogin() {
		if (mAuthTask != null) { return; }

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError("Este campo es Obligatorio");
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError("Este campo es Obligatorio");
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError("Este campo es Obligatorio");
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText("Por favor espere");
			showProgress(true);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	public class UserLoginTask extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... urls) {
			try {
				String ret = new JsonCont().readJSONFeed(urls[0]);
				Log.w("RETURN", ret);
				return ret;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {
			mAuthTask = null;
			showProgress(false);
            	try{
	                if(!result.equals("false")) {
	                	JSONObject jObject  = new JSONObject(result);
	                	String usuario = jObject.getString("email");
	                	
	                	Log.w("USUARIO", usuario);
		                
		                Intent i = new Intent(getApplicationContext(), Principal.class);
		                i.putExtra("usuario", usuario);
		                
		                SharedPreferences storedPreferences = getSharedPreferences("datos",Context.MODE_PRIVATE);
		                Editor editor = storedPreferences.edit();
		                editor.putString("mail", email);
		                editor.putString("pass", pass);
		                editor.commit();
		                
						startActivity(i);
						showProgress(false);
						
						finish();
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
			showProgress(false);
		}
	}
}
