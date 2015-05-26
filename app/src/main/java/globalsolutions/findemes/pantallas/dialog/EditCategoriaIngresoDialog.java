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
public class EditCategoriaIngresoDialog extends DialogFragment {

    private EditText txtDecripcionCatIng;
    private ImageButton btnModCatIng;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.edit_category_ingreso_dialog, container, false);

        txtDecripcionCatIng = (EditText) view.findViewById(R.id.txtDecripcionCatIng);
        txtDecripcionCatIng.setText(getArguments().getString("nameCategory"));

        final GrupoIngreso aMod = new GrupoIngreso();
        aMod.setGrupo(getArguments().getString("nameCategory"));

        btnModCatIng = (ImageButton) view.findViewById(R.id.btnModCatIng);

        btnModCatIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descripcion = (String)((EditText) view.findViewById(R.id.txtDecripcionCatIng)).getText().toString();
                if(descripcion == null || descripcion.isEmpty()) {
                    ((EditText) view.findViewById(R.id.txtDecripcionCatIng)).setError(getResources().getString(R.string.Validacion_Nombre));
                    return;
                }

                GrupoIngreso nuevoGrupo = new GrupoIngreso();
                nuevoGrupo.setGrupo(descripcion);

                GrupoIngresoDAO grupoDAO = new GrupoIngresoDAO(v.getContext());
                boolean actualizado = grupoDAO.updateGrupoIngreso(aMod,nuevoGrupo);
                if(actualizado){
                    Util.showToast(view.getContext(), getResources().getString(R.string.Actualizado));
                    ((CategoriasIngresosDialog)getTargetFragment()).OnCategoriaIngresoDialogSubmit(String.valueOf(Activity.RESULT_OK));
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
