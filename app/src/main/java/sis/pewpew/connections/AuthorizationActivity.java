package sis.pewpew.connections;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sis.pewpew.R;

public class AuthorizationActivity extends AppCompatActivity implements View.OnClickListener {

        private FirebaseAuth mAuth;
        private FirebaseAuth.AuthStateListener mAuthListener;
        private static final String TAG = "Login";
        private EditText mEmailField;
        private EditText mPasswordField;
        private EditText mNicknameField;


        @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.signInButton).setOnClickListener(this);
        findViewById(R.id.createAccountButton).setOnClickListener(this);


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
        // ...
    }

        @Override
        public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

        @Override
        public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(AuthorizationActivity.this, "Ошибка входа",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


    protected boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Необходим email-адрес");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Необходим пароль");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        /*String nickname = mNicknameField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mNicknameField.setError("Необходимо имя пользователя");
            valid = false;
        } else {
            mNicknameField.setError(null);
        }*/

        return valid;
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.createAccountButton) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.signInButton) {
            Intent intent = new Intent(AuthorizationActivity.this, SignInActivity.class);
            startActivity(intent);
        }
    }
}
