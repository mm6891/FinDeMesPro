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
import globalsolutions.findemes.database.dao.CuentaDAO;
import globalsolutions.findemes.database.dao.GrupoGastoDAO;
import globalsolutions.findemes.database.dao.GrupoIngresoDAO;
import globalsolutions.findemes.database.dao.MovimientoDAO;
import globalsolutions.findemes.database.dao.RegistroDAO;
import globalsolutions.findemes.database.model.Cuenta;
import globalsolutions.findemes.database.model.Registro;
import globalsolutions.findemes.database.util.Constantes;
import globalsolutions.findemes.pantallas.util.MoneyValueFilter;
import globalsolutions.findemes.pantallas.util.Util;

/**
 * Created by manuel.molero on 16/02/2015.
 */
public class EditCuentaDialog extends DialogFragment {

    private OnEditCuentaDialogListener callback;

    public interface OnEditCuentaDialogListener {
        public void OnEditCuentaDialogSubmit(String result);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            callback = (OnEditCuentaDialogListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnEditCuentaDialogListener");
        }

        final View view = inflater.inflate(R.layout.edit_cuenta_dialog, container, false);

        //se cargan las propiedades del item seleccionado
        String _id = getArguments().getString("_id");
        String nombre = getArguments().getString("nombre");
        String numero = getArguments().getString("numero");
        String fecha = getArguments().getString("fecha");

        final Cuenta regProperties = new Cuenta();
        regProperties.setNombre(nombre);
        regProperties.setNumero(numero);
        regProperties.setFecha(fecha);

        ((EditText) view.findViewById(R.id.etNombreCuentaDialogEdit)).setText(nombre);
        ((EditText) view.findViewById(R.id.etNumeroCuentaDialogEdit)).setText(numero);

        final Cuenta aMod = new Cuenta();
        aMod.set_id(Integer.valueOf(_id).intValue());

        ImageButton btnModificarReg = (ImageButton) view.findViewById(R.id.btnEditarCuenta);

        btnModificarReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nombre , numero
                String nombre = (String) ((EditText) view.findViewById(R.id.etNombreCuentaDialogEdit)).getText().toString();
                if (nombre == null || nombre.isEmpty()) {
                    ((EditText) view.findViewById(R.id.etNombreCuentaDialogEdit)).setError(getResources().getString(R.string.Validacion_Nombre));
                    return;
                }
                String numero = (String) ((EditText) view.findViewById(R.id.etNumeroCuentaDialogEdit)).getText().toString();
                if (numero == null || numero.isEmpty()) {
                    ((EditText) view.findViewById(R.id.etNumeroCuentaDialogEdit)).setError(getResources().getString(R.string.Validacion_Numero));
                    return;
                }

                Cuenta nuevaCuenta = new Cuenta();
                nuevaCuenta.setNombre(nombre);
                nuevaCuenta.setNumero(numero);

                CuentaDAO cuentaDAO = new CuentaDAO(view.getContext());
                boolean actualizado = cuentaDAO.updateCuenta(aMod, nuevaCuenta);
                if (actualizado) {
                    Util.showToast(view.getContext(), getResources().getString(R.string.Modificado));
                    callback.OnEditCuentaDialogSubmit(String.valueOf(Activity.RESULT_OK));
                    dismiss();
                } else
                    Util.showToast(view.getContext(), getResources().getString(R.string.No_Modificado));
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
