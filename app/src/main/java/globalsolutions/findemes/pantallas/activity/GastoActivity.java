package globalsolutions.findemes.pantallas.activity;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import globalsolutions.findemes.R;
import globalsolutions.findemes.database.dao.GastoDAO;
import globalsolutions.findemes.database.dao.GrupoGastoDAO;
import globalsolutions.findemes.database.model.Gasto;
import globalsolutions.findemes.database.model.GrupoGasto;
import globalsolutions.findemes.pantallas.fragment.DatePickerFragment;
import globalsolutions.findemes.pantallas.util.MoneyValueFilter;
import globalsolutions.findemes.pantallas.util.Util;

public class GastoActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto);

        //boton retroceder
        ImageButton btnReturn = (ImageButton) findViewById(R.id.btnBackButton);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                backActivity();
            }
        });

        //establecemos listener de limitador de digitos
        ((EditText) findViewById(R.id.txtGasto)).setKeyListener(new MoneyValueFilter());

        //cargamos el combo de categorias
        Spinner categoria = (Spinner) findViewById(R.id.spCategoriaGasto);

        List<String> list = new ArrayList<String>();
        GrupoGastoDAO grupoGastoDAO = new GrupoGastoDAO(getApplicationContext());
        String[] categoriasGastos = grupoGastoDAO.selectGrupos();
        list = Arrays.asList(categoriasGastos);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoria.setAdapter(dataAdapter);

        //cargamos el modal para seleccionar fecha
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdfDia = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfHora = new SimpleDateFormat("kk:mm");
        String mTimeText = sdfDia.format(date);
        String mTimeHora = sdfHora.format(date);

        ((TextView) findViewById(R.id.tvDiaAG)).setText(mTimeText);
        ((TextView) findViewById(R.id.tvHoraAG)).setText(mTimeHora);

        ImageButton datePicker = (ImageButton) findViewById(R.id.myDatePickerButtonAG);

        datePicker.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
    }

    //dialogos
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("movimiento", getResources().getString(R.string.TIPO_MOVIMIENTO_GASTO));
        newFragment.setArguments(bundle);
        newFragment.show(getFragmentManager(),"Fecha");
    }

    //eventos botones guardar gasto e ingreso
    public void guardarGasto(View view) {
        //descripcion , valor , fecha
        String valor = (String)((EditText) findViewById(R.id.txtGasto)).getText().toString();
        if(valor == null || valor.isEmpty()) {
            ((EditText) findViewById(R.id.txtGasto)).setError(getResources().getString(R.string.Validacion_Cantidad));
            return;
        }
        String descripcion = (String)((EditText) findViewById(R.id.txtDecripcion)).getText().toString();
        if(descripcion == null || descripcion.isEmpty()) {
            ((EditText) findViewById(R.id.txtDecripcion)).setError(getResources().getString(R.string.Validacion_Descripcion));
            return;
        }
        //obtenemos categoria de gasto
        String categoriaGasto = (String)((Spinner) findViewById(R.id.spCategoriaGasto)).getSelectedItem();
        if(categoriaGasto != null && !categoriaGasto.isEmpty()) {
            Gasto nuevoGasto = new Gasto();
            nuevoGasto.setDescripcion(descripcion);
            nuevoGasto.setValor(valor);
            String fecha = (String) ((TextView) findViewById(R.id.tvDiaAG)).getText();
            String hora = (String) ((TextView) findViewById(R.id.tvHoraAG)).getText();
            nuevoGasto.setFecha(fecha + " " + hora);

            GrupoGasto grupo = new GrupoGasto();
            grupo.setGrupo(categoriaGasto);
            nuevoGasto.setGrupoGasto(grupo);
            GastoDAO gastoDAO = new GastoDAO(getApplicationContext());
            gastoDAO.createRecords(nuevoGasto);
            Util.showToast(view.getContext(),getResources().getString(R.string.Creado));
            clear();
        }
        else{
            Util.showToast(view.getContext(),getResources().getString(R.string.Selecciona_categoria));
        }
    }

    private void clear() {
        ((EditText) findViewById(R.id.txtGasto)).setText("");
        ((EditText) findViewById(R.id.txtDecripcion)).setText("");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        final Calendar c = Calendar.getInstance();
        c.set(year,month,day);

        Date date = new Date(c.getTimeInMillis());
        SimpleDateFormat sdfDia = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfHora = new SimpleDateFormat("kk:mm");
        String mTimeText = sdfDia.format(date);
        String mTimeHora = sdfHora.format(date);

        ((TextView) findViewById(R.id.tvDiaAG)).setText(mTimeText);
        ((TextView) findViewById(R.id.tvHoraAG)).setText(mTimeHora);
    }

    @Override
    public void onBackPressed() {
       backActivity();
    }

    private void backActivity(){
        Intent in = new Intent(GastoActivity.this, MainActivity.class);
        startActivity(in);
        setResult(RESULT_OK);
        finish();
    }
}
