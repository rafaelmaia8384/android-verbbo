package br.com.globalinotec.verbbo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import br.com.globalinotec.verbbo.databinding.ActivityBuscarBinding;
import br.com.globalinotec.verbbo.databinding.ActivityTermosBinding;

public class ActivityBuscar extends ActivityBase {

    public static final int ACTIVITY_BUSCAR = 5811;

    private ActivityBuscarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_buscar);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }


    public void buttonBack(View view) {

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }
}
