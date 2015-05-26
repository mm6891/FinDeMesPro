package globalsolutions.findemes.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import globalsolutions.findemes.database.model.Password;
import globalsolutions.findemes.database.model.Registro;
import globalsolutions.findemes.database.model.RegistroItem;
import globalsolutions.findemes.database.util.MyDatabaseHelper;

/**
 * Created by manuel.molero on 03/02/2015.
 */
public class PasswordDAO {
    private MyDatabaseHelper dbHelper;

    private SQLiteDatabase database;

    //tables´s name
    public final static String PASSWORD_TABLA="Password";

    //columns table Password
    public final static String PASSWORD_ID = "_id"; // id value for pass
    public final static String PASSWORD_PASSWORD="password";  // password
    public final static String PASSWORD_MAIL="mail";  // correo al que se envia la pass
    public final static String PASSWORD_ACTIVO="activo";  // password activa
    public final static String PASSWORD_FECHA="fecha";  // fecha que se crea la pass

    /**
     *
     * @param context
     */
    public PasswordDAO(Context context){
        dbHelper = new MyDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long createRecords(Password registro){
        ContentValues values = new ContentValues();
        values.put(PASSWORD_PASSWORD, registro.getPassword());
        values.put(PASSWORD_MAIL, registro.getMail());
        values.put(PASSWORD_ACTIVO, registro.getActivo());

        return database.insert(PASSWORD_TABLA, null, values);
    }

    public Password selectPassword() {
        Password ret;
        String[] cols = new String[] {PASSWORD_ID,PASSWORD_PASSWORD, PASSWORD_MAIL, PASSWORD_ACTIVO,PASSWORD_FECHA};
        Cursor mCursor = database.query(true, PASSWORD_TABLA,cols,null
                , null, null, null, null, null);
        ret = new Password();
        mCursor.moveToFirst();
        while (mCursor.isAfterLast() == false) {
            ret.set_id(mCursor.getInt(0));
            ret.setPassword(mCursor.getString(1));
            ret.setMail(mCursor.getString(2));
            ret.setActivo(String.valueOf(mCursor.getInt(3)));
            ret.setFecha(mCursor.getString(4));
            mCursor.moveToNext();
        }
        return ret; // iterate to get each value.
    }

    public boolean deletePassword(int _id){

        return  database.delete(PASSWORD_TABLA,
                PASSWORD_ID + "=" + _id , null) > 0;
    }

    public boolean updatePassword(Password antiguo, Password nuevo){
        String[] args = new String[] {String.valueOf(antiguo.get_id())};

        //Establecemos los campos-valores a actualizar
        ContentValues valores = new ContentValues();
        valores.put(PASSWORD_PASSWORD,nuevo.getPassword());
        valores.put(PASSWORD_MAIL,nuevo.getMail());
        valores.put(PASSWORD_ACTIVO,nuevo.getActivo());

        int rows = database.update(PASSWORD_TABLA,valores,PASSWORD_ID + "=?",args);
        return rows > 0;
    }
}