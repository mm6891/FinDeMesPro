package globalsolutions.findemes.pantallas.util;

import java.io.IOException;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import globalsolutions.findemes.R;

/**
 * Created by manuel.molero on 29/05/2015.
 */
public class DropBoxUtil {

    private static final String appKey = "tqf9laifyog9tt4";
    private static final String appSecret = "wwsjfniwy6fr2jw";

    public static boolean backup(Context c,String fileName){
        boolean realizado = true;
        /*try {
            final String TEST_DATA = "Hello Dropbox";

            DbxAccountManager mDbxAcctMgr = DbxAccountManager.getInstance(c, appKey, appSecret);
            DbxPath testPath = new DbxPath(DbxPath.ROOT, fileName);
            // Create DbxFileSystem for synchronized file access.
            DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());

            // Create a test file only if it doesn't already exist.
            if (!dbxFs.exists(testPath)) {
                DbxFile testFile = dbxFs.create(testPath);
                try {
                    testFile.writeString(TEST_DATA);
                } finally {
                    testFile.close();
                }
            }
        } catch (IOException e) {
            realizado = false;
        }*/
        return realizado;
    }
}

