package br.com.globalinotec.verbbo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent i = new Intent(ActivitySplash.this, ActivityMain.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }
}
