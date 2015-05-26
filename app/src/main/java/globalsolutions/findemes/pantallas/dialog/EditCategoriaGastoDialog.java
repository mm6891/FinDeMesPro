package globalsolutions.findemes.pantallas.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import globalsolutions.findemes.R;
import globalsolutions.findemes.database.dao.GrupoGastoDAO;
import globalsolutions.findemes.database.model.GrupoGasto;
import globalsolutions.findemes.pantallas.util.Util;

/**
 * Created by manuel.molero on 16/02/2015.
 */
public class EditCategoriaGastoDialog extends DialogFragment {

    private EditText txtDecripcionCatGas;
    private ImageButton btnModCatGas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.edit_category_gasto_dialog, container, false);

        txtDecripcionCatGas = (EditText) view.findViewById(R.id.txtDecripcionCatGas);
        txtDecripcionCatGas.setText(getArguments().getString("nameCategory"));

        final GrupoGasto aMod = new GrupoGasto();
        aMod.setGrupo(getArguments().getString("nameCategory"));

        btnModCatGas = (ImageButton) view.findViewById(R.id.btnModCatGas);

        btnModCatGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descripcion = (String)((EditText) view.findViewById(R.id.txtDecripcionCatGas)).getText().toString();
                if(descripcion == null || descripcion.isEmpty()) {
                    ((EditText) view.findViewById(R.id.txtDecripcionCatGas)).setError(getResources().getString(R.string.Validacion_Nombre));
                    return;
                }

                GrupoGasto nuevoGrupo = new GrupoGasto();
                nuevoGrupo.setGrupo(descripcion);

                GrupoGastoDAO grupoDAO = new GrupoGastoDAO(v.getContext());
                boolean actualizado = grupoDAO.updateGrupoGasto(aMod,nuevoGrupo);
                if(actualizado){
                    Util.showToast(view.getContext(), getResources().getString(R.string.Actualizado));
                    ((CategoriasGastosDialog)getTargetFragment()).OnCategoriaGastoDialogSubmit(String.valueOf(Activity.RESULT_OK));
                    dismiss();
                }
                else
                    Util.showToast(view.getContext(), getResources().getString(R.string.No_Actualizado));
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
