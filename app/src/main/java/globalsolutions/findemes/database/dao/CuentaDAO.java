package globalsolutions.findemes.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import globalsolutions.findemes.database.model.Cuenta;
import globalsolutions.findemes.database.model.CuentaItem;
import globalsolutions.findemes.database.model.Registro;
import globalsolutions.findemes.database.model.RegistroItem;
import globalsolutions.findemes.database.util.MyDatabaseHelper;

/**
 * Created by manuel.molero on 03/02/2015.
 */
public class CuentaDAO {
    private MyDatabaseHelper dbHelper;

    private SQLiteDatabase database;

    //tables´s name
    public final static String CUENTAS_TABLA="Cuentas";

    //columns table Registros
    public final static String CUENTAS_ID="_id"; // id value for cuenta
    public final static String CUENTAS_NOMBRE="nombre";  // nombre de la cuenta, identificativo
    public final static String CUENTAS_NUMERO="numeroCuenta";  // numero de cuenta, opcional
    public final static String CUENTAS_FECHA="fecha";  // fecha activacion del registro

    /**
     *
     * @param context
     */
    public CuentaDAO(Context context){
        dbHelper = new MyDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long createRecords(Cuenta cuenta){
        ContentValues values = new ContentValues();
        values.put(CUENTAS_NOMBRE, cuenta.getNombre());
        values.put(CUENTAS_NUMERO, cuenta.getNumero());

        return database.insert(CUENTAS_TABLA, null, values);
    }

    public Cuenta[] selectCuentas() {
        Cuenta[] ret;
        String[] cols = new String[] {CUENTAS_ID,CUENTAS_NOMBRE, CUENTAS_NUMERO, CUENTAS_FECHA};
        Cursor mCursor = database.query(true, CUENTAS_TABLA,cols,null
                , null, null, null, null, null);
        ret = new Cuenta[mCursor.getCount()];
        int i = 0;
        mCursor.moveToFirst();
        while (mCursor.isAfterLast() == false) {
            Cuenta nuevaCuenta = new Cuenta();
            nuevaCuenta.set_id(mCursor.getInt(0));
            nuevaCuenta.setNombre(mCursor.getString(1));
            nuevaCuenta.setNumero(mCursor.getString(2));
            nuevaCuenta.setFecha(mCursor.getString(3));

            ret[i] = nuevaCuenta;
            i++;
            mCursor.moveToNext();
        }
        return ret; // iterate to get each value.
    }

    public ArrayList<CuentaItem> selectCuentasItems() {
        ArrayList<CuentaItem> ret;
        String[] cols = new String[] {CUENTAS_ID,CUENTAS_NOMBRE, CUENTAS_NUMERO, CUENTAS_FECHA};
        Cursor mCursor = database.query(true, CUENTAS_TABLA,cols,null
                , null, null, null, null, null);
        ret = new ArrayList<CuentaItem>(mCursor.getCount());
        int i = 0;
        mCursor.moveToFirst();
        while (mCursor.isAfterLast() == false) {
            CuentaItem nuevoRegistro = new CuentaItem();
            nuevoRegistro.set_id(mCursor.getInt(0));
            nuevoRegistro.setNombre(mCursor.getString(1));
            nuevoRegistro.setNumero(mCursor.getString(2));
            nuevoRegistro.setFecha(mCursor.getString(3));

            ret.add(nuevoRegistro);
            i++;
            mCursor.moveToNext();
        }
        return ret; // iterate to get each value.
    }

    public boolean deleteCuenta(int _id){
        return  database.delete(CUENTAS_TABLA,
                CUENTAS_ID + "=" + _id , null) > 0;
    }

    public boolean updateCuenta(Cuenta antiguo, Cuenta nuevo){
        String[] args = new String[] {String.valueOf(antiguo.get_id())};

        //Establecemos los campos-valores a actualizar
        ContentValues valores = new ContentValues();
        valores.put(CUENTAS_NOMBRE,nuevo.getNombre());
        valores.put(CUENTAS_NUMERO,nuevo.getNumero());

        int rows = database.update(CUENTAS_TABLA,valores,CUENTAS_ID + "=?",args);
        return rows > 0;
    }
}