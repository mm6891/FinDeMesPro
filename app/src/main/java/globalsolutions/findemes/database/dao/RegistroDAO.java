package globalsolutions.findemes.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import globalsolutions.findemes.database.model.Registro;
import globalsolutions.findemes.database.model.RegistroItem;
import globalsolutions.findemes.database.util.MyDatabaseHelper;

/**
 * Created by manuel.molero on 03/02/2015.
 */
public class RegistroDAO {
    private MyDatabaseHelper dbHelper;

    private SQLiteDatabase database;

    //tables´s name
    public final static String REGISTROS_TABLA="Registros";

    //columns table Registros
    public final static String REGISTROS_ID="_id"; // id value for registro
    public final static String REGISTROS_DESC="descripcion";  // nombre del registro
    public final static String REGISTROS_PERIOD="periodicidad";  // peridodicidad del registro (mensual, anual)
    public final static String REGISTROS_TIPO="tipo";  // tipo del registro (ingreso, gasto)
    public final static String REGISTROS_VALOR="valor";  // valor del registro
    public final static String REGISTROS_GRUPO="grupo";  // grupo al que pertenece el registro
    public final static String REGISTROS_ACTIVO="activo";  // indicador 0/1 si el registro esta activado o no
    public final static String REGISTROS_FECHA="fecha";  // fecha activacion del registro

    /**
     *
     * @param context
     */
    public RegistroDAO(Context context){
        dbHelper = new MyDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long createRecords(Registro registro){
        ContentValues values = new ContentValues();
        values.put(REGISTROS_DESC, registro.getDescripcion());
        values.put(REGISTROS_PERIOD, registro.getPeriodicidad());
        values.put(REGISTROS_TIPO, registro.getTipo());
        values.put(REGISTROS_VALOR, registro.getValor());
        values.put(REGISTROS_GRUPO, registro.getGrupo());
        values.put(REGISTROS_ACTIVO, registro.getActivo());
        values.put(REGISTROS_FECHA, registro.getFecha());

        return database.insert(REGISTROS_TABLA, null, values);
    }

    public Registro[] selectRegistros() {
        Registro[] ret;
        String[] cols = new String[] {REGISTROS_ID,REGISTROS_DESC, REGISTROS_PERIOD, REGISTROS_TIPO,REGISTROS_VALOR,REGISTROS_GRUPO,REGISTROS_ACTIVO,REGISTROS_FECHA};
        Cursor mCursor = database.query(true, REGISTROS_TABLA,cols,null
                , null, null, null, null, null);
        ret = new Registro[mCursor.getCount()];
        int i = 0;
        mCursor.moveToFirst();
        while (mCursor.isAfterLast() == false) {
            Registro nuevoRegistro = new Registro();
            nuevoRegistro.set_id(mCursor.getInt(0));
            nuevoRegistro.setDescripcion(mCursor.getString(1));
            nuevoRegistro.setPeriodicidad(mCursor.getString(2));
            nuevoRegistro.setTipo(mCursor.getString(3));
            nuevoRegistro.setValor(mCursor.getString(4));
            nuevoRegistro.setGrupo(mCursor.getString(5));
            nuevoRegistro.setActivo(Integer.valueOf(mCursor.getInt(6)));
            nuevoRegistro.setFecha(mCursor.getString(7));


            ret[i] = nuevoRegistro;
            i++;
            mCursor.moveToNext();
        }
        return ret; // iterate to get each value.
    }

    public ArrayList<RegistroItem> selectRegistrosItems() {
        ArrayList<RegistroItem> ret;
        String[] cols = new String[] {REGISTROS_ID,REGISTROS_DESC, REGISTROS_PERIOD, REGISTROS_TIPO,REGISTROS_VALOR,REGISTROS_GRUPO,REGISTROS_ACTIVO,REGISTROS_FECHA};
        Cursor mCursor = database.query(true, REGISTROS_TABLA,cols,null
                , null, null, null, null, null);
        ret = new ArrayList<RegistroItem>(mCursor.getCount());
        int i = 0;
        mCursor.moveToFirst();
        while (mCursor.isAfterLast() == false) {
            RegistroItem nuevoRegistro = new RegistroItem();
            nuevoRegistro.set_id(mCursor.getInt(0));
            nuevoRegistro.setDescripcion(mCursor.getString(1));
            nuevoRegistro.setPeriodicidad(mCursor.getString(2));
            nuevoRegistro.setTipo(mCursor.getString(3));
            nuevoRegistro.setValor(mCursor.getString(4));
            nuevoRegistro.setGrupo(mCursor.getString(5));
            nuevoRegistro.setActivo(Integer.valueOf(mCursor.getInt(6)));
            nuevoRegistro.setFecha(mCursor.getString(7));

            ret.add(nuevoRegistro);
            i++;
            mCursor.moveToNext();
        }
        return ret; // iterate to get each value.
    }

    public boolean deleteRegistro(int _id){

        return  database.delete(REGISTROS_TABLA,
                REGISTROS_ID + "=" + _id , null) > 0;
    }

    public boolean updateRegistro(Registro antiguo, Registro nuevo){
        String[] args = new String[] {String.valueOf(antiguo.get_id())};

        //Establecemos los campos-valores a actualizar
        ContentValues valores = new ContentValues();
        valores.put(REGISTROS_DESC,nuevo.getDescripcion());
        valores.put(REGISTROS_PERIOD,nuevo.getPeriodicidad());
        valores.put(REGISTROS_TIPO,nuevo.getTipo());
        valores.put(REGISTROS_VALOR,nuevo.getValor());
        valores.put(REGISTROS_GRUPO,nuevo.getGrupo());
        valores.put(REGISTROS_ACTIVO,nuevo.getActivo());
        valores.put(REGISTROS_FECHA,nuevo.getFecha());

        int rows = database.update(REGISTROS_TABLA,valores,REGISTROS_ID + "=?",args);
        return rows > 0;
    }
}