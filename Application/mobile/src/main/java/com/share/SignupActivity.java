package com.share;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.james.sharedclasses.GetUserRequestWrapper;
import com.example.james.sharedclasses.Profile;
import com.example.james.sharedclasses.ServerEndpoint;
import com.example.james.sharedclasses.User;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class SignupActivity extends Activity {
    private static final String TAG = "SignupActivity";
    private static final String BASE_URL = "http://share-backend.herokuapp.com/";
    private ProgressDialog progressDialog;

    @InjectView(R.id.input_name) EditText _nameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.input_phone) EditText _phoneText;
    @InjectView(R.id.input_occupation) EditText _occupationText;
    @InjectView(R.id.input_username) EditText _usernameText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;

    private UserRegistrationTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });

    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        // Show a progress spinner, and kick off a background task to
        // perform user registration.
        progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String username = _usernameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String name = _nameText.getText().toString();
        String phone = _phoneText.getText().toString();
        String occupation = _occupationText.getText().toString();

        mAuthTask = new UserRegistrationTask(name, email, password, phone, occupation, username);
        mAuthTask.execute((Void) null);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String username = _usernameText.getText().toString();
        String phone = _phoneText.getText().toString();
        String occupation = _occupationText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (username.isEmpty() || username.length() < 3) {
            _usernameText.setError("at least 3 characters");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
            _phoneText.setError("enter a valid phone number");
            valid = false;
        } else {
            _phoneText.setError(null);
        }

        if (occupation.isEmpty()) {
            _occupationText.setError("Cannot be blank");
            valid = false;
        } else {
            _occupationText.setError(null);
        }

        return valid;
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    /**
     * Represents an asynchronous registration task used to authenticate
     * the user.
     */
    public class UserRegistrationTask extends AsyncTask<Void, Void, Boolean> {

        private final String mName;
        private final String mEmail;
        private final String mPassword;
        private final String mUsername;
        private final String mOccupation;
        private final String mPhone;

        UserRegistrationTask(String name, String email, String password, String phone, String occupation, String username) {
            mName = name;
            mEmail = email;
            mPassword = password;
            mUsername = username;
            mOccupation = occupation;
            mPhone = phone;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            // TODO: register the new account here.
//            OkHttpClient client = new OkHttpClient();
//            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            client.interceptors().add(interceptor);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
//                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ServerEndpoint service = retrofit.create(ServerEndpoint.class);
            Call<GetUserRequestWrapper> call = service.createUser(new GetUserRequestWrapper(new User(mUsername, mPassword), new Profile(mName, mEmail, mPhone, mOccupation)));
            try {
                Response response = call.execute();
                com.squareup.okhttp.Response raw = response.raw();
                Log.d(TAG, String.format("Status: %d. Username: %s ", raw.code(), mUsername));
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            progressDialog.dismiss();

            if (success) {
               onSignupSuccess();
            }
            else {
                onSignupFailed();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            progressDialog.dismiss();
        }
    }
}
