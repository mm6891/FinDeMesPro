package globalsolutions.findemes.pantallas.activity;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import globalsolutions.findemes.R;
import globalsolutions.findemes.database.dao.CuentaDAO;
import globalsolutions.findemes.database.dao.GastoDAO;
import globalsolutions.findemes.database.model.CuentaItem;
import globalsolutions.findemes.database.model.Gasto;
import globalsolutions.findemes.database.model.GrupoGasto;
import globalsolutions.findemes.pantallas.fragment.DatePickerFragment;
import globalsolutions.findemes.pantallas.util.Util;

public class CuentasActivity extends FragmentActivity {

    private TextView tvNombreCuenta;
    private TextView tvNumeroCuenta;
    private TextView tvFechaCuenta;
    private Spinner cuentaSp;

    //this counts how many Spinner's are on the UI
    private int mSpinnerCount=0;
    //this counts how many Spinner's have been initialized
    private int mSpinnerInitializedCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);

        //boton retroceder
        ImageButton btnReturn = (ImageButton) findViewById(R.id.btnBackButton);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                backActivity();
            }
        });


        tvNombreCuenta = (TextView) findViewById(R.id.tvNombreCuenta);
        tvNumeroCuenta = (TextView) findViewById(R.id.tvNumeroCuenta);
        tvFechaCuenta = (TextView) findViewById(R.id.tvFechaCuenta);

        //cargamos el combo de cuentas
        cuentaSp = (Spinner) findViewById(R.id.spCuentas);

        CuentaDAO cuentaDAO = new CuentaDAO(getApplicationContext());
        final ArrayList<CuentaItem> list = cuentaDAO.selectCuentasItems();

        String[] nombres = new String[list.size() + 1];
        nombres [0] = "";
        for(int i = 0; i < list.size(); i++) {
            int pos = i + 1;
            nombres[pos] = list.get(i).getNombre();
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, nombres);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cuentaSp.setAdapter(dataAdapter);

        mSpinnerCount++;
        cuentaSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSpinnerInitializedCount < mSpinnerCount)
                {
                    mSpinnerInitializedCount++;
                }
                else {
                    String cuentaSelected = cuentaSp.getSelectedItem() != null ? (String) cuentaSp.getSelectedItem() : "";
                    for(int i = 0; i < list.size(); i++) {
                       if(cuentaSelected.equals(list.get(i).getNombre())){
                           tvNombreCuenta.setText(list.get(i).getNombre());
                           tvNumeroCuenta.setText(list.get(i).getNumero());
                           tvFechaCuenta.setText(list.get(i).getFecha());
                       }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    public void onBackPressed() {
       backActivity();
    }

    private void backActivity(){
        Intent in = new Intent(CuentasActivity.this, MainActivity.class);
        startActivity(in);
        setResult(RESULT_OK);
        finish();
    }
}
