package br.com.globalinotec.verbbo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import br.com.globalinotec.verbbo.databinding.ActivityLoginBinding;
import br.com.globalinotec.verbbo.databinding.ActivityTermosBinding;

public class ActivityTermos extends ActivityBase {

    public static final int ACTIVITY_TERMOS = 9441;

    private ActivityTermosBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_termos);
    }

    public void buttonContinuar(View view) {

        dialogHelper.showProgress();

        Intent i = new Intent(ActivityTermos.this, ActivityCadastrar.class);
        startActivityForResult(i, ActivityCadastrar.ACTIVITY_CADASTRAR);
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

        if (requestCode == ActivityCadastrar.ACTIVITY_CADASTRAR) {

            setResult(resultCode);
            finish();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
