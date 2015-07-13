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

        final View view = inflater.inflate(R.layout.nueva_cuenta_dialog, container, false);

        ImageButton btnGuardarCuenta = (ImageButton) view.findViewById(R.id.btnGuardarCuenta);

        btnGuardarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nombre , numero
                String nombre = (String)((EditText) view.findViewById(R.id.etNombreCuentaDialog)).getText().toString();
                if(nombre == null || nombre.isEmpty()) {
                    ((EditText) view.findViewById(R.id.etNombreCuentaDialog)).setError(getResources().getString(R.string.Validacion_Nombre));
                    return;
                }
                String numero = (String)((EditText) view.findViewById(R.id.etNumeroCuentaDialog)).getText().toString();
                if(numero == null || numero.isEmpty()) {
                    ((EditText) view.findViewById(R.id.etNumeroCuentaDialog)).setError(getResources().getString(R.string.Validacion_Numero));
                    return;
                }


                Cuenta nuevaCuenta = new Cuenta();
                nuevaCuenta.setNombre(nombre);
                nuevaCuenta.setNumero(numero);

                CuentaDAO cuentaDAO = new CuentaDAO(view.getContext());
                long idNuevaCuenta = cuentaDAO.createRecords(nuevaCuenta);
                boolean actualizado = idNuevaCuenta > 0;
                if(actualizado) {
                    nuevaCuenta.set_id(Long.valueOf(idNuevaCuenta).intValue());
                    Util.showToast(view.getContext(), getResources().getString(R.string.Creado));
                    callback.ONuevaCuentaDialogSubmit(String.valueOf(Activity.RESULT_OK));
                    dismiss();
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
