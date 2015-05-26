package globalsolutions.findemes.pantallas.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import globalsolutions.findemes.R;
import globalsolutions.findemes.database.dao.GrupoGastoDAO;
import globalsolutions.findemes.database.util.ArrayAdapterWithIcon;
import globalsolutions.findemes.pantallas.adapter.CategoriaAdapter;
import globalsolutions.findemes.pantallas.util.Util;

/**
 * Created by manuel.molero on 16/02/2015.
 */
public class CategoriasGastosDialog extends DialogFragment {

    private ListView listViewCategoriasGastos;
    private ImageButton btnPlusCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.categoria_gasto_dialog, container, false);

        List<String> list = new ArrayList<String>();
        GrupoGastoDAO grupoGastoDAO = new GrupoGastoDAO(view.getContext());
        String[] categoriasGastos = grupoGastoDAO.selectGrupos();
        list = Arrays.asList(categoriasGastos);

        listViewCategoriasGastos = (ListView) view.findViewById(R.id.listViewCatGas);
        listViewCategoriasGastos.setAdapter(new CategoriaAdapter(view.getContext(),new ArrayList<String>(list)));

        listViewCategoriasGastos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position,
                                    long id) {
                final String categoriaGasto = (String) listViewCategoriasGastos.getItemAtPosition(position);
                final String[] items = {getResources().getString(R.string.Modificar), getResources().getString(R.string.Eliminar)};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //builder.setTitle(getResources().getString(R.string.MENU_OPCIONES));
                ListAdapter adapter = new ArrayAdapterWithIcon(view.getContext(), items, Util.prgmImagesOption);
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    //Eliminar Movimiento
                                    String accion = (String) items[item];
                                    if (accion.equals(getResources().getString(R.string.Eliminar))) {
                                        new AlertDialog.Builder(getActivity())
                                                //set message, title, and icon
                                                .setTitle(view.getContext().getResources().getString(R.string.Eliminar))
                                                .setMessage(view.getContext().getResources().getString(R.string.Confirmar))
                                                .setIcon(R.drawable.delete)
                                                .setPositiveButton(view.getContext().getResources().getString(R.string.Eliminar), new DialogInterface.OnClickListener() {

                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        //your deleting code
                                                        GrupoGastoDAO grupoGastoDAO = new GrupoGastoDAO(getActivity());
                                                        boolean realizado = grupoGastoDAO.deleteRecords(categoriaGasto);
                                                        if (realizado) {
                                                            Util.showToast(view.getContext(), getResources().getString(R.string.Eliminado));
                                                            String[] newList = grupoGastoDAO.selectGrupos();
                                                            ((CategoriaAdapter) listViewCategoriasGastos.getAdapter()).updateReceiptsList(
                                                                    new ArrayList<String>(Arrays.asList(newList)));
                                                        } else
                                                            Util.showToast(view.getContext(), getResources().getString(R.string.No_Eliminado));
                                                        dialog.dismiss();
                                                    }

                                                })
                                                .setNegativeButton(view.getContext().getResources().getString(R.string.Cancelar), new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .create().show();
                                    }
                                    if (accion.equals(getResources().getString(R.string.Modificar))) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("nameCategory", categoriaGasto);
                                        // Create an instance of the dialog fragment and show it*//*
                                        showEditGastoDialog(view, bundle);
                                    }
                                }
                            }
                    ).show();
            }
        });

        btnPlusCategory = (ImageButton) view.findViewById(R.id.btnPlusGastoCategory);
        btnPlusCategory.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewGastoDialog();
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

    public void showEditGastoDialog(View v, Bundle bundle) {
        DialogFragment newFragment = new EditCategoriaGastoDialog();
        newFragment.setArguments(bundle);
        newFragment.setTargetFragment(this,1);
        newFragment.show(getFragmentManager(),"EDITGASTO");
    }

    public void showNewGastoDialog() {
        DialogFragment newFragment = new NuevaCategoriaGastoDialog();
        newFragment.setTargetFragment(this,1);
        newFragment.show(getFragmentManager(),"NEWGASTO");
    }

    public void OnCategoriaGastoDialogSubmit(String result) {
        if(result.equals(String.valueOf(Activity.RESULT_OK))){
            GrupoGastoDAO grupoGastoDAO = new GrupoGastoDAO(getActivity());
            String[] newList = grupoGastoDAO.selectGrupos();
            ((CategoriaAdapter) listViewCategoriasGastos.getAdapter()).updateReceiptsList(
                    new ArrayList<String>(Arrays.asList(newList)));
        }
    }
}
