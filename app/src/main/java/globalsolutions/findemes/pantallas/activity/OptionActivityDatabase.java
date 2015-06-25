package globalsolutions.findemes.pantallas.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import globalsolutions.findemes.R;
import globalsolutions.findemes.database.util.MyDatabaseHelper;
import globalsolutions.findemes.pantallas.util.FileDialog;
import globalsolutions.findemes.pantallas.util.UploadDatabase;
import globalsolutions.findemes.pantallas.util.Util;


/**
 * Created by Manuel on 23/02/2015.
 */
public class OptionActivityDatabase extends Activity {


    //propiedades dropbox
    private static final String appKey = "tqf9laifyog9tt4";
    private static final String appSecret = "wwsjfniwy6fr2jw";
    private static final String ACCOUNT_PREFS_NAME = "prefs";
    private static final String ACCESS_KEY_NAME = "ACCESS_KEY";
    private static final String ACCESS_SECRET_NAME = "ACCESS_SECRET";
    DropboxAPI<AndroidAuthSession> mApi;
    private final String DATABASES_DIR = "/Databases/";


    private ImageButton guardar;
    private ImageButton importar;
    private ImageButton btnImportarDropBoxDB;
    private FileDialog fileDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_activity_database);

        //boton retroceder
        ImageButton btnReturn = (ImageButton) findViewById(R.id.btnBackButton);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                backActivity();
            }
        });

        guardar = (ImageButton) findViewById(R.id.btnGuardarDB);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String state = Environment.getExternalStorageState();
                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        File sd = Environment.getExternalStorageDirectory();

                        if (sd.canWrite()) {
                            String currentDBPath = MyDatabaseHelper.DB_PATH + MyDatabaseHelper.DATABASE_NAME;
                            String backupDBPath = "/findemes/" + MyDatabaseHelper.DATABASE_NAME;
                            File currentDB = new File(currentDBPath);
                            File backupDB = new File(sd, backupDBPath);
                            if(!backupDB.exists())
                                backupDB.getParentFile().mkdirs();

                            if (currentDB.exists()) {
                                Util.copyFile(new FileInputStream(currentDB), new FileOutputStream(backupDB));
                                Util.showToast(getApplicationContext(), getResources().getString(R.string.Creado));
                            }
                        }
                        else
                            Util.showToast(getApplicationContext(), getResources().getString(R.string.No_Creado));
                    }
                    else {
                        Util.showToast(getApplicationContext(), getResources().getString(R.string.No_Creado));
                    }

                }catch (IOException ex){
                    Util.showToast(getApplicationContext(), getResources().getString(R.string.No_Creado));
                }
            }
        });

        importar = (ImageButton) findViewById(R.id.btnImportarDB);
        importar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
                fileDialog = new FileDialog(OptionActivityDatabase.this, mPath);
                fileDialog.setFileEndsWith(MyDatabaseHelper.DATABASE_NAME);
                fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
                    public void fileSelected(File file) {
                        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getApplicationContext());
                        try {
                            boolean realizado = dbHelper.importDatabase(file.getPath());
                            if(realizado)
                                Util.showToast(getApplicationContext(), getResources().getString(R.string.Creado));
                            else
                                Util.showToast(getApplicationContext(), getResources().getString(R.string.No_Creado));
                        } catch (IOException e) {
                            Util.showToast(getApplicationContext(), getResources().getString(R.string.No_Creado));
                        }
                    }
                });
                fileDialog.showDialog();
            }
        });
        //importar db a dropbox, realizar backup
        btnImportarDropBoxDB = (ImageButton) findViewById(R.id.btnImportarDropBoxDB);
        btnImportarDropBoxDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
                fileDialog = new FileDialog(OptionActivityDatabase.this, mPath);
                fileDialog.setFileEndsWith(MyDatabaseHelper.DATABASE_NAME);
                fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
                    public void fileSelected(File file) {
                        UploadDatabase upload = new UploadDatabase(OptionActivityDatabase.this,getApplicationContext(), mApi, DATABASES_DIR, file);
                        upload.execute();

                    }
                });
                fileDialog.showDialog();
            }
        });

        // We create a new AuthSession so that we can use the Dropbox API.
        AndroidAuthSession session = buildSession();
        mApi = new DropboxAPI<AndroidAuthSession>(session);
        // Start the remote authentication
        mApi.getSession().startOAuth2Authentication(OptionActivityDatabase.this);
    }

    @Override
    public void onBackPressed() {
        backActivity();
    }

    private void backActivity(){
        Intent in = new Intent(OptionActivityDatabase.this, OpcionesActivity.class);
        startActivity(in);
        setResult(RESULT_OK);
        finish();
    }

    //metodos y utilidades dropbox
    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     */
    private void loadAuth(AndroidAuthSession session) {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        String key = prefs.getString(ACCESS_KEY_NAME, null);
        String secret = prefs.getString(ACCESS_SECRET_NAME, null);
        if (key == null || secret == null || key.length() == 0 || secret.length() == 0) return;

        if (key.equals("oauth2:")) {
            // If the key is set to "oauth2:", then we can assume the token is for OAuth 2.
            session.setOAuth2AccessToken(secret);
        } else {
            // Still support using old OAuth 1 tokens.
            session.setAccessTokenPair(new AccessTokenPair(key, secret));
        }
    }

    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(appKey, appSecret);

        AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
        //loadAuth(session);
        session.setOAuth2AccessToken(appSecret);
        return session;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mApi.getSession().finishAuthentication();

                String accessToken = mApi.getSession().getOAuth2AccessToken();
            } catch (IllegalStateException e) {
                Util.showToast(getApplicationContext(), e.getMessage());
            }
        }
    }
}
