package globalsolutions.findemes.pantallas.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import globalsolutions.findemes.R;
import globalsolutions.findemes.database.util.MyDatabaseHelper;
import globalsolutions.findemes.pantallas.util.FileDialog;
import globalsolutions.findemes.pantallas.util.Util;


/**
 * Created by Manuel on 23/02/2015.
 */
public class OptionActivityAyuda extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ayuda);

        //boton retroceder
        ImageButton btnReturn = (ImageButton) findViewById(R.id.btnBackButton);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                backActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        backActivity();
    }

    private void backActivity(){
        Intent in = new Intent(OptionActivityAyuda.this, OpcionesActivity.class);
        startActivity(in);
        setResult(RESULT_OK);
        finish();
    }
}
