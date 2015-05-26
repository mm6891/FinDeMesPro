package globalsolutions.findemes.pantallas.activity;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import globalsolutions.findemes.R;
import globalsolutions.findemes.database.model.OptionItem;
import globalsolutions.findemes.pantallas.adapter.OptionAdapter;
import globalsolutions.findemes.pantallas.dialog.AcercaDeDialog;
import globalsolutions.findemes.pantallas.dialog.CategoriasGastosDialog;
import globalsolutions.findemes.pantallas.dialog.CategoriasIngresosDialog;
import globalsolutions.findemes.pantallas.dialog.FormatoFechaDialog;
import globalsolutions.findemes.pantallas.dialog.FormatoMonedaDialog;


/**
 * Created by Manuel on 23/02/2015.
 */
public class OpcionesActivity extends FragmentActivity {

    public static int [] prgmImages={R.drawable.plusoption,R.drawable.minusoption,
            R.drawable.dollaroption,/*R.drawable.calendaroption,*/
            R.drawable.databaseoption,R.drawable.padlockicon,R.drawable.staroption,
            R.drawable.appoption,/*R.drawable.prooption,R.drawable.developeroption,*/R.drawable.interrogationoption};

    private ListView listViewOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        //boton retroceder
        ImageButton btnReturn = (ImageButton) findViewById(R.id.btnBackButton);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                backActivity();
            }
        });
        String[] options = creaOpciones();
        ArrayList<OptionItem> opcionesArray = new ArrayList<OptionItem>(options.length);

        for(String op : options){
            OptionItem nuevo = new OptionItem();
            nuevo.setDescripcion(op);
            opcionesArray.add(nuevo);
        }

        // cargamos adaptador de opciones
        listViewOptions = (ListView) findViewById(R.id.listViewOptions);
        listViewOptions.setAdapter(new OptionAdapter(getApplicationContext(),opcionesArray,prgmImages));

        listViewOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position,long id) {
                //OptionItem optSelected = (OptionItem) listViewOptions.getItemAtPosition(position);

                switch (position){
                    case 0:
                        showCategoriasIngresosDialog();
                        break;
                    case 1:
                        showCategoriasGastosDialog();
                        break;
                    case 2:
                        showFormatoMonedaDialog();
                        break;
                    case 3:
                        showDatabaseActivity();
                        break;
                    case 4:
                        showPasswordActivity();
                        break;
                    case 5:
                        linkCalificarActivity();
                        break;
                    case 6:
                        showAcercaDeDialog();
                        break;
                    case 7:
                        showAyuda();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        backActivity();
    }

    private void backActivity(){
        Intent in = new Intent(OpcionesActivity.this, MainActivity.class);
        startActivity(in);
        setResult(RESULT_OK);
        finish();
    }

    public void showCategoriasIngresosDialog() {
        DialogFragment newFragment = new CategoriasIngresosDialog();
        newFragment.show(getFragmentManager(), "INGRESOS");
    }

    public void showCategoriasGastosDialog() {
        DialogFragment newFragment = new CategoriasGastosDialog();
        newFragment.show(getFragmentManager(),"GASTOS");
    }

    public void showFormatoFechaDialog() {
        DialogFragment newFragment = new FormatoFechaDialog();
        newFragment.show(getFragmentManager(),"FORMATO_FECHA");
    }

    public void showFormatoMonedaDialog() {
        DialogFragment newFragment = new FormatoMonedaDialog();
        newFragment.show(getFragmentManager(),"FORMATO_MONEDA");
    }

    public void showPasswordActivity(){
        Intent intent = new Intent(OpcionesActivity.this, OptionActivityPassword.class);
        startActivity(intent);
        finish();
    }

    public void showDatabaseActivity(){
        Intent intent = new Intent(OpcionesActivity.this, OptionActivityDatabase.class);
        startActivity(intent);
        finish();
    }

    public void linkCalificarActivity(){
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void showAcercaDeDialog(){
        DialogFragment newFragment = new AcercaDeDialog();
        newFragment.show(getFragmentManager(), "ACERCADE");
    }

    public void showAyuda(){
        Intent intent = new Intent(OpcionesActivity.this, OptionActivityAyuda.class);
        startActivity(intent);
        finish();
    }

    public String[] creaOpciones(){
        String[] options = {
        getResources().getString(R.string.tituloCategoriaIngresos),
        getResources().getString(R.string.tituloCategoriaGastos),
        getResources().getString(R.string.OPCIONES_FORMATO_MONEDA),
        /**getResources().getString(R.string.OPCIONES_FORMATO_FECHA),*/
        getResources().getString(R.string.OPCIONES_BASEDATOS),
        getResources().getString(R.string.OPCIONES_CONTRASENYA),
        getResources().getString(R.string.OPCIONES_CALIFICAR),
        getResources().getString(R.string.OPCIONES_ACERCA_APLICACION),
        /*getResources().getString(R.string.OPCIONES_FUNCIONES_PRO),
        getResources().getString(R.string.OPCIONES_DESARROLLO_CONTACTO),*/
        getResources().getString(R.string.OPCIONES_AYUDA)};
        return options;
    }
}
