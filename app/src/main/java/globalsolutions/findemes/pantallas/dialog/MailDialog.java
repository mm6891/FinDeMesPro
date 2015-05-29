package globalsolutions.findemes.pantallas.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import globalsolutions.findemes.R;
import globalsolutions.findemes.pantallas.util.GMailSender;
import globalsolutions.findemes.pantallas.util.Util;

/**
 * Created by manuel.molero on 16/02/2015.
 */
public class MailDialog extends DialogFragment {

    private EditText txtMailTo;
    private ImageButton guardar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.mail_dialog, container, false);
        txtMailTo = (EditText) view.findViewById(R.id.txtMailExpTo);
        guardar = (ImageButton) view.findViewById(R.id.btnEnviarCorreoExp);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mailTo = txtMailTo.getText().toString();
                if(mailTo == null || mailTo.isEmpty() || !mailTo.contains("@")) {
                    ((EditText) view.findViewById(R.id.txtMailExpTo)).setError(getResources().getString(R.string.Validacion_Correo_destino));
                    return;
                }
                try {
                    String pdfName = getArguments().getString("pdfname");
                    GMailSender sender = new GMailSender("findemesapp@gmail.com", "esta50es");
                    sender.sendMailWithAttachment(getResources().getString(R.string.AsuntoExportar),
                            getResources().getString(R.string.CuerpoExportar),
                            "findemesapp@gmail.com",
                            mailTo, view.getContext(), pdfName);
                    Util.showToast(view.getContext(), getResources().getString(R.string.Validacion_Correo_ok));
                    dismiss();
                } catch (Exception e) {
                    Util.showToast(view.getContext(), getResources().getString(R.string.Validacion_Correo_envio));
                    return;
                }
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
