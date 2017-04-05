package sis.pewpew.connections;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import sis.pewpew.MainActivity;
import sis.pewpew.R;
import sis.pewpew.utils.ProgressDialogActivity;

public class AuthorizationActivity extends ProgressDialogActivity implements View.OnClickListener {

        private FirebaseAuth mAuth;
        private FirebaseAuth.AuthStateListener mAuthListener;
        private static final String TAG = "Login";
        protected EditText mEmailField;
        protected EditText mPasswordField;
        //private EditText mNicknameField;


        @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.signInButton).setOnClickListener(this);
        findViewById(R.id.createAccountButton).setOnClickListener(this);
            mEmailField = (EditText) findViewById(R.id.createEmail);
            mPasswordField = (EditText) findViewById(R.id.createPassword);


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
                updateUI(user);
            }
        };
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

    private void sendEmailVerification() {

        final FirebaseUser user = mAuth.getCurrentUser();
        //Maybe
        assert user != null;
        //Maybe
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(AuthorizationActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(AuthorizationActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        hideProgressDialog();

                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                mPasswordField.setError("Ваш пароль слишком слаб");
                                mPasswordField.requestFocus();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                mEmailField.setError("Пожалуйста, проверьте корректность предоставленных данных");
                                mEmailField.requestFocus();
                            } catch(FirebaseAuthUserCollisionException e) {
                                mEmailField.setError("Пользователь с таким email уже существует");
                                mEmailField.requestFocus();
                            } catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
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
        } else if (TextUtils.isDigitsOnly(password)) {
            mPasswordField.setError("Пароль должен содержать буквы");
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

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            sendEmailVerification();
            Intent intent = new Intent(AuthorizationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
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
