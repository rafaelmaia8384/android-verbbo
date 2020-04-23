package br.com.globalinotec.verbbo;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import br.com.globalinotec.verbbo.databinding.ActivityEditarBinding;
import id.zelory.compressor.Compressor;

public class ActivityEditar extends ActivityBase {

    public static final int ACTIVITY_EDITAR = 1234;

    private ActivityEditarBinding binding;
    private PopupWindow popupMenu;
    private ImageView currentImage;
    private String usuarioJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_editar);

        usuarioJson = getIntent().getStringExtra("usuarioJson");

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_editar_menu, null);

        popupMenu = new PopupWindow(view, AppUtils.dpToPx(this, 250), LinearLayout.LayoutParams.WRAP_CONTENT, false);
        popupMenu.setOutsideTouchable(true);
        popupMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        YoYo.with(Techniques.FadeOut)
                                .duration(150)
                                .onEnd(new YoYo.AnimatorCallback() {
                                    @Override
                                    public void call(Animator animator) {
                                        binding.layoutPopupBackground.setVisibility(View.GONE);
                                    }
                                })
                                .playOn(binding.layoutPopupBackground);
                    }
                }, 100);
            }
        });

        dialogHelper.showSuccess("Mostrar mensagem falando sobre a edição de publicações.\n\nMostrar opção para não mostrar novamente.");
    }

    @Override
    public void onBackPressed() {

        if (popupMenu.isShowing()) {

            popupMenu.dismiss();
        }
        else super.onBackPressed();
    }

    public void buttonContinuar(View view) {

        showDialogCategorias();
    }

    private void showDialogCategorias() {

        dialogHelper.showProgressDelayed(500, new Runnable() {

            @Override
            public void run() {

                new MaterialDialog.Builder(ActivityEditar.this)
                        .title("Categorias")
                        .autoDismiss(false)
                        .backgroundColor(getResources().getColor(R.color.colorWindowBackground))
                        .customView(R.layout.layout_editar_categorias, false)
                        .negativeText("Cancelar")
                        .positiveText("OK")
                        .cancelable(true)
                        .showListener(new DialogInterface.OnShowListener() {

                            @Override
                            public void onShow(DialogInterface dialog) {

                                View content = ((MaterialDialog)dialog).getCustomView();

                                View.OnClickListener onClick = new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {

                                        CardView card = (CardView) view;

                                        if (card.getTag() == null) {

                                            card.setCardBackgroundColor(getResources().getColor(R.color.colorGreenAccent));
                                            card.setTag(1);
                                        }
                                        else if ((int)card.getTag() == 0) {

                                            card.setCardBackgroundColor(getResources().getColor(R.color.colorGreenAccent));
                                            card.setTag(1);
                                        }
                                        else if ((int)card.getTag() == 1) {

                                            card.setCardBackgroundColor(getResources().getColor(R.color.colorCategoriaUnselected));
                                            card.setTag(0);
                                        }

//                                        Drawable background = card.getCardBackgroundColor();
//
//                                        if (background instanceof ColorDrawable) {
//
//                                            if (((ColorDrawable)background).getColor() == getResources().getColor(R.color.colorCategoriaTransparent)) {
//
//                                                view.setBackgroundColor(getResources().getColor(R.color.colorGreenAccent));
//                                                view.setTag(1);
//                                            }
//                                            else {
//
//                                                view.setBackgroundColor(getResources().getColor(R.color.colorCategoriaTransparent));
//                                                view.setTag(0);
//                                            }
//                                        }
                                    }
                                };

                                content.findViewById(R.id.categoria01).setOnClickListener(onClick);
                                content.findViewById(R.id.categoria02).setOnClickListener(onClick);
                                content.findViewById(R.id.categoria03).setOnClickListener(onClick);
                                content.findViewById(R.id.categoria04).setOnClickListener(onClick);
                                content.findViewById(R.id.categoria05).setOnClickListener(onClick);
                                content.findViewById(R.id.categoria06).setOnClickListener(onClick);
                                content.findViewById(R.id.categoria07).setOnClickListener(onClick);
                                content.findViewById(R.id.categoria08).setOnClickListener(onClick);
                                content.findViewById(R.id.categoria09).setOnClickListener(onClick);
                                content.findViewById(R.id.categoria10).setOnClickListener(onClick);
                                content.findViewById(R.id.categoria11).setOnClickListener(onClick);
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                dialog.dismiss();
                            }
                        })
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                View content = dialog.getCustomView();

                                int count = 0;

                                Object tagCat01 = content.findViewById(R.id.categoria01).getTag();
                                Object tagCat02 = content.findViewById(R.id.categoria02).getTag();
                                Object tagCat03 = content.findViewById(R.id.categoria03).getTag();
                                Object tagCat04 = content.findViewById(R.id.categoria04).getTag();
                                Object tagCat05 = content.findViewById(R.id.categoria05).getTag();
                                Object tagCat06 = content.findViewById(R.id.categoria06).getTag();
                                Object tagCat07 = content.findViewById(R.id.categoria07).getTag();
                                Object tagCat08 = content.findViewById(R.id.categoria08).getTag();
                                Object tagCat09 = content.findViewById(R.id.categoria09).getTag();
                                Object tagCat10 = content.findViewById(R.id.categoria10).getTag();
                                Object tagCat11 = content.findViewById(R.id.categoria11).getTag();

                                if (tagCat01 != null && (int)tagCat01 > 0) count++;
                                if (tagCat02 != null && (int)tagCat02 > 0) count++;
                                if (tagCat03 != null && (int)tagCat03 > 0) count++;
                                if (tagCat04 != null && (int)tagCat04 > 0) count++;
                                if (tagCat05 != null && (int)tagCat05 > 0) count++;
                                if (tagCat06 != null && (int)tagCat06 > 0) count++;
                                if (tagCat07 != null && (int)tagCat07 > 0) count++;
                                if (tagCat08 != null && (int)tagCat08 > 0) count++;
                                if (tagCat09 != null && (int)tagCat09 > 0) count++;
                                if (tagCat10 != null && (int)tagCat10 > 0) count++;
                                if (tagCat11 != null && (int)tagCat11 > 0) count++;

                                if (count == 0) {

                                    dialogHelper.showError("Selecione pelo menos uma categoria.");
                                }
                                else if (count > 3) {

                                    dialogHelper.showError("Você não pode selecionar mais de 3 categorias para sua publicação.");
                                }
                                else {

                                    dialog.dismiss();

                                    showDialogPalavrasChave();
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private void showDialogPalavrasChave() {

        dialogHelper.showProgressDelayed(500, new Runnable() {

            @Override
            public void run() {

                new MaterialDialog.Builder(ActivityEditar.this)
                        .title("Palavras-chaves")
                        .autoDismiss(false)
                        .backgroundColor(getResources().getColor(R.color.colorWindowBackground))
                        .customView(R.layout.layout_editar_palavraschaves, false)
                        .negativeText("Cancelar")
                        .positiveText("OK")
                        .cancelable(true)
                        .showListener(new DialogInterface.OnShowListener() {

                            @Override
                            public void onShow(final DialogInterface dialog) {

                                final View content = ((MaterialDialog)dialog).getCustomView();
                                final TextInputLayout ti = content.findViewById(R.id.inputPalavra);
                                final EditText editPalavra = content.findViewById(R.id.editPalavra);

                                final View.OnClickListener onClick = new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {

                                        final TextInputLayout ti = content.findViewById(R.id.inputPalavra);

                                        if (ti.getError() != null) {

                                            return;
                                        }

                                        final ChipGroup chipGroup = content.findViewById(R.id.chipGroup);
                                        EditText editPalavra = content.findViewById(R.id.editPalavra);

                                        if (chipGroup.getChildCount() >= 20) {

                                            dialogHelper.showError("Você não pode adicionar mais de 20 palavras chaves.");

                                            return;
                                        }

                                        String palavra = editPalavra.getText().toString();

                                        if (palavra.length() < 3) {

                                            dialogHelper.showError("A palavra deve conter mais de 2 letras.");

                                            return;
                                        }
                                        else {

                                            for (int i = 0; i < chipGroup.getChildCount(); i++) {

                                                Chip c = (Chip) chipGroup.getChildAt(i);

                                                if (c != null) {

                                                    if (c.getText().toString().equals(palavra)) {

                                                        dialogHelper.showError("Essa palavra já foi adicionada.");

                                                        return;
                                                    }
                                                }
                                            }
                                        }

                                        final Chip chip = new Chip(ActivityEditar.this);
                                        chip.setText(palavra);
                                        chip.setCloseIconVisible(true);

                                        chip.setChipBackgroundColorResource(R.color.colorGrey);
                                        chip.setClickable(true);
                                        chip.setCheckable(false);
                                        chip.setOnCloseIconClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View view) {

                                                chipGroup.removeView(chip);
                                            }
                                        });

                                        chipGroup.addView(chip);

                                        editPalavra.setText("");
                                    }
                                };

                                editPalavra.addTextChangedListener(new TextWatcher() {

                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                        if (charSequence.length() < 3) {

                                            ti.setError("Mínimo de 3 caracteres.");
                                        }
                                        else {

                                            ti.setError(null);
                                        }
                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {

                                    }
                                });

                                editPalavra.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                                    @Override
                                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                                        if (actionId == EditorInfo.IME_ACTION_SEND) {

                                            onClick.onClick(null);

                                            return true;
                                        }

                                        return false;
                                    }
                                });

                                content.findViewById(R.id.buttonAdd).setOnClickListener(onClick);
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                dialog.dismiss();
                            }
                        })
                        .onPositive(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                View content = dialog.getCustomView();

                                ChipGroup chipGroup = content.findViewById(R.id.chipGroup);

                                if (chipGroup.getChildCount() < 3) {

                                    dialogHelper.showError("Adicione pelo menos 3 palavras-chaves.");

                                    return;
                                }

                                dialog.dismiss();

                                dialogHelper.showProgress();

                                final Intent i = new Intent(ActivityEditar.this, ActivityPublicar.class);
                                i.putExtra("usuarioJson", usuarioJson);

                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {

                                        startActivityForResult(i, ActivityPublicar.ACTIVITY_PUBLICAR);
                                    }
                                }, 500);
                            }
                        })
                        .show();
            }
        });
    }

    public void buttonExcluir(final View view) {

        PopupMenu p = new PopupMenu(this, view);
        p.getMenuInflater().inflate(R.menu.menu_elemento_excluir, p.getMenu());

        p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getOrder() == 0) { //Meus cadastros

                    final ViewGroup v = (ViewGroup)view.getParent().getParent().getParent().getParent().getParent();

                    YoYo.with(Techniques.FadeOut)
                            .duration(150)
                            .onEnd(new YoYo.AnimatorCallback() {

                                @Override
                                public void call(Animator animator) {

                                    v.removeView((View)view.getParent().getParent().getParent().getParent());
                                }
                            })
                            .playOn((View)view.getParent().getParent().getParent().getParent());
                }

                return false;
            }
        });

        p.show();
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
                            .start(ActivityEditar.this);
                }

                return false;
            }
        });

        p.show();
    }

    public void buttonMenu(View view) {

        popupMenu.showAsDropDown(binding.menuAdicionar, 0, AppUtils.dpToPx(this, -40));
        binding.layoutPopupBackground.setVisibility(View.VISIBLE);

        YoYo.with(Techniques.FadeIn)
                .duration(150)
                .playOn(binding.layoutPopupBackground);
    }

    public void buttonPopupItemClick(View view) {

        popupMenu.dismiss();

        String item = (String) view.getTag();
        View v;

        if (item.equals("0")) { // Parágrafo

            v = LayoutInflater.from(this).inflate(R.layout.layout_editar_add_paragrafo, null);
        }
        else if (item.equals("1")) { // Imagem

            v = LayoutInflater.from(this).inflate(R.layout.layout_editar_add_imagem, null);
        }
        else if (item.equals("2")) { // Carousel de Imagens

            v = LayoutInflater.from(this).inflate(R.layout.layout_editar_add_imagem_carousel, null);
        }
        else if (item.equals("3")) { // Texto forte

            v = LayoutInflater.from(this).inflate(R.layout.layout_editar_add_texto_forte, null);
        }
        else if (item.equals("4")) { // Texto italico

            v = LayoutInflater.from(this).inflate(R.layout.layout_editar_add_texto_italico, null);
        }
        else if (item.equals("5")) { // Citacao

            v = LayoutInflater.from(this).inflate(R.layout.layout_editar_add_citacao, null);
        }
        else if (item.equals("6")) { // Áudio

            v = LayoutInflater.from(this).inflate(R.layout.layout_editar_add_audio, null);
        }
        else if (item.equals("7")) { // Hyperlink

            v = LayoutInflater.from(this).inflate(R.layout.layout_editar_add_hyperlink, null);
        }
        else { // Video do youtube

            v = LayoutInflater.from(this).inflate(R.layout.layout_editar_add_videoyoutube, null);
        }

        binding.layoutConteudo.addView(v);

        YoYo.with(Techniques.FadeIn)
                .duration(150)
                .playOn(v);

        binding.scrollView.post(new Runnable() {

            @Override
            public void run() {

                binding.scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
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

                            new Compressor(ActivityEditar.this).setCompressFormat(Bitmap.CompressFormat.JPEG).compressToFile(new File(result.getUri().getPath()));
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
        else if (requestCode == ActivityPublicar.ACTIVITY_PUBLICAR) {

            dialogHelper.dismissProgress();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
