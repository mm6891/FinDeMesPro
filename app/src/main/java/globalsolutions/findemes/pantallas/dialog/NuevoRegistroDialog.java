package globalsolutions.findemes.pantallas.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import globalsolutions.findemes.R;
import globalsolutions.findemes.database.dao.GrupoGastoDAO;
import globalsolutions.findemes.database.dao.GrupoIngresoDAO;
import globalsolutions.findemes.database.dao.MovimientoDAO;
import globalsolutions.findemes.database.dao.RegistroDAO;
import globalsolutions.findemes.database.model.Registro;
import globalsolutions.findemes.database.util.Constantes;
import globalsolutions.findemes.pantallas.fragment.DatePickerFragment;
import globalsolutions.findemes.pantallas.util.MoneyValueFilter;
import globalsolutions.findemes.pantallas.util.Util;

/**
 * Created by manuel.molero on 16/02/2015.
 */
public class NuevoRegistroDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private ONuevoRegistroDialogListener callback;

    public interface ONuevoRegistroDialogListener {
        public void ONuevoRegistroDialogSubmit(String result);
    }

    private Spinner categoria;
    private Spinner tipoMovimiento;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            callback = (ONuevoRegistroDialogListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement ONuevoRegistroDialogListener");
        }

        final View view = inflater.inflate(R.layout.nuevo_registro_dialog, container, false);

        //establecemos listener de limitador de digitos
        ((EditText) view.findViewById(R.id.txtValor)).setKeyListener(new MoneyValueFilter());

        //cargamos el combo de periodicidad
        Spinner periodicidad = (Spinner) view.findViewById(R.id.spPeriodicidad);
        List<String> listPeriod = new ArrayList<String>();
        listPeriod.add(getResources().getString(R.string.PERIODICIDAD_REGISTRO_DIARIO));
        listPeriod.add(getResources().getString(R.string.PERIODICIDAD_REGISTRO_SEMANAL));
        listPeriod.add(getResources().getString(R.string.PERIODICIDAD_REGISTRO_QUINCENAL));
        listPeriod.add(getResources().getString(R.string.PERIODICIDAD_REGISTRO_MENSUAL));
        listPeriod.add(getResources().getString(R.string.PERIODICIDAD_REGISTRO_TRIMESTRAL));
        listPeriod.add(getResources().getString(R.string.PERIODICIDAD_REGISTRO_ANUAL));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item, listPeriod);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodicidad.setAdapter(dataAdapter);

        //cargamos el combo tipo (gasto o ingreso)
        tipoMovimiento = (Spinner) view.findViewById(R.id.spTipo);
        List<String> listTipos = new ArrayList<String>();
        listTipos.add(getResources().getString(R.string.TIPO_MOVIMIENTO_GASTO));
        listTipos.add(getResources().getString(R.string.TIPO_MOVIMIENTO_INGRESO));
        ArrayAdapter<String> dataAdapterTM = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item, listTipos);
        dataAdapterTM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoMovimiento.setAdapter(dataAdapterTM);
        tipoMovimiento.setOnItemSelectedListener(new tipoMovimientoOnClickListener());

        //cargamos el combo de categorias
        categoria = (Spinner) view.findViewById(R.id.spCategoria);
        categoria.setEnabled(false);

        //cargamos el modal para seleccionar fecha
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdfDia = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfHora = new SimpleDateFormat("kk:mm");
        String mTimeText = sdfDia.format(date);
        String mTimeHora = sdfHora.format(date);

        ((TextView) view.findViewById(R.id.tvDiaNR)).setText(mTimeText);
        ((TextView) view.findViewById(R.id.tvHoraNR)).setText(mTimeHora);

        ImageButton datePicker = (ImageButton) view.findViewById(R.id.myDatePickerButtonNR);

        datePicker.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        ImageButton btnNuevoRegistro = (ImageButton) view.findViewById(R.id.btnCrearRegistro);

        btnNuevoRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nombre , valor
                String valor = (String)((EditText) view.findViewById(R.id.txtValor)).getText().toString();
                if(valor == null || valor.isEmpty()) {
                    ((EditText) view.findViewById(R.id.txtValor)).setError(getResources().getString(R.string.Validacion_Cantidad));
                    return;
                }
                String descripcion = (String)((EditText) view.findViewById(R.id.txtRegistro)).getText().toString();
                if(descripcion == null || descripcion.isEmpty()) {
                    ((EditText) view.findViewById(R.id.txtRegistro)).setError(getResources().getString(R.string.Validacion_Nombre));
                    return;
                }

                //periodicidad
                String periodicidad = (String)((Spinner) view.findViewById(R.id.spPeriodicidad)).getSelectedItem();
                String tipoMovimiento = (String)((Spinner) view.findViewById(R.id.spTipo)).getSelectedItem();
                String categoria = (String)((Spinner) view.findViewById(R.id.spCategoria)).getSelectedItem();

                if(categoria != null && !categoria.isEmpty()) {
                    Registro nuevoRegistro = new Registro();
                    nuevoRegistro.setDescripcion(descripcion);
                    nuevoRegistro.setPeriodicidad(periodicidad);
                    nuevoRegistro.setTipo(tipoMovimiento);
                    nuevoRegistro.setGrupo(categoria);
                    Integer valueActivo = ((RadioButton)view.findViewById(R.id.rbActivo)).isChecked()
                            ? Integer.valueOf(Constantes.REGISTRO_ACTIVO.toString()) :
                            Integer.valueOf(Constantes.REGISTRO_INACTIVO.toString());
                    nuevoRegistro.setActivo(valueActivo);
                    nuevoRegistro.setValor(valor);
                    String fecha = (String) ((TextView) view.findViewById(R.id.tvDiaNR)).getText();
                    String hora = (String) ((TextView) view.findViewById(R.id.tvHoraNR)).getText();
                    nuevoRegistro.setFecha(fecha + " " + hora);

                    RegistroDAO registroDAO = new RegistroDAO(view.getContext());
                    long idNuevoRegistro = registroDAO.createRecords(nuevoRegistro);
                    boolean actualizado = idNuevoRegistro > 0;
                    if(actualizado){
                        nuevoRegistro.set_id(Long.valueOf(idNuevoRegistro).intValue());
                        Util.showToast(view.getContext(), getResources().getString(R.string.Creado));
                        //actualiza movimientos segun registro frecuente creado
                        new MovimientoDAO().creaMovimientos(nuevoRegistro,view.getContext());
                        callback.ONuevoRegistroDialogSubmit(String.valueOf(Activity.RESULT_OK));
                        dismiss();
                    }
                    else
                        Util.showToast(view.getContext(), getResources().getString(R.string.No_Creado));
                }
                else{
                    Util.showToast(view.getContext(), getResources().getString(R.string.Selecciona_categoria));
                    return;
                }
            }
        });

        // Inflate the layout to use as dialog or embedded fragment
        return view;
    }

    //dialogos
    public void showDatePickerDialog(View v) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(getActivity(), this, year, month, day).show();
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
        ((TextView)this.getDialog().findViewById(R.id.tvDiaNR)).setText(mTimeText);
        ((TextView)this.getDialog().findViewById(R.id.tvHoraNR)).setText(mTimeHora);
    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    private class tipoMovimientoOnClickListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int pos,
                                   long id) {

            parent.getItemAtPosition(pos);
            categoria.setEnabled(true);
            List<String> list = new ArrayList<String>();

            if(((String)(tipoMovimiento.getSelectedItem())).equals(getString(R.string.TIPO_MOVIMIENTO_GASTO))) {
                GrupoGastoDAO grupoGastoDAO = new GrupoGastoDAO(getView().getContext());
                String[] categoriasGastos = grupoGastoDAO.selectGrupos();
                list = Arrays.asList(categoriasGastos);
            }
            else {
                GrupoIngresoDAO grupoIngresoDAO = new GrupoIngresoDAO(getView().getContext());
                String[] categoriasIngresos = grupoIngresoDAO.selectGrupos();
                list = Arrays.asList(categoriasIngresos);
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getView().getContext(),
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categoria.setAdapter(dataAdapter);
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    }
}
