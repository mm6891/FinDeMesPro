package globalsolutions.findemes.database.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import globalsolutions.findemes.pantallas.util.Util;

/**
 * Created by manuel.molero on 03/02/2015.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DBFinDeMes";

    private static final int DATABASE_VERSION = 2;
    public static String DB_PATH = "";

    // Database creation sql statement
    private static final String CREATE_TABLE_GASTOS =
            "create table Gastos( _id integer primary key," +
                    "descripcion text not null," +
                    "valor text not null," +
                    "fecha TIMESTAMP NOT NULL DEFAULT current_timestamp," +
                    "_idRegistro integer," +
                    "grupogasto text not null," +
                    "  FOREIGN KEY(grupogasto) REFERENCES Grupo_Gastos(_id));";

    private static final String CREATE_TABLE_GRUPO_GASTOS =
            "create table Grupo_Gastos( _id integer primary key," +
                    "grupo text not null);";

    private static final String CREATE_TABLE_INGRESOS =
            "create table Ingresos( _id integer primary key," +
                    "descripcion text not null," +
                    "valor text not null," +
                    "fecha TIMESTAMP NOT NULL DEFAULT current_timestamp," +
                    "_idRegistro integer," +
                    "grupoingreso text not null," +
                    "  FOREIGN KEY(grupoingreso) REFERENCES Grupo_Ingresos(_id));";

    private static final String CREATE_TABLE_GRUPO_INGRESOS =
            "create table Grupo_Ingresos( _id integer primary key," +
                    "grupo text not null);";

    private static final String CREATE_TABLE_REGISTROS =
            "create table Registros( _id integer primary key," +
                    "descripcion text not null," +
                    "periodicidad text not null," +
                    "tipo text not null," +
                    "valor text not null," +
                    "grupo text not null," +
                    "activo integer not null," +
                    "fecha TIMESTAMP NOT NULL DEFAULT current_timestamp);";

    private static final String CREATE_TABLE_PASSWORD =
            "create table Password( _id integer primary key," +
                    "password text not null," +
                    "mail text not null," +
                    "activo integer not null," +
                    "fecha TIMESTAMP NOT NULL DEFAULT current_timestamp);";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
            database.execSQL(CREATE_TABLE_GASTOS);
            database.execSQL(CREATE_TABLE_GRUPO_GASTOS);
            database.execSQL(CREATE_TABLE_INGRESOS);
            database.execSQL(CREATE_TABLE_GRUPO_INGRESOS);
            database.execSQL(CREATE_TABLE_REGISTROS);
            database.execSQL(CREATE_TABLE_PASSWORD);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(MyDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS Gastos");
        database.execSQL("DROP TABLE IF EXISTS Grupo_Gastos");
        database.execSQL("DROP TABLE IF EXISTS Ingresos");
        database.execSQL("DROP TABLE IF EXISTS Grupo_Ingresos");
        database.execSQL("DROP TABLE IF EXISTS Registros");
        database.execSQL("DROP TABLE IF EXISTS Password");

        onCreate(database);
    }

    /**
     * Check if the database exist
     *
     * @return true if it exists, false if it doesn't
     */
    public static boolean checkDataBase(Context context) {
        SQLiteDatabase checkDB = null;
        int count = 0;
        try {
            if(android.os.Build.VERSION.SDK_INT >= 17){
                DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
            }
            else
            {
                DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
            }
            checkDB = SQLiteDatabase.openDatabase(DB_PATH + DATABASE_NAME, null,
                    SQLiteDatabase.OPEN_READONLY);

            String selectRegistros = "SELECT COUNT(*) FROM Grupo_Gastos";

            Cursor mCursor = checkDB.query(true, "Grupo_Gastos",new String[]{"_id"},null
                    , null, null, null, null, null);
            count = mCursor.getCount();

            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
            return  false;
        }
        return count > 0;
    }

    /**
     * Copies the database file at the specified location over the current
     * internal application database.
     * */
    public boolean importDatabase(String dbPath) throws IOException {

        // Close the SQLiteOpenHelper so it will commit the created empty
        // database to internal storage.
        close();
        File newDb = new File(dbPath);
        File oldDb = new File(DB_PATH + DATABASE_NAME);
        if (newDb.exists()) {
            Util.copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
            // Access the copied database so SQLiteHelper will cache it and mark
            // it as created.
            getWritableDatabase().close();
            return true;
        }
        return false;
    }
}
