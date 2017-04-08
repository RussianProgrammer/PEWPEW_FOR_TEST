package sis.pewpew;

import android.os.Bundle;
import sis.pewpew.connections.AuthorizationActivity;

public class WelcomeActivity extends AuthorizationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }
}
