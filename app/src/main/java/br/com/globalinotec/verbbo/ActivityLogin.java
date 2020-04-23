package br.com.globalinotec.verbbo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;

import java.io.File;

import br.com.globalinotec.verbbo.databinding.ActivityLoginBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends ActivityBase {

    public static final int ACTIVITY_LOGIN = 7654;

    private ActivityLoginBinding binding;
    private String usuarioJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        binding.editSenha.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_SEND) {

                    buttonEntrar(null);

                    return true;
                }

                return false;
            }
        });
    }

    public void buttonCadastrar(View view) {

        dialogHelper.showProgress();

        Intent i = new Intent(ActivityLogin.this, ActivityTermos.class);
        startActivityForResult(i, ActivityTermos.ACTIVITY_TERMOS);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    public void buttonEntrar(View view) {

        String email = binding.editEmail.getText().toString();
        String senha = binding.editSenha.getText().toString();

        if (email.length() == 0) {

            dialogHelper.showError("Digite o email.");

            return;
        }
        else if (!AppUtils.validarEmail(email)) {

            dialogHelper.showError("Verifique o email digitado.");

            return;
        }
        else if (senha.length() < 6 || senha.length() > 20) {

            dialogHelper.showError("A senha deve conter entre 6 e 20 dígitos.");

            return;
        }

        dialogHelper.showProgress();

        Call<ModelRequest> call = endpoints.usuariosLogin(email, senha);
        call.enqueue(new Callback<ModelRequest>() {

            @Override
            public void onResponse(Call<ModelRequest> call, Response<ModelRequest> response) {

                if (response.isSuccessful()) {

                    if (response.body().getError() == 0) {

                        usuarioJson = gson.toJson(response.body().getData().get(0));

                        if (getIntent().getBooleanExtra("editar", false)) {

                            Intent i = new Intent();
                            i.putExtra("usuarioJson", usuarioJson);
                            setResult(2, i);

                            finish();
                        }
                        else {

                            Intent i = new Intent(ActivityLogin.this, ActivityConta.class);
                            i.putExtra("usuarioJson", usuarioJson);

                            startActivityForResult(i, ActivityConta.ACTIVITY_CONTA);
                        }
                    }
                    else if (response.body().getError() == 2) {

                        dialogHelper.dismissProgress();
                        dialogHelper.showUpdateDialog();
                    }
                    else {

                        dialogHelper.dismissProgress();
                        dialogHelper.showError(response.body().getMsg());
                    }
                }
                else {

                    dialogHelper.dismissProgress();
                    dialogHelper.showError("Não foi possível contectar com o servidor. Tente novamente em instantes.");
                }
            }

            @Override
            public void onFailure(Call<ModelRequest> call, Throwable t) {

                dialogHelper.dismissProgress();
                dialogHelper.showError("Não foi possível conectar com o servidor. Tente novamente em instantes.");
            }
        });
    }

    public void buttonBack(View view) {

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {

        if (requestCode == ActivityTermos.ACTIVITY_TERMOS) {

            dialogHelper.dismissProgress();

            if (resultCode == 1) {

                dialogHelper.showSuccess("Cadastro realizado.\n\nInsira seu email e senha para realizar o login.");
            }
        }
        if (requestCode == ActivityConta.ACTIVITY_CONTA) {

            if (resultCode == 1) { // Logoff

                dialogHelper.dismissProgress();
            }
            else { // Logado ou perfil atualizado

                Intent i = new Intent();

                if (data != null) {

                    i.putExtra("usuarioJson", data.getStringExtra("usuarioJson"));
                    setResult(1, i);
                }
                else {

                    i.putExtra("usuarioJson", usuarioJson);
                    setResult(1, i);
                }

                finish();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
