package br.com.globalinotec.verbbo;

import android.animation.Animator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.tabs.TabLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.globalinotec.verbbo.databinding.ActivityContaBinding;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityConta extends ActivityBase {

    public static final int ACTIVITY_CONTA = 4321;

    private static final int layoutList[] = {

            R.layout.layout_tabview_contaactivity_pager_01,
            R.layout.layout_tabview_contaactivity_pager_02,
            R.layout.layout_tabview_contaactivity_pager_03,
            R.layout.layout_tabview_contaactivity_pager_04,
            R.layout.layout_tabview_contaactivity_pager_05
    };

    private static final String layoutNames[] = {

            "Dash board",
            "Meus portais",
            "Minhas publicações",
            "Minhas revisões",
            "Perfil",
    };

    private ActivityContaBinding binding;
    private PopupMenu popupMenu;
    private ViewPagerAdapter pagerAdapter;
    private ModelUsuario usuario;
    private ImageView currentImage;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_conta);

        usuario = gson.fromJson(getIntent().getStringExtra("usuarioJson"), ModelUsuario.class);

        pagerAdapter = new ViewPagerAdapter(this, layoutList);
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setOffscreenPageLimit(30);

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(layoutNames[0]));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(layoutNames[1]));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(layoutNames[2]));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(layoutNames[3]));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(layoutNames[4]));

        ViewGroup.LayoutParams params = binding.tabLayout.getLayoutParams();
        params.height = AppUtils.dpToPx(this, 40);
        binding.tabLayout.setLayoutParams(params);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                binding.tabLayout.getTabAt(i).select();

                clearFocus();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        popupMenu = new PopupMenu(this, binding.buttonMenu);
        popupMenu.getMenuInflater().inflate(R.menu.menu_conta, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getOrder() == 0) { //Termos de uso

                }
                else if (item.getOrder() == 1) { //Outra opcao

                }
                else if (item.getOrder() == 2) { //Fazer logoff

                    dialogHelper.showProgressDelayed(500, new Runnable() {

                        @Override
                        public void run() {

                            setResult(1);
                            finish();
                        }
                    });
                }

                return false;
            }
        });

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                loadTabPerfil();
            }
        }, 500);
    }

    private void loadTabPerfil() {

        final View perfilButtonMenu = findViewById(R.id.perfilButtonMenu);
        final View perfilButtonCamera = findViewById(R.id.perfilButtonCamera);
        final ImageView perfilImagemPerfil = findViewById(R.id.perfilImagemPerfil);
        final EditText perfilEditPublicanteNome = findViewById(R.id.perfilEditPublicanteNome);
        final EditText perfilEditPublicanteResumo = findViewById(R.id.perfilEditPublicanteResumo);
        final EditText perfilEditPublicanteNomeCompleto = findViewById(R.id.perfilEditPublicanteNomeCompleto);
        final EditText perfilEditPublicanteCPF = findViewById(R.id.perfilEditPublicanteCPF);
        final EditText perfilEditPublicanteDataNascimento = findViewById(R.id.perfilEditPublicanteDataNascimento);
        final EditText perfilEditPublicanteTelefone = findViewById(R.id.perfilEditPublicanteTelefone);
        final View perfilButtonSalvar = findViewById(R.id.perfilButtonSalvar);

        perfilButtonSalvar.setEnabled(false);

        perfilImagemPerfil.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);

        Glide.with(ActivityConta.this)
                .asBitmap()
                .centerCrop()
                .load(usuario.getPathImage())
                .into(perfilImagemPerfil);


        final PopupMenu pMenu = new PopupMenu(this, perfilButtonMenu);
        pMenu.getMenuInflater().inflate(R.menu.menu_conta_perfil, pMenu.getMenu());
        pMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getOrder() == 0) { //Modificar senha de acesso

                    dialogHelper.showProgressDelayed(500, new Runnable() {

                        @Override
                        public void run() {

                            new MaterialDialog.Builder(ActivityConta.this)
                                    .title("Alterar senha de acesso")
                                    .backgroundColor(getResources().getColor(R.color.colorWindowBackground))
                                    .customView(R.layout.layout_contaactivity_perfil_alterar_senha, false)
                                    .positiveText("OK")
                                    .negativeText("Cancelar")
                                    .cancelable(true)
                                    .autoDismiss(false)
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {

                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                            dialog.dismiss();
                                        }
                                    })
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {

                                        @Override
                                        public void onClick(@NonNull final MaterialDialog dialog, @NonNull DialogAction which) {

                                            String senhaAtual = ((EditText)dialog.getCustomView().findViewById(R.id.editSenhaAtual)).getText().toString();
                                            String senha1 = ((EditText)dialog.getCustomView().findViewById(R.id.editSenha1)).getText().toString();
                                            String senha2 = ((EditText)dialog.getCustomView().findViewById(R.id.editSenha2)).getText().toString();

                                            if (senhaAtual.length() < 6 || senhaAtual.length() > 20) {

                                                dialogHelper.showError("Sua senha atual deve conter entre 6 e 20 caracteres.");

                                                return;
                                            }
                                            else if (senha1.length() < 6 || senha1.length() > 20) {

                                                dialogHelper.showError("A nova senha deve conter entre 6 e 20 caracteres.");

                                                return;
                                            }
                                            else if (!senha1.equals(senha2)) {

                                                dialogHelper.showError("As senhas não conferem.");

                                                return;
                                            }

                                            dialogHelper.showProgress();

                                            Call<ModelRequest> call = endpoints.usuariosAlterarSenha(usuario.getToken(), senhaAtual, senha1);

                                            call.enqueue(new Callback<ModelRequest>() {

                                                @Override
                                                public void onResponse(Call<ModelRequest> call, Response<ModelRequest> response) {

                                                    if (response.isSuccessful()) {

                                                        if (response.body().getError() == 0) {

                                                            dialogHelper.dismissProgress();
                                                            dialogHelper.showSuccess("Senha alterada.");

                                                            dialog.dismiss();
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
                                    })
                                    .show();
                        }
                    });
                }
                else if (item.getOrder() == 1) { //Deletar conta

                }

                return false;
            }
        });

        final PopupMenu pCamera = new PopupMenu(this, perfilButtonCamera);
        pCamera.getMenuInflater().inflate(R.menu.menu_imagem_selecionar, pCamera.getMenu());
        pCamera.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getOrder() == 0) { //Selecionar imagem

                    dialogHelper.showProgress();

                    currentImage = perfilImagemPerfil;

                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setInitialCropWindowPaddingRatio(0.05f)
                            .setAspectRatio(1, 1)
                            .setRequestedSize(1024, 1024)
                            .setOutputCompressQuality(90)
                            .setCropMenuCropButtonTitle("OK")
                            .start(ActivityConta.this);
                }

                return false;
            }
        });

        perfilButtonMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                pMenu.show();
            }
        });

        perfilButtonCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                pCamera.show();
            }
        });

        perfilEditPublicanteNome.setText(usuario.getPublicanteNome());
        perfilEditPublicanteNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                perfilButtonSalvar.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        perfilEditPublicanteResumo.setText(usuario.getPublicanteResumo());
        perfilEditPublicanteResumo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                perfilButtonSalvar.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        perfilEditPublicanteNomeCompleto.setText(usuario.getPublicanteNomeCompleto());
        perfilEditPublicanteNomeCompleto.setFocusable(false);
        perfilEditPublicanteNomeCompleto.setInputType(InputType.TYPE_NULL);
        perfilEditPublicanteNomeCompleto.setTextColor(getResources().getColor(R.color.colorTextDark));

        perfilEditPublicanteCPF.addTextChangedListener(Mask.insert(Mask.CPF, perfilEditPublicanteCPF));
        perfilEditPublicanteCPF.setText(usuario.getPublicanteCPF());
        perfilEditPublicanteCPF.setFocusable(false);
        perfilEditPublicanteCPF.setInputType(InputType.TYPE_NULL);
        perfilEditPublicanteCPF.setTextColor(getResources().getColor(R.color.colorTextDark));

        perfilEditPublicanteDataNascimento.setText(AppUtils.formatarDataSimple(usuario.getPublicanteDataNascimento()));
        perfilEditPublicanteDataNascimento.setFocusable(false);
        perfilEditPublicanteDataNascimento.setInputType(InputType.TYPE_NULL);
        perfilEditPublicanteDataNascimento.setTextColor(getResources().getColor(R.color.colorTextDark));

        perfilEditPublicanteTelefone.addTextChangedListener(Mask.insert(Mask.CELULAR, perfilEditPublicanteTelefone));
        perfilEditPublicanteTelefone.setText(usuario.getPublicanteTelefone());
        perfilEditPublicanteTelefone.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                perfilButtonSalvar.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        perfilButtonSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (perfilEditPublicanteNome.getText().toString().length() < 3) {

                    dialogHelper.showError("O nome do publicante deve ter pelo menos 3 caracteres.");

                    return;
                }
                else if (perfilEditPublicanteResumo.getText().toString().split(" ").length < 3) {

                    dialogHelper.showError("O resumo sobre o publicante deve ter mais de 2 palavras.");

                    return;
                }
                else if (perfilEditPublicanteTelefone.getText().toString().length() == 0) {

                    dialogHelper.showError("Informe o número do seu telefone.");

                    return;
                }
                else if (perfilEditPublicanteTelefone.getText().toString().length() < 12) {

                    dialogHelper.showError("Verifique o número do telefone.");

                    return;
                }


                dialogHelper.showProgress();

                boolean novaImagem = imageUri != null;

                Call<ModelRequest> call = endpoints.usuariosAtualizarPerfil(usuario.getToken(), novaImagem, perfilEditPublicanteNome.getText().toString(), perfilEditPublicanteResumo.getText().toString(), perfilEditPublicanteTelefone.getText().toString());

                call.enqueue(new Callback<ModelRequest>() {

                    @Override
                    public void onResponse(Call<ModelRequest> call, Response<ModelRequest> response) {

                        if (response.isSuccessful()) {

                            if (response.body().getError() == 0) {

                                ModelSignedUrls signedUrls = gson.fromJson(gson.toJson(response.body().getData().get(0)), ModelSignedUrls.class);
                                ModelUsuario u = gson.fromJson(gson.toJson(response.body().getData().get(1)), ModelUsuario.class);

                                Intent i = new Intent();
                                i.putExtra("usuarioJson", gson.toJson(u));
                                setResult(0, i);

                                if (signedUrls.getSignedContent().size() > 0) {

                                    List<File> files = new ArrayList<>();
                                    files.add(new File(imageUri.getPath()));

                                    enviarImagens(files, signedUrls.getSignedContent(), new Runnable() {

                                        @Override
                                        public void run() {

                                            dialogHelper.dismissProgress();
                                            dialogHelper.showSuccess("Perfil atualizado.");
                                        }
                                    });
                                }
                                else {

                                    dialogHelper.dismissProgress();
                                    dialogHelper.showSuccess("Perfil atualizado.");
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

                        Log.d("ERROR:", t.getLocalizedMessage());

                        dialogHelper.dismissProgress();
                        dialogHelper.showError("Não foi possível conectar com o servidor. Tente novamente em instantes.");
                    }
                });
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

    public void buttonMenu(View view) {

        popupMenu.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            dialogHelper.dismissProgress();

            if (resultCode == RESULT_OK) {

                final CropImage.ActivityResult result = CropImage.getActivityResult(data);

                AsyncTask.execute(new Runnable() {

                    @Override
                    public void run() {

                        try {

                            new Compressor(ActivityConta.this).setCompressFormat(Bitmap.CompressFormat.JPEG).compressToFile(new File(result.getUri().getPath()));
                        }
                        catch (Exception e) {

                            Log.d("ERROR: ", e.getLocalizedMessage());
                        }
                    }
                });

                currentImage.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
                currentImage.setImageURI(result.getUri());

                imageUri = result.getUri();

                findViewById(R.id.perfilButtonSalvar).setEnabled(true);

                YoYo.with(Techniques.FadeIn)
                        .duration(150)
                        .playOn(currentImage);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void buttonBack(View view) {

        finish();
    }

    private void clearFocus() {

        View root = binding.rootLayout;

        root.requestFocus();
        hideKeyboard(root);
    }

    private static void hideKeyboard(View view) {

        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
