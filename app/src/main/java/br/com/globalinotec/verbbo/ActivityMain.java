package br.com.globalinotec.verbbo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.tabs.TabLayout;

import br.com.globalinotec.verbbo.databinding.ActivityMainBinding;

public class ActivityMain extends ActivityBase {

    private static final int layoutList[] = {

            R.layout.layout_tabview_mainactivity_pager_01,
            R.layout.layout_tabview_mainactivity_pager_02,
            R.layout.layout_tabview_mainactivity_pager_03,
            R.layout.layout_tabview_mainactivity_pager_04,
            R.layout.layout_tabview_mainactivity_pager_05,
            R.layout.layout_tabview_mainactivity_pager_06,
            R.layout.layout_tabview_mainactivity_pager_07
    };

    private static final String layoutNames[] = {

            "Explorar",
            "Política",
            "Economia",
            "Saúde",
            "Segurança",
            "Esportes",
            "Mais" //Engloba: Ciência e tecnologia, Artes, Turismo, Moda e Beleza, Entretenimento, Educação.
    };

    private ActivityMainBinding binding;
    private ModelUsuario usuario;
    private ViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        pagerAdapter = new ViewPagerAdapter(this, layoutList);
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setOffscreenPageLimit(30);

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(layoutNames[0]));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(layoutNames[1]));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(layoutNames[2]));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(layoutNames[3]));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(layoutNames[4]));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(layoutNames[5]));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(layoutNames[6]));

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

        binding.editBusca.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    buttonFecharBusca(null);

                    dialogHelper.showProgress();

                    final Intent i = new Intent(ActivityMain.this, ActivityBuscar.class);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            startActivityForResult(i, ActivityBuscar.ACTIVITY_BUSCAR);
                        }
                    }, 500);

                    return true;
                }

                return false;
            }
        });
    }

    public void buttonConta(View view) {

        dialogHelper.showProgress();

        if (usuario == null) {

            Intent i = new Intent(ActivityMain.this, ActivityLogin.class);
            startActivityForResult(i, ActivityLogin.ACTIVITY_LOGIN);
        }
        else {

            Intent i = new Intent(ActivityMain.this, ActivityConta.class);
            i.putExtra("usuarioJson", gson.toJson(usuario));
            startActivityForResult(i, ActivityConta.ACTIVITY_CONTA);
        }
    }

    public void buttonEditar(View view) {

        dialogHelper.showProgress();

        if (usuario != null) {

            Intent i = new Intent(ActivityMain.this, ActivityEditar.class);
            i.putExtra("usuarioJson", gson.toJson(usuario));
            startActivityForResult(i, ActivityEditar.ACTIVITY_EDITAR);
        }
        else {

            Intent i = new Intent(ActivityMain.this, ActivityLogin.class);
            i.putExtra("editar", true);
            startActivityForResult(i, ActivityLogin.ACTIVITY_LOGIN);
        }
    }

    public void buttonBuscar(View view) {

        binding.layoutBusca.setVisibility(View.VISIBLE);
        binding.layoutLogo.setVisibility(View.GONE);
        binding.layoutMenu.setVisibility(View.GONE);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        binding.layoutBusca.requestFocus();
    }

    public void buttonFecharBusca(View view) {

        binding.layoutBusca.setVisibility(View.GONE);
        binding.layoutLogo.setVisibility(View.VISIBLE);
        binding.layoutMenu.setVisibility(View.VISIBLE);

        binding.editBusca.setText("");

        clearFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == ActivityEditar.ACTIVITY_EDITAR) {

            dialogHelper.dismissProgress();
        }
        else if (requestCode == ActivityLogin.ACTIVITY_LOGIN) {

            dialogHelper.dismissProgress();

            if (resultCode == 1) {

                usuario = gson.fromJson(data.getStringExtra("usuarioJson"), ModelUsuario.class);
            }
            else if (resultCode == 2) {

                usuario = gson.fromJson(data.getStringExtra("usuarioJson"), ModelUsuario.class);

                Intent i = new Intent(ActivityMain.this, ActivityEditar.class);
                i.putExtra("usuarioJson", data.getStringExtra("usuarioJson"));
                startActivityForResult(i, ActivityEditar.ACTIVITY_EDITAR);
            }
        }
        else if (requestCode == ActivityConta.ACTIVITY_CONTA) {

            dialogHelper.dismissProgress();

            if (resultCode == 1) { //Fez logoff

                usuario = null;
            }
        }
        else if (requestCode == ActivityBuscar.ACTIVITY_BUSCAR) {

            dialogHelper.dismissProgress();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {

        clearFocus();

        super.onPause();
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
