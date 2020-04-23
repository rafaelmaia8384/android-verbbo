package br.com.globalinotec.verbbo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.imangazaliev.slugify.Slugify;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import br.com.globalinotec.verbbo.databinding.ActivityCriarportalBinding;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCriarPortal extends ActivityBase {

    public static final int ACTIVITY_CRIAR_PORTAL = 2340;

    private ActivityCriarportalBinding binding;
    private ModelUsuario usuario;
    private ImageView currentImage;
    private Slugify slugify = new Slugify();
    private String portalSlug;

    private static final String hint = "Endereço web do portal.";
    private static final String host = ".verbbo.com.br";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_criarportal);

        usuario = gson.fromJson(getIntent().getStringExtra("usuarioJson"), ModelUsuario.class);

        binding.editSlug.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() < 3) {

                    binding.inputSlug.setHint(hint);
                }
                else {

                    portalSlug = slugify.slugify(charSequence.toString());

                    binding.inputSlug.setHint(portalSlug + host);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.spinnerEstilo.setItems(getResources().getStringArray(R.array.portal_estilos));
    }

    public void buttonConcluir(View view) {

        if (currentImage == null || currentImage.getTag() == null) {

            dialogHelper.showError("Selecione a imagem de capa.");

            return;
        }
        else if (binding.editNome.getText().toString().length() == 0) {

            dialogHelper.showError("Defina o nome do portal.");

            return;
        }
        else if (binding.editNome.getText().toString().length() <= 5) {

            dialogHelper.showError("O nome do portal deve ter mais de 5 caracteres.");

            return;
        }
        else if (binding.editResumo.getText().toString().length() == 0) {

            dialogHelper.showError("Escreva o resumo sobre o portal.");

            return;
        }
        else if (binding.editResumo.getText().toString().split(" ").length < 4) {

            dialogHelper.showError("O resumo deve conter mais de 3 palavras.");

            return;
        }
        else if (binding.spinnerEstilo.getSelectedIndex() == 0) {

            dialogHelper.showError("Selecione o tema de cores do portal.");

            return;
        }

        dialogHelper.showProgress();

        Call<ModelRequest> call = endpoints.portaisCriar(usuario.getToken(), portalSlug, binding.editNome.getText().toString(), binding.editResumo.getText().toString(), binding.spinnerEstilo.getSelectedIndex());
        call.enqueue(new Callback<ModelRequest>() {

            @Override
            public void onResponse(Call<ModelRequest> call, Response<ModelRequest> response) {

                if (response.isSuccessful()) {

                    if (response.body().getError() == 0) {

                        //dialogHelper.dismissProgress();

                        final ModelPortal portal = gson.fromJson(gson.toJson(response.body().getData().get(0)), ModelPortal.class);
                        ModelSignedUrls signedUrls = gson.fromJson(gson.toJson(response.body().getData().get(1)), ModelSignedUrls.class);

                        List<File> files = new ArrayList<>();
                        files.add(new File(((Uri)currentImage.getTag()).getPath()));

                        enviarImagens(files, signedUrls.getSignedContent(), new Runnable() {

                            @Override
                            public void run() {

                                Intent i = new Intent();
                                i.putExtra("portal", gson.toJson(portal));

                                setResult(1, i);
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

    public void buttonSelecionarImagem(final View view) {

        PopupMenu p = new PopupMenu(this, view);
        p.getMenuInflater().inflate(R.menu.menu_imagem_selecionar, p.getMenu());

        p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getOrder() == 0) { //Meus cadastros

                    dialogHelper.showProgress();

                    currentImage = (ImageView) view;

                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setInitialCropWindowPaddingRatio(0.05f)
                            .setAspectRatio(10, 6)
                            .setRequestedSize(1024, 1024)
                            .setOutputCompressQuality(90)
                            .setCropMenuCropButtonTitle("OK")
                            .start(ActivityCriarPortal.this);
                }

                return false;
            }
        });

        p.show();
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

                            new Compressor(ActivityCriarPortal.this).setCompressFormat(Bitmap.CompressFormat.JPEG).compressToFile(new File(result.getUri().getPath()));
                        }
                        catch (Exception e) {

                            Log.d("ERROR: ", e.getLocalizedMessage());
                        }
                    }
                });

                currentImage.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
                currentImage.setScaleType(ImageView.ScaleType.FIT_XY);
                currentImage.setImageURI(result.getUri());
                currentImage.setTag(result.getUri());

                YoYo.with(Techniques.FadeIn)
                        .duration(150)
                        .playOn(currentImage);
            }
        }
        else if (requestCode == ActivityCadastrar.ACTIVITY_CADASTRAR) {

            setResult(resultCode);
            finish();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
