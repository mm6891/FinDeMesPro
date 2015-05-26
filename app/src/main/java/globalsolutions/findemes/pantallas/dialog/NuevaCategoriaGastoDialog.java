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
public class NuevaCategoriaGastoDialog extends DialogFragment {

    private ImageButton btnNewCatGas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.nueva_categoria_gasto_dialog, container, false);

        btnNewCatGas = (ImageButton) view.findViewById(R.id.btnNewCatGas);

        btnNewCatGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descripcion = (String)((EditText)view.findViewById(R.id.txtNuevaCatGas)).getText().toString();
                if(descripcion == null || descripcion.isEmpty()) {
                    ((EditText) view.findViewById(R.id.txtNuevaCatGas)).setError(getResources().getString(R.string.Validacion_Nombre));
                    return;
                }

                GrupoGasto nuevoGrupo = new GrupoGasto();
                nuevoGrupo.setGrupo(descripcion);

                GrupoGastoDAO grupoDAO = new GrupoGastoDAO(v.getContext());
                boolean actualizado = grupoDAO.createRecords(nuevoGrupo) > 0;

                if(actualizado){
                    Util.showToast(view.getContext(), getResources().getString(R.string.Creado));
                    ((CategoriasGastosDialog)getTargetFragment()).OnCategoriaGastoDialogSubmit(String.valueOf(Activity.RESULT_OK));
                    dismiss();
                }
                else
                    Util.showToast(view.getContext(), getResources().getString(R.string.No_Creado));
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