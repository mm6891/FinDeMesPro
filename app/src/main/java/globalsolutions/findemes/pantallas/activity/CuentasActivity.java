package globalsolutions.findemes.pantallas.activity;

import android.app.Activity;
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
import android.widget.ListView;
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
import globalsolutions.findemes.pantallas.adapter.CuentaAdapter;
import globalsolutions.findemes.pantallas.dialog.NuevaCuentaDialog;
import globalsolutions.findemes.pantallas.fragment.DatePickerFragment;
import globalsolutions.findemes.pantallas.util.Util;

public class CuentasActivity extends FragmentActivity implements NuevaCuentaDialog.ONuevaCuentaDialogListener
        /*EditCuentaDialog.OnEditCuentaDialogListener*/{

    private TextView tvNombreCuenta;
    private TextView tvNumeroCuenta;
    private TextView tvFechaCuenta;
    private ImageButton btnNuevaCuenta;
    private ListView listViewCuentas;

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

        CuentaDAO cuentaDAO = new CuentaDAO(getApplicationContext());
        ArrayList<CuentaItem> list = cuentaDAO.selectCuentasItems();

        listViewCuentas = (ListView) findViewById(R.id.listViewCuentas);
        listViewCuentas.setAdapter(new CuentaAdapter(getApplicationContext(), list));

        btnNuevaCuenta = (ImageButton) findViewById(R.id.btnNuevaCuenta);
        btnNuevaCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    //muestra el modal de nuevo registro frecuente
    public void showNuevaCuentaDialog() {
        DialogFragment newFragment = new NuevaCuentaDialog();
        newFragment.show(getFragmentManager(),"NUEVA CUENTA");
    }

    //muestra el modal de edicion de registro frecuente
    public void showEditCuentaDialog(Bundle bundle) {
        /*DialogFragment newFragment = new EditCuentaDialog();
        newFragment.setArguments(bundle);
        newFragment.show(getFragmentManager(),"EDITAR CUENTA");*/
    }

    @Override
    public void ONuevaCuentaDialogSubmit(String result) {
        /*if(result.equals(String.valueOf(Activity.RESULT_OK)))
            ((RegistroAdapter)listViewReg.getAdapter()).updateReceiptsList(new RegistroDAO(getApplicationContext()).selectRegistrosItems());*/
    }

    //@Override
    public void OnEditCuentaDialogSubmit(String result) {
       /* if(result.equals(String.valueOf(Activity.RESULT_OK)))
            ((RegistroAdapter)listViewReg.getAdapter()).updateReceiptsList(new RegistroDAO(getApplicationContext()).selectRegistrosItems());*/
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
