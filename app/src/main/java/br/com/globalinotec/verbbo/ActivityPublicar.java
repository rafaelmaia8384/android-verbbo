package br.com.globalinotec.verbbo;

import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.globalinotec.verbbo.databinding.ActivityPublicarBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPublicar extends ActivityBase {

    public static final int ACTIVITY_PUBLICAR = 2345;

    private ActivityPublicarBinding binding;
    private String usuarioJson;
    private ModelUsuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_publicar);

        usuarioJson = getIntent().getStringExtra("usuarioJson");
        usuario = gson.fromJson(usuarioJson, ModelUsuario.class);

        obterMeusPortais();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    private void obterMeusPortais() {

        Call<ModelRequest> call = endpoints.portaisMeusPortais(usuario.getToken());
        call.enqueue(new Callback<ModelRequest>() {

            @Override
            public void onResponse(Call<ModelRequest> call, Response<ModelRequest> response) {

                if (response.isSuccessful()) {

                    if (response.body().getError() == 0) {

                        for (int i = 0; i < response.body().getData().size(); i++) {

                            ModelPortal portal = gson.fromJson(gson.toJson(response.body().getData().get(i)), ModelPortal.class);

                            addPortal(portal);
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

    public void buttonSelecionarVerbbo(View view) {

        binding.cardViewVerbbo.setCardBackgroundColor(getResources().getColor(R.color.colorGreenAccent));

        for (int i = 0; i < binding.layoutPortais.getChildCount(); i++) {

            CardView card = (CardView)((ViewGroup)binding.layoutPortais.getChildAt(i)).getChildAt(0);

            card.setCardBackgroundColor(getResources().getColor(R.color.colorCategoriaUnselected));
        }
    }

    public void buttonCriarPortal(View view) {

        dialogHelper.showProgress();

        Intent i = new Intent(ActivityPublicar.this, ActivityCriarPortal.class);
        i.putExtra("usuarioJson", usuarioJson);
        startActivityForResult(i, ActivityCriarPortal.ACTIVITY_CRIAR_PORTAL);
    }

    public void buttonBack(View view) {

        finish();
    }

    private View.OnClickListener onClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            binding.cardViewVerbbo.setCardBackgroundColor(getResources().getColor(R.color.colorCategoriaUnselected));

            for (int i = 0; i < binding.layoutPortais.getChildCount(); i++) {

                CardView card = (CardView)((ViewGroup)binding.layoutPortais.getChildAt(i)).getChildAt(0);

                card.setCardBackgroundColor(getResources().getColor(R.color.colorCategoriaUnselected));
            }

            CardView card = (CardView) view;
            card.setCardBackgroundColor(getResources().getColor(R.color.colorGreenAccent));
        }
    };

    private void addPortal(ModelPortal portal) {

        binding.layoutPortais.setVisibility(View.VISIBLE);

        View v = LayoutInflater.from(this).inflate(R.layout.layout_portal_layout, null);

        v.findViewById(R.id.cardView).setOnClickListener(onClick);

        Glide.with(ActivityPublicar.this)
                .asBitmap()
                .centerCrop()
                .load(portal.getPathImage())
                .into((ImageView)v.findViewById(R.id.portalImagem));

        ((TextView)v.findViewById(R.id.portalUrl)).setText(portal.getUrl());
        ((TextView)v.findViewById(R.id.portalNome)).setText(portal.getPortalNome());
        ((TextView)v.findViewById(R.id.portalResumo)).setText(portal.getPortalResumo());
        ((TextView)v.findViewById(R.id.portalCreatedAt)).setText("Criado em: " + AppUtils.formatarData(portal.getCreatedAt()));

        binding.layoutPortais.addView(v, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {

        if (requestCode == ActivityCriarPortal.ACTIVITY_CRIAR_PORTAL) {

            dialogHelper.dismissProgress();

            if (data.getStringExtra("portal") != null) {

                ModelPortal portal = gson.fromJson(data.getStringExtra("portal"), ModelPortal.class);

                addPortal(portal);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
