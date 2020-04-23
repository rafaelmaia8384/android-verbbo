package br.com.globalinotec.verbbo;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class ActivityBase extends AppCompatActivity {

    protected DialogHelper dialogHelper;
    protected Gson gson = new Gson();
    protected RetrofitServerEndpoints endpoints = RetrofitClientInstance.getRetrofitInstance().create(RetrofitServerEndpoints.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        dialogHelper = new DialogHelper(this);

        super.onCreate(savedInstanceState);
    }
}
