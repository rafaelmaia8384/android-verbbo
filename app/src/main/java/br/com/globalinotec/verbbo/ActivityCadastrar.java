package br.com.globalinotec.verbbo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.globalinotec.verbbo.databinding.ActivityCadastrarBinding;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCadastrar extends ActivityBase {

    public static final int ACTIVITY_CADASTRAR = 7221;

    private ActivityCadastrarBinding binding;
    private ImageView currentImage;
    private PopupMenu popupMenu;
    private int tryCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cadastrar);

        popupMenu = new PopupMenu(this, binding.imagemPerfil);
        popupMenu.getMenuInflater().inflate(R.menu.menu_imagem_selecionar, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getOrder() == 0) { //Meus cadastros

                    dialogHelper.showProgress();

                    currentImage = binding.imagemPerfil;

                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setInitialCropWindowPaddingRatio(0.05f)
                            .setAspectRatio(1, 1)
                            .setRequestedSize(1024, 1024)
                            .setOutputCompressQuality(90)
                            .setCropMenuCropButtonTitle("OK")
                            .start(ActivityCadastrar.this);
                }

                return false;
            }
        });

        binding.editPublicanteCPF.addTextChangedListener(Mask.insert(Mask.CPF, binding.editPublicanteCPF));
        binding.editPublicanteDataNascimento.addTextChangedListener(Mask.insert(Mask.DATA, binding.editPublicanteDataNascimento));
        binding.editPublicanteTelefone.addTextChangedListener(Mask.insert(Mask.CELULAR, binding.editPublicanteTelefone));
    }

    public void buttonSelecionarImagemPerfil(final View view) {

        popupMenu.show();
    }

    public void buttonConcluir(View view) {

        if (currentImage == null || currentImage.getTag() == null) {

            dialogHelper.showError("Selecione a imagem de perfil.");

            return;
        }

        String publicanteNome = binding.editPublicanteNome.getText().toString();
        String publicanteResumo = binding.editPublicanteResumo.getText().toString();
        String publicanteNomeCompleto = binding.editPublicanteNomeCompleto.getText().toString();
        String publicanteCPF = binding.editPublicanteCPF.getText().toString();
        String publicanteDataNascimento = binding.editPublicanteDataNascimento.getText().toString();
        String publicanteTelefone = binding.editPublicanteTelefone.getText().toString();
        String email = binding.editEmail.getText().toString();
        String senha1 = binding.editSenha1.getText().toString();
        String senha2 = binding.editSenha2.getText().toString();

        if (publicanteNome.length() < 3) {

            dialogHelper.showError("O nome do publicante deve ter pelo menos 3 caracteres.");

            return;
        }
        else if (publicanteResumo.split(" ").length < 3) {

            dialogHelper.showError("O resumo sobre o publicante deve ter mais de 2 palavras.");

            return;
        }
        else if (publicanteNomeCompleto.split(" ").length < 2) {

            dialogHelper.showError("O nome completo deve ter mais de 1 palavra.");

            return;
        }
        else if (publicanteCPF.length() == 0) {

            dialogHelper.showError("Informe o CPF.");

            return;
        }
        else if (!AppUtils.validarCPF(publicanteCPF)) {

            dialogHelper.showError("CPF inválido.");

            return;
        }
        else if (publicanteResumo.split(" ").length < 3) {

            dialogHelper.showError("O resumo do publicante deve ter mais de 2 palavras.");

            return;
        }
        else if (publicanteDataNascimento.length() < 10) {

            dialogHelper.showError("Verifique a data de nascimento.");

            return;
        }
        else if (publicanteTelefone.length() == 0) {

            dialogHelper.showError("Informe o número do seu telefone.");

            return;
        }
        else if (publicanteTelefone.length() < 12) {

            dialogHelper.showError("Verifique o número do telefone.");

            return;
        }
        else if (!AppUtils.validarEmail(email)) {

            dialogHelper.showError("Verifique seu email.");

            return;
        }
        else if (senha1.length() < 6 || senha1.length() > 20) {

            dialogHelper.showError("Sua senha deve conter entre 6 e 20 caracteres.");

            return;
        }
        else if (!senha1.equals(senha2)) {

            dialogHelper.showError("As senhas não conferem.");

            return;
        }

        if (publicanteDataNascimento.length() == 10) {

            try {

                int errorCount = 0;

                String[] dataParts = publicanteDataNascimento.split("/");

                if (Integer.parseInt(dataParts[0]) < 1 || Integer.parseInt(dataParts[0]) > 31) {

                    errorCount++;
                }
                else if (Integer.parseInt(dataParts[1]) < 1 || Integer.parseInt(dataParts[1]) > 12) {

                    errorCount++;
                }
                else if (dataParts[2].length() > 0 && dataParts[2].length() < 4) {

                    dialogHelper.showError("Verifique a data de nascimento.");

                    return;
                }
                else if (Integer.parseInt(dataParts[2]) < 1940 || Integer.parseInt(dataParts[2]) > 2015) {

                    errorCount++;
                }

                if (errorCount > 0) {

                    dialogHelper.showError("Verifique a data de nascimento.");

                    return;
                }
            }
            catch (Exception e) {

                dialogHelper.showError("Verifique a data de nascimento.");

                return;
            }
        }

        dialogHelper.showProgress();

        Call<ModelRequest> call = endpoints.usuariosCadastrar(
                binding.editPublicanteNome.getText().toString(),
                binding.editPublicanteResumo.getText().toString(),
                binding.editPublicanteNomeCompleto.getText().toString(),
                binding.editPublicanteCPF.getText().toString(),
                binding.editPublicanteDataNascimento.getText().toString(),
                binding.editPublicanteTelefone.getText().toString(),
                binding.editEmail.getText().toString(),
                binding.editSenha1.getText().toString());

        call.enqueue(new Callback<ModelRequest>() {

            @Override
            public void onResponse(Call<ModelRequest> call, Response<ModelRequest> response) {

                if (response.isSuccessful()) {

                    if (response.body().getError() == 0) {

                        ModelSignedUrls signedUrls = gson.fromJson(gson.toJson(response.body().getData().get(0)), ModelSignedUrls.class);

                        List<File> files = new ArrayList<>();
                        files.add(new File(((Uri)currentImage.getTag()).getPath()));

                        enviarImagens(files, signedUrls.getSignedContent(), new Runnable() {

                            @Override
                            public void run() {

                                setResult(1);
                                finish();
                            }
                        });
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

                Log.d("ERROR:", t.getLocalizedMessage());

                dialogHelper.dismissProgress();
                dialogHelper.showError("Não foi possível conectar com o servidor. Tente novamente em instantes.");
            }
        });
    }

    private void enviarImagens(final List<File> files, final List<String> signedUrls, final Runnable callback) {

        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {

                try {

                    int retry = 0;

                    for (int i = 0; i < signedUrls.size(); i++) {

                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), files.get(i));
                        Call<Void> call = endpoints.enviarImagem(signedUrls.get(i), requestBody);

                        while (retry++ < 3) {

                            if (call.execute().isSuccessful()) {

                                break;
                            }
                        }

                        retry = 0;
                    }

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            if (callback != null) callback.run();
                        }
                    });
                }
                catch (Exception e) {

                    Log.d("ERROR: ", e.getLocalizedMessage());
                }
            }
        });
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

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            dialogHelper.dismissProgress();

            if (resultCode == RESULT_OK) {

                final CropImage.ActivityResult result = CropImage.getActivityResult(data);

                AsyncTask.execute(new Runnable() {

                    @Override
                    public void run() {

                        try {

                            new Compressor(ActivityCadastrar.this).setCompressFormat(Bitmap.CompressFormat.JPEG).compressToFile(new File(result.getUri().getPath()));
                        }
                        catch (Exception e) {

                            Log.d("ERROR: ", e.getLocalizedMessage());
                        }
                    }
                });

                currentImage.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
                currentImage.setImageURI(result.getUri());
                currentImage.setTag(result.getUri());

                YoYo.with(Techniques.FadeIn)
                        .duration(150)
                        .playOn(currentImage);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
