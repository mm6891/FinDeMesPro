package globalsolutions.findemes.pantallas.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;

import globalsolutions.findemes.R;
import globalsolutions.findemes.database.dao.RegistroDAO;
import globalsolutions.findemes.database.model.RegistroItem;
import globalsolutions.findemes.database.util.ArrayAdapterWithIcon;
import globalsolutions.findemes.pantallas.adapter.RegistroAdapter;
import globalsolutions.findemes.pantallas.dialog.EditRegistroDialog;
import globalsolutions.findemes.pantallas.dialog.NuevoRegistroDialog;
import globalsolutions.findemes.pantallas.util.Util;

public class RegistrosActivity extends FragmentActivity implements NuevoRegistroDialog.ONuevoRegistroDialogListener,
        EditRegistroDialog.OnEditRegistroDialogListener {


    private ListView listViewReg;
    private ImageButton btnNuevoRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);

        //boton retroceder
        ImageButton btnReturn = (ImageButton) findViewById(R.id.btnBackButton);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                backActivity();
            }
        });

        //boton nuevo registro
        btnNuevoRegistro = (ImageButton) findViewById(R.id.btnNuevoRegistro);
        btnNuevoRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNuevoRegistroDialog();
            }
        });

        //recuperamos registros
        ArrayList<RegistroItem> regs = new ArrayList(new RegistroDAO(getApplicationContext()).selectRegistrosItems());
        if(regs.size() <= 0 )
            Util.showToast(getApplicationContext(), getResources().getString(R.string.No_Registros));

        listViewReg = (ListView) findViewById(R.id.listViewReg);
        listViewReg.setAdapter(new RegistroAdapter(getApplicationContext(), regs));

        listViewReg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position,
                                    long id) {
                final RegistroItem registroItem = (RegistroItem) listViewReg.getItemAtPosition(position);
                final String[] items = {getResources().getString(R.string.Modificar), getResources().getString(R.string.Eliminar)};

                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrosActivity.this);
                ListAdapter adapter = new ArrayAdapterWithIcon(view.getContext(), items, Util.prgmImagesOption);
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                //Eliminar Registro
                                String accion = (String) items[item];
                                if (accion.equals(getResources().getString(R.string.Eliminar))) {
                                    new AlertDialog.Builder(RegistrosActivity.this)
                                            //set message, title, and icon
                                            .setTitle(getApplicationContext().getResources().getString(R.string.Eliminar))
                                            .setMessage(getApplicationContext().getResources().getString(R.string.Confirmar))
                                            .setIcon(R.drawable.delete)
                                            .setPositiveButton(getApplicationContext().getResources().getString(R.string.Eliminar), new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    //your deleting code
                                                    RegistroDAO registroDAO = new RegistroDAO(RegistrosActivity.this);
                                                    boolean realizado = registroDAO.deleteRegistro(registroItem.get_id());
                                                    if (realizado) {
                                                        Util.showToast(getApplicationContext(), getResources().getString(R.string.Eliminado));
                                                        ((RegistroAdapter) listViewReg.getAdapter()).updateReceiptsList(new RegistroDAO(getApplicationContext()).selectRegistrosItems());
                                                    } else
                                                        Util.showToast(getApplicationContext(), getResources().getString(R.string.No_Eliminado));
                                                    dialog.dismiss();
                                                }

                                            })
                                            .setNegativeButton(getApplicationContext().getResources().getString(R.string.Cancelar), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .create().show();
                                }
                                if (accion.equals(getResources().getString(R.string.Modificar))) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("_id", String.valueOf(registroItem.get_id()));
                                    bundle.putString("nombre", registroItem.getDescripcion());
                                    bundle.putString("periodicidad", registroItem.getPeriodicidad());
                                    bundle.putString("valor", registroItem.getValor());
                                    bundle.putString("fecha", registroItem.getFecha());
                                    bundle.putString("categoria", registroItem.getGrupo());
                                    bundle.putString("tipo", registroItem.getTipo());
                                    bundle.putString("activo", String.valueOf(registroItem.getActivo()));

                                    // Create an instance of the dialog fragment and show it*//*
                                    showEditRegistroDialog(bundle);
                                }
                            }
                        }
                ).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        backActivity();
    }

    private void backActivity(){
        Intent in = new Intent(RegistrosActivity.this, MainActivity.class);
        startActivity(in);
        setResult(RESULT_OK);
        finish();
    }

    //muestra el modal de nuevo registro frecuente
    public void showNuevoRegistroDialog() {
        DialogFragment newFragment = new NuevoRegistroDialog();
        newFragment.show(getFragmentManager(),"NUEVO REGISTRO");
    }

    //muestra el modal de edicion de registro frecuente
    public void showEditRegistroDialog(Bundle bundle) {
        DialogFragment newFragment = new EditRegistroDialog();
        newFragment.setArguments(bundle);
        newFragment.show(getFragmentManager(),"EDITAR REGISTRO");
    }

    @Override
    public void ONuevoRegistroDialogSubmit(String result) {
        if(result.equals(String.valueOf(Activity.RESULT_OK)))
            ((RegistroAdapter)listViewReg.getAdapter()).updateReceiptsList(new RegistroDAO(getApplicationContext()).selectRegistrosItems());
    }

    @Override
    public void OnEditRegistroDialogSubmit(String result) {
        if(result.equals(String.valueOf(Activity.RESULT_OK)))
            ((RegistroAdapter)listViewReg.getAdapter()).updateReceiptsList(new RegistroDAO(getApplicationContext()).selectRegistrosItems());
    }
}
