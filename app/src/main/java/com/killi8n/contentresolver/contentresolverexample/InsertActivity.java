package com.killi8n.contentresolver.contentresolverexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InsertActivity extends AppCompatActivity {


    EditText et_country;
    EditText et_capital;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        et_country = (EditText)findViewById(R.id.et_country);
        et_capital = (EditText)findViewById(R.id.et_capital);

        Intent i = getIntent();
        String country = i.getStringExtra("country");
        String capital = i.getStringExtra("capital");

        et_country.setText(country);
        et_capital.setText(capital);

        Button btn = (Button)findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getIntent();
                i.putExtra("country", et_country.getText().toString());
                i.putExtra("capital", et_capital.getText().toString());
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode)
        {
            setResult(RESULT_CANCELED, getIntent());
        }
        return super.onKeyDown(keyCode, event);
    }
}
