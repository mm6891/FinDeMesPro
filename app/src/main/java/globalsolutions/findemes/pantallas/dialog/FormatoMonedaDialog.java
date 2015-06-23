package globalsolutions.findemes.pantallas.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import globalsolutions.findemes.R;
import globalsolutions.findemes.pantallas.util.Util;

/**
 * Created by manuel.molero on 16/02/2015.
 */
public class FormatoMonedaDialog extends DialogFragment {

    private Spinner spFormatoMoneda;
    private ImageButton btnFormatoMoneda;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.formato_moneda_dialog, container, false);

        spFormatoMoneda = (Spinner) view.findViewById(R.id.spFormatoMoneda);

        List<String> formatos = new ArrayList<String>();
        formatos.add("\u20ac");//euro
        formatos.add("\u00a3");//libra
        formatos.add("$");//dollar
		formatos.add("R$");//real brasileiro

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item, formatos);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFormatoMoneda.setAdapter(dataAdapter);

        btnFormatoMoneda = (ImageButton) view.findViewById(R.id.btnFormatoMoneda);

        btnFormatoMoneda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs =
                        view.getContext().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("moneda");
                editor.putString("moneda", (String)spFormatoMoneda.getSelectedItem());
                editor.commit();
                Util.showToast(view.getContext(),view.getResources().getString(R.string.Modificado));
                dismiss();
            }
        });

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
