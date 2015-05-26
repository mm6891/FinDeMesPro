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
import globalsolutions.findemes.database.dao.GrupoIngresoDAO;
import globalsolutions.findemes.database.model.GrupoIngreso;
import globalsolutions.findemes.pantallas.util.Util;

/**
 * Created by manuel.molero on 16/02/2015.
 */
public class NuevaCategoriaIngresoDialog extends DialogFragment {

    private ImageButton btnNewCatIng;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.nueva_categoria_ingreso_dialog, container, false);

        btnNewCatIng = (ImageButton) view.findViewById(R.id.btnNewCatIng);

        btnNewCatIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descripcion = (String)((EditText) view.findViewById(R.id.txtNuevaCatIng)).getText().toString();
                if(descripcion == null || descripcion.isEmpty()) {
                    ((EditText) view.findViewById(R.id.txtNuevaCatIng)).setError(getResources().getString(R.string.Validacion_Nombre));
                    return;
                }

                GrupoIngreso nuevoGrupo = new GrupoIngreso();
                nuevoGrupo.setGrupo(descripcion);

                GrupoIngresoDAO grupoDAO = new GrupoIngresoDAO(v.getContext());
                boolean actualizado = grupoDAO.createRecords(nuevoGrupo) > 0;

                if(actualizado){
                    Util.showToast(view.getContext(), getResources().getString(R.string.Creado));
                    ((CategoriasIngresosDialog)getTargetFragment()).OnCategoriaIngresoDialogSubmit(String.valueOf(Activity.RESULT_OK));
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
