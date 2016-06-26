package in.codme.axisvoiceadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button loginbutton;
    TextView signupbtn;
    EditText username,passwordet;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    final String TAG="LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.login_layout_title_bar);
        loginbutton=(Button)findViewById(R.id.loginbtn);
        username=(EditText)findViewById(R.id.usernamelg);
        passwordet=(EditText)findViewById(R.id.passwordlg);

        mAuth = FirebaseAuth.getInstance();


        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null)
        {
            Log.d(TAG, " signed in");
            // User is signed in
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // User is signed out
            Log.d(TAG, "not signed in");

        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");


                }
                // ...
            }
        };


        SharedPreferences sharedPreferences=getSharedPreferences("APP_DATA",MODE_PRIVATE);

        System.out.println("username is"+sharedPreferences.getString("useremail",""));
        System.out.println("pwd is"+sharedPreferences.getString("password",""));

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = username.getText().toString();
                String password = passwordet.getText().toString();


                email = email.trim();
                password = password.trim();

                if (email.isEmpty() || password.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(R.string.login_error_message)
                            .setTitle(R.string.login_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    final String emailAddress = email;
                    final String passwordtemp=password;
                    final   ProgressDialog progressDialog=new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Authenticating");
                    progressDialog.show();
                    //Login with an email/password combination
                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                    progressDialog.dismiss();
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Log.w(TAG, "signInWithEmail", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        SharedPreferences sharedPreferences=getSharedPreferences("APP_DATA",MODE_PRIVATE);
                                        SharedPreferences.Editor editor=sharedPreferences.edit();
                                        editor.putString("useremail",emailAddress);
                                        editor.putString("password",passwordtemp);
                                        editor.commit();
                                        Toast.makeText(LoginActivity.this, "Authentication Success.",
                                                Toast.LENGTH_SHORT).show();

                                        System.out.println("username is"+sharedPreferences.getString("useremail",""));
                                        System.out.println("pwd is"+sharedPreferences.getString("password",""));
                                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    // ...
                                }
                            });



                }

            }
        });




    }
}
