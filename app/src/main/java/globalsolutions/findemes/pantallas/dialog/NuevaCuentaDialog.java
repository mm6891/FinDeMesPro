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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

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
import globalsolutions.findemes.pantallas.util.MoneyValueFilter;
import globalsolutions.findemes.pantallas.util.Util;

/**
 * Created by manuel.molero on 16/02/2015.
 */
public class NuevaCuentaDialog extends DialogFragment {

    private ONuevaCuentaDialogListener callback;

    public interface ONuevaCuentaDialogListener {
        public void ONuevaCuentaDialogSubmit(String result);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            callback = (ONuevaCuentaDialogListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement ONuevaCuentaDialogListener");
        }

        final View view = inflater.inflate(R.layout.nuevo_registro_dialog, container, false);

        //establecemos listener de limitador de digitos
        ((EditText) view.findViewById(R.id.txtValor)).setKeyListener(new MoneyValueFilter());

        ImageButton btnGuardarCuenta = (ImageButton) view.findViewById(R.id.btnCrearRegistro);

        btnGuardarCuenta.setOnClickListener(new View.OnClickListener() {
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
                        callback.ONuevaCuentaDialogSubmit(String.valueOf(Activity.RESULT_OK));
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

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

}
