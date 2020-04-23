package br.com.globalinotec.verbbo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Timer;
import java.util.TimerTask;

public class DialogHelper {

    private Activity activity;

    private MaterialDialog progressDialog;
    private MaterialDialog errorDialog;
    private MaterialDialog successDialog;
    private MaterialDialog blockDialog;

    public DialogHelper(Activity activity) {

        this.activity = activity;

        progressDialog = new MaterialDialog.Builder(activity)
                .content("Aguarde...")
                .progress(true, 0)
                .cancelable(false)
                .build();

        successDialog = new MaterialDialog.Builder(activity)
                .title("Sucesso")
                .positiveText("OK")
                .build();

        errorDialog = new MaterialDialog.Builder(activity)
                .title("Desculpe")
                .positiveText("OK")
                .build();

        blockDialog = new MaterialDialog.Builder(activity)
                .title("SAI")
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .build();

        progressDialog.getContentView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        successDialog.getContentView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        errorDialog.getContentView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        blockDialog.getContentView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

        blockDialog.getActionButton(DialogAction.POSITIVE).setVisibility(View.GONE);
    }

    public void showProgress() {

        progressDialog.show();
    }

    public void dismissProgress() {

        progressDialog.dismiss();
    }

    public void showProgressDelayed(final int millisec, final Runnable runnable) {

        showProgress();

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {

                dismissProgress();

                if (runnable == null) return;

                try {

                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            runnable.run();
                        }
                    });
                }
                catch (Exception e) { }
            }
        }, millisec);
    }

    public void showSuccess(String text) {

        successDialog.setContent(text);
        successDialog.show();
    }

    public void showError(String text) {

        errorDialog.setContent(text);
        errorDialog.show();
    }

    public void inputDialog(String title, String text, int inputType, MaterialDialog.InputCallback inputCallback) {

        MaterialDialog input = new MaterialDialog.Builder(activity)
                .title(title)
                .content(text)
                .inputType(inputType)
                .input("", "", inputCallback)
                .build();

        input.getContentView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        input.show();
    }

    public void confirmDialog(boolean cancelable, String title, String text, String negativeText, MaterialDialog.SingleButtonCallback inputCallback) {

        MaterialDialog confirm = new MaterialDialog.Builder(activity)
                .title(title)
                .content(text)
                .positiveText("Sim")
                .negativeText(negativeText)
                .onPositive(inputCallback)
                .cancelable(cancelable)
                .build();

        confirm.getContentView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        confirm.show();
    }

    public void showUpdateDialog() {

        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                .title("Atualização necessária")
                .content("Foi lançada uma versão atualizada deste aplicativo. Atualize para a versão mais nova e continue a utilizar o sistema normalmente.\n\nDeseja atualizar agora?")
                .positiveText("Atualizar agora")
                .negativeText("Cancelar")
                .onPositive(new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        showProgressDelayed(1000, new Runnable() {
                            @Override
                            public void run() {

                                //Intent market = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=br.gov.pb.pm.sasp"));
                                Intent market = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/apps/internaltest/4701468606133452677"));
                                activity.startActivity(market);
                            }
                        });
                    }
                })
                .build();

        dialog.getContentView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        dialog.show();
    }
}
