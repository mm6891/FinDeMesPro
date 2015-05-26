package globalsolutions.findemes.pantallas.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import globalsolutions.findemes.R;
import globalsolutions.findemes.database.model.InformeItem;
import globalsolutions.findemes.database.model.MovimientoItem;
import globalsolutions.findemes.pantallas.adapter.MovimientoAdapter;

/**
 * Created by manuel.molero on 16/02/2015.
 */
public class InformeDialog extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.informe_dialog, container, false);

        //se cargan las propiedades del item seleccionado
        InformeItem informe = getArguments().getParcelable("informe");

       ((TextView) view.findViewById(R.id.tvDetalleInformePeriodo)).setText(informe.getPeriodoDesc());
        ListView listViewMovs = (ListView) view.findViewById(R.id.listViewMovInforme);
        listViewMovs.setAdapter(new MovimientoAdapter(view.getContext(), informe.getMovimientos()));

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
