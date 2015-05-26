package globalsolutions.findemes.pantallas.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import globalsolutions.findemes.R;
import globalsolutions.findemes.database.dao.PasswordDAO;
import globalsolutions.findemes.database.util.Constantes;
import globalsolutions.findemes.pantallas.util.Util;

/**
 * Created by Manuel on 11/04/2015.
 */
public class Password extends Activity implements View.OnClickListener {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("EXIT", false))
        {
            finish();
            return;
        }
        PasswordDAO passwordDAO = new PasswordDAO(getApplicationContext());
        globalsolutions.findemes.database.model.Password password = passwordDAO.selectPassword();
        String pass = password.getPassword();
        if (pass != null && !pass.isEmpty() &&
                password.getActivo().equals(Constantes.REGISTRO_ACTIVO.toString())) {
            setContentView(R.layout.password_dialog);
            ((EditText) findViewById(R.id.txtContrasenaMain)).clearComposingText();
            Button submitButton = (Button) findViewById(R.id.btnContrasenaMain);
            submitButton.setOnClickListener(this);
        }
        else{
            StartMain();
        }
    }

    public void onClick(View v) {

        PasswordDAO passwordDAO = new PasswordDAO(getApplicationContext());
        globalsolutions.findemes.database.model.Password password = passwordDAO.selectPassword();
        String pass = password.getPassword();
        if (pass != null && !pass.isEmpty() &&
                password.getActivo().equals(Constantes.REGISTRO_ACTIVO.toString())) {
            EditText passwordEditText = (EditText) findViewById(R.id.txtContrasenaMain);
            String passwordEdit = passwordEditText.getText().toString();
            if (passwordEdit.equals(pass)) {
                StartMain();
            }
            else{
                Util.showToast(getApplicationContext(), getResources().getString(R.string.Validacion_Contrasenya));
                finish();
            }
        }
        else{
            StartMain();
        }
    }

    public void StartMain()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
