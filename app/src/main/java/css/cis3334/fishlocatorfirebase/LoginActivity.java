package css.cis3334.fishlocatorfirebase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

/**
 * Created by Heather on 4/24/2017.
 */

public class LoginActivity extends MainActivity {

    Button btnNewUser, btnLogin;
    EditText etEmail, etPassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //old log that said it was an ormal log in
                signIn(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });

        btnNewUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //old log saying the account has been created
                createAccount(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });


    }

    private void signIn(String email, String password){
        //sign in the recurrent user with email and password previously created.
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() { //add to listener
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) { //when failed
                    Toast.makeText(LoginActivity.this, "SignIn--Authentication failed.", Toast.LENGTH_LONG).show();
                } else {
                    //return to MainActivity is login works
                    finish();
                }
            }
        });
    }

    private void createAccount(String email, String password) {
        //create account for new users
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {  //update listener.
                if (!task.isSuccessful()) { //when failed
                    Toast.makeText(LoginActivity.this, "createAccount--Authentication failed.",Toast.LENGTH_LONG).show();
                } else {
                    //return to MainActivity is login works
                    finish();
                }
            }
        });
    }
}
