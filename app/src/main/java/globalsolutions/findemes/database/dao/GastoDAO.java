package globalsolutions.findemes.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import globalsolutions.findemes.database.model.Gasto;
import globalsolutions.findemes.database.model.GrupoGasto;
import globalsolutions.findemes.database.util.MyDatabaseHelper;

/**
 * Created by manuel.molero on 03/02/2015.
 */
public class GastoDAO {
    private MyDatabaseHelper dbHelper;

    private SQLiteDatabase database;

    //tables´s name
    public final static String GASTOS_TABLA="Gastos";

    //columns table Gastos
    public final static String GASTOS_ID="_id"; // id value for gasto
    public final static String GASTOS_DESC="descripcion";  // nombre del gasto
    public final static String GASTOS_VALOR="valor";  // valor del gasto
    public final static String GASTOS_FECHA="fecha";  // fecha del gasto
    public final static String GASTOS_GRUPO="grupogasto";  // grupo al que pertenece el gasto, referencia
    public final static String GASTOS_REGISTRO_ID="_idRegistro"; // id value for registro

    /**
     *
     * @param context
     */
    public GastoDAO(Context context){
        dbHelper = new MyDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long createRecords(Gasto gasto){
        ContentValues values = new ContentValues();
        values.put(GASTOS_DESC, gasto.getDescripcion());
        values.put(GASTOS_VALOR, gasto.getValor());
        values.put(GASTOS_GRUPO, gasto.getGrupoGasto().getGrupo());
        values.put(GASTOS_FECHA, gasto.getFecha());
        values.put(GASTOS_REGISTRO_ID, gasto.get_idRegistro());

        return database.insert(GASTOS_TABLA, null, values);
    }

    public Gasto[] selectGastos() {
        Gasto[] ret;
        String[] cols = new String[] {GASTOS_ID,GASTOS_GRUPO, GASTOS_DESC, GASTOS_VALOR,GASTOS_FECHA,GASTOS_REGISTRO_ID};
        Cursor mCursor = database.query(true, GASTOS_TABLA,cols,null
                , null, null, null, null, null);
        ret = new Gasto[mCursor.getCount()];
        int i = 0;
        mCursor.moveToFirst();
        while (mCursor.isAfterLast() == false) {
            Gasto nuevoGasto = new Gasto();
            nuevoGasto.set_id(mCursor.getInt(0));
            GrupoGasto categoria = new GrupoGasto();
            categoria.setGrupo(mCursor.getString(1));
            nuevoGasto.setGrupoGasto(categoria);
            nuevoGasto.setDescripcion(mCursor.getString(2));
            nuevoGasto.setValor(mCursor.getString(3));
            nuevoGasto.setFecha(mCursor.getString(4));
            nuevoGasto.set_idRegistro(mCursor.getInt(5));

            ret[i] = nuevoGasto;
            i++;
            mCursor.moveToNext();
        }
        return ret; // iterate to get each value.
    }

    public Gasto[] selectGastosByRegistroID(int registroID) {
        Gasto[] ret;
        String[] cols = new String[] {GASTOS_ID,GASTOS_GRUPO, GASTOS_DESC, GASTOS_VALOR,GASTOS_FECHA,GASTOS_REGISTRO_ID};
        String[] args = new String[]{String.valueOf(registroID)};
        Cursor mCursor = database.query(true, GASTOS_TABLA,cols,GASTOS_REGISTRO_ID + "=?"
                , args, null, null, null, null);
        ret = new Gasto[mCursor.getCount()];
        int i = 0;
        mCursor.moveToFirst();
        while (mCursor.isAfterLast() == false) {
            Gasto nuevoGasto = new Gasto();
            nuevoGasto.set_id(mCursor.getInt(0));
            GrupoGasto categoria = new GrupoGasto();
            categoria.setGrupo(mCursor.getString(1));
            nuevoGasto.setGrupoGasto(categoria);
            nuevoGasto.setDescripcion(mCursor.getString(2));
            nuevoGasto.setValor(mCursor.getString(3));
            nuevoGasto.setFecha(mCursor.getString(4));
            nuevoGasto.set_idRegistro(mCursor.getInt(5));

            ret[i] = nuevoGasto;
            i++;
            mCursor.moveToNext();
        }
        return ret; // iterate to get each value.
    }

    public boolean deleteGasto(int _id){

        return  database.delete(GASTOS_TABLA,
                GASTOS_ID + "=" + _id , null) > 0;
    }

    public boolean updateGasto(Gasto antiguo, Gasto nuevo){
        String[] args = new String[] {String.valueOf(antiguo.get_id())};

        //Establecemos los campos-valores a actualizar
        ContentValues valores = new ContentValues();
        valores.put(GASTOS_GRUPO,nuevo.getGrupoGasto().getGrupo());
        valores.put(GASTOS_DESC,nuevo.getDescripcion());
        valores.put(GASTOS_VALOR,nuevo.getValor());
        valores.put(GASTOS_FECHA,nuevo.getFecha());

        int rows = database.update(GASTOS_TABLA,valores,GASTOS_ID + "=?",args);
        return rows > 0;
    }

}
