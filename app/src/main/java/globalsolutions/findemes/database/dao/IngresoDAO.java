package globalsolutions.findemes.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import globalsolutions.findemes.database.model.GrupoIngreso;
import globalsolutions.findemes.database.model.Ingreso;
import globalsolutions.findemes.database.util.MyDatabaseHelper;

/**
 * Created by manuel.molero on 03/02/2015.
 */
public class IngresoDAO {
    private MyDatabaseHelper dbHelper;

    private SQLiteDatabase database;

    //tables´s name
    public final static String INGRESOS_TABLA="Ingresos";

    //columns table Ingresos
    public final static String INGRESOS_ID="_id"; // id value for gasto
    public final static String INGRESOS_DESC="descripcion";  // nombre del gasto
    public final static String INGRESOS_VALOR="valor";  // valor del gasto
    public final static String INGRESOS_FECHA="fecha";  // fecha del gasto
    public final static String INGRESOS_GRUPO="grupoingreso";  // grupo al que pertenece el gasto, referencia
    public final static String INGRESOS_REGISTRO_ID="_idRegistro"; // id value for registro

    /**
     *
     * @param context
     */
    public IngresoDAO(Context context){
        dbHelper = new MyDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long createRecords(Ingreso ingreso){
        ContentValues values = new ContentValues();
        values.put(INGRESOS_DESC, ingreso.getDescripcion());
        values.put(INGRESOS_VALOR, ingreso.getValor());
        values.put(INGRESOS_GRUPO, ingreso.getGrupoIngreso().getGrupo());
        values.put(INGRESOS_FECHA, ingreso.getFecha());
        values.put(INGRESOS_REGISTRO_ID, ingreso.get_idRegistro());

        return database.insert(INGRESOS_TABLA, null, values);
    }

    public Ingreso[] selectIngresos() {
        Ingreso[] ret;
        String[] cols = new String[] {INGRESOS_ID,INGRESOS_GRUPO, INGRESOS_DESC, INGRESOS_VALOR,INGRESOS_FECHA,INGRESOS_REGISTRO_ID};
        Cursor mCursor = database.query(true, INGRESOS_TABLA,cols,null
                , null, null, null, null, null);
        ret = new Ingreso[mCursor.getCount()];
        int i = 0;
        mCursor.moveToFirst();
        while (mCursor.isAfterLast() == false) {
            Ingreso nuevoIngreso = new Ingreso();
            nuevoIngreso.set_id(mCursor.getInt(0));
            GrupoIngreso categoria = new GrupoIngreso();
            categoria.setGrupo(mCursor.getString(1));
            nuevoIngreso.setGrupoIngreso(categoria);
            nuevoIngreso.setDescripcion(mCursor.getString(2));
            nuevoIngreso.setValor(mCursor.getString(3));
            nuevoIngreso.setFecha(mCursor.getString(4));
            nuevoIngreso.set_idRegistro(mCursor.getInt(5));

            ret[i] = nuevoIngreso;
            i++;
            mCursor.moveToNext();
        }
        return ret; // iterate to get each value.
    }

    public Ingreso[] selectIngresosByRegistroID(int registroID) {
        Ingreso[] ret;
        String[] cols = new String[] {INGRESOS_ID,INGRESOS_GRUPO, INGRESOS_DESC, INGRESOS_VALOR,INGRESOS_FECHA,INGRESOS_REGISTRO_ID};
        String[] args = new String[]{String.valueOf(registroID)};
        Cursor mCursor = database.query(true, INGRESOS_TABLA,cols,INGRESOS_REGISTRO_ID + "=?"
                , args, null, null, null, null);
        ret = new Ingreso[mCursor.getCount()];
        int i = 0;
        mCursor.moveToFirst();
        while (mCursor.isAfterLast() == false) {
            Ingreso nuevoIngreso = new Ingreso();
            nuevoIngreso.set_id(mCursor.getInt(0));
            GrupoIngreso categoria = new GrupoIngreso();
            categoria.setGrupo(mCursor.getString(1));
            nuevoIngreso.setGrupoIngreso(categoria);
            nuevoIngreso.setDescripcion(mCursor.getString(2));
            nuevoIngreso.setValor(mCursor.getString(3));
            nuevoIngreso.setFecha(mCursor.getString(4));
            nuevoIngreso.set_idRegistro(mCursor.getInt(5));

            ret[i] = nuevoIngreso;
            i++;
            mCursor.moveToNext();
        }
        return ret; // iterate to get each value.
    }

    public boolean deleteIngreso(int _id){

        return  database.delete(INGRESOS_TABLA,
                INGRESOS_ID + "=" + _id, null) > 0;
    }

    public boolean updateIngreso(Ingreso antiguo, Ingreso nuevo){
        String[] args = new String[] {String.valueOf(antiguo.get_id())};

        //Establecemos los campos-valores a actualizar
        ContentValues valores = new ContentValues();
        valores.put(INGRESOS_GRUPO,nuevo.getGrupoIngreso().getGrupo());
        valores.put(INGRESOS_DESC,nuevo.getDescripcion());
        valores.put(INGRESOS_VALOR,nuevo.getValor());
        valores.put(INGRESOS_FECHA,nuevo.getFecha());

        int rows = database.update(INGRESOS_TABLA,valores,INGRESOS_ID + "=?",args);
        return rows > 0;
    }
}
