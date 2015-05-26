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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import globalsolutions.findemes.R;
import globalsolutions.findemes.database.dao.GrupoIngresoDAO;
import globalsolutions.findemes.database.dao.IngresoDAO;
import globalsolutions.findemes.database.model.GrupoIngreso;
import globalsolutions.findemes.database.model.Ingreso;
import globalsolutions.findemes.pantallas.fragment.DatePickerFragment;
import globalsolutions.findemes.pantallas.util.MoneyValueFilter;
import globalsolutions.findemes.pantallas.util.Util;

public class IngresoActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso);

        //boton retroceder
        ImageButton btnReturn = (ImageButton) findViewById(R.id.btnBackButton);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                backActivity();
            }
        });

        //establecemos listener de limitador de digitos
        ((EditText) findViewById(R.id.txtIngreso)).setKeyListener(new MoneyValueFilter());

        //cargamos el combo de categorias
        Spinner categoria = (Spinner) findViewById(R.id.spCategoriaIngreso);

        List<String> list = new ArrayList<String>();
        GrupoIngresoDAO grupoIngresoDAO = new GrupoIngresoDAO(getApplicationContext());
        String[] categoriasIngresos = grupoIngresoDAO.selectGrupos();
        list = Arrays.asList(categoriasIngresos);

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

        ((TextView) findViewById(R.id.tvDiaAI)).setText(mTimeText);
        ((TextView) findViewById(R.id.tvHoraAI)).setText(mTimeHora);

        ImageButton datePicker = (ImageButton) findViewById(R.id.myDatePickerButtonAI);

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
        bundle.putString("movimiento", getResources().getString(R.string.TIPO_MOVIMIENTO_INGRESO));
        newFragment.setArguments(bundle);
        newFragment.show(getFragmentManager(),"Fecha");
    }

    public void guardarIngreso(View view) {
        //descripcion , valor , fecha
        String valor = (String)((EditText) findViewById(R.id.txtIngreso)).getText().toString();
        if(valor == null || valor.isEmpty()) {
            ((EditText) findViewById(R.id.txtIngreso)).setError(getResources().getString(R.string.Validacion_Cantidad));
            return;
        }
        String descripcion = (String)((EditText) findViewById(R.id.txtDecripcion)).getText().toString();
        if(descripcion == null || descripcion.isEmpty()) {
            ((EditText) findViewById(R.id.txtDecripcion)).setError(getResources().getString(R.string.Validacion_Descripcion));
            return;
        }
        //obtenemos categoria de ingreso
        String categoriaIngreso = (String)((Spinner) findViewById(R.id.spCategoriaIngreso)).getSelectedItem();
        if(categoriaIngreso != null && !categoriaIngreso.isEmpty()) {
            Ingreso nuevoIngreso = new Ingreso();
            nuevoIngreso.setDescripcion(descripcion);
            nuevoIngreso.setValor(valor);
            String fecha = (String) ((TextView) findViewById(R.id.tvDiaAI)).getText();
            String hora = (String) ((TextView) findViewById(R.id.tvHoraAI)).getText();
            nuevoIngreso.setFecha(fecha + " " + hora);

            GrupoIngreso grupo = new GrupoIngreso();
            grupo.setGrupo(categoriaIngreso);
            nuevoIngreso.setGrupoIngreso(grupo);
            IngresoDAO ingresoDAO = new IngresoDAO(getApplicationContext());
            ingresoDAO.createRecords(nuevoIngreso);
            Util.showToast(view.getContext(), getResources().getString(R.string.Creado));
            clear();
        }
        else{
            Util.showToast(view.getContext(), getResources().getString(R.string.Selecciona_categoria));
            return;
        }
    }

    private void clear() {
        ((EditText) findViewById(R.id.txtIngreso)).setText("");
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

        ((TextView) findViewById(R.id.tvDiaAI)).setText(mTimeText);
        ((TextView) findViewById(R.id.tvHoraAI)).setText(mTimeHora);
    }

    @Override
    public void onBackPressed() {
        backActivity();
    }

    private void backActivity(){
        Intent in = new Intent(IngresoActivity.this, MainActivity.class);
        startActivity(in);
        setResult(RESULT_OK);
        finish();
    }
}
