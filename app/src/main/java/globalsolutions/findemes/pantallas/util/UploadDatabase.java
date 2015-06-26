package globalsolutions.findemes.pantallas.util;

import android.content.Context;
import com.dropbox.client2.DropboxAPI;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import com.dropbox.client2.DropboxAPI.UploadRequest;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxFileSizeException;
import com.dropbox.client2.exception.DropboxIOException;
import com.dropbox.client2.exception.DropboxParseException;
import com.dropbox.client2.exception.DropboxPartialFileException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.exception.DropboxUnlinkedException;

import globalsolutions.findemes.R;
import globalsolutions.findemes.pantallas.activity.OptionActivityDatabase;

/**
 * Created by manuel.molero on 29/05/2015.
 */
    public class UploadDatabase extends AsyncTask<Void, Long, Boolean> {

        private DropboxAPI<?> mApi;
        private String mPath;
        private File mFile;

        private long mFileLen;
        private UploadRequest mRequest;
        private Context mContext;
        private ProgressDialog mDialog;

        private String mErrorMsg;


        public UploadDatabase(OptionActivityDatabase activity,Context context, DropboxAPI<?> api, String dropboxPath,
                             File file) {
            // We set the context this way so we don't accidentally leak activities
            mContext = context.getApplicationContext();

            mDialog = new ProgressDialog(activity);
            mFileLen = file.length();
            mApi = api;
            mPath = dropboxPath;
            mFile = file;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // By creating a request, we get a handle to the putFile operation,
                // so we can cancel it later if we want to
                FileInputStream fis = new FileInputStream(mFile);
                String path = mPath + mFile.getName();
                mRequest = mApi.putFileOverwriteRequest(path, fis, mFile.length(),
                        new ProgressListener() {
                            @Override
                            public long progressInterval() {
                                // Update the progress bar every half-second or so
                                return 100;
                            }

                            @Override
                            public void onProgress(long bytes, long total) {
                                publishProgress(bytes);
                            }
                        });

                if (mRequest != null) {
                    mRequest.upload();
                    //compartir enlace
                    // Get the metadata for a directory
                    /*DropboxAPI.Entry dirent = mApi.metadata(mPath, 1000, null, true, null);

                    for (DropboxAPI.Entry ent : dirent.contents) {

                        String shareAddress = null;
                        if (!ent.isDir) {
                            DropboxAPI.DropboxLink shareLink = mApi.share(ent.path);
                            shareAddress = getShareURL(shareLink.url).replaceFirst("https://www", "https://dl");
                            //Log.d(TAG, "dropbox share link " + shareAddress);
                            //Envio de correo
                            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                            emailIntent.setType("message/rfc822");
                            //emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{txtMailTo.getText().toString()});
                            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.AsuntoDropbox));
                            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareAddress);
                            mContext.startActivity(Intent.createChooser(emailIntent, "Sending email..."));
                        }
                    }*/
                    return true;
                }

            } catch (DropboxUnlinkedException e) {
                // This session wasn't authenticated properly or user unlinked
                mErrorMsg = "This app wasn't authenticated properly.";
            } catch (DropboxFileSizeException e) {
                // File size too big to upload via the API
                mErrorMsg = "This file is too big to upload";
            } catch (DropboxPartialFileException e) {
                // We canceled the operation
                mErrorMsg = "Upload canceled";
            } catch (DropboxServerException e) {
                // Server-side exception.  These are examples of what could happen,
                // but we don't do anything special with them here.
                if (e.error == DropboxServerException._401_UNAUTHORIZED) {
                    // Unauthorized, so we should unlink them.  You may want to
                    // automatically log the user out in this case.
                } else if (e.error == DropboxServerException._403_FORBIDDEN) {
                    // Not allowed to access this
                } else if (e.error == DropboxServerException._404_NOT_FOUND) {
                    // path not found (or if it was the thumbnail, can't be
                    // thumbnailed)
                } else if (e.error == DropboxServerException._507_INSUFFICIENT_STORAGE) {
                    // user is over quota
                } else {
                    // Something else
                }
                // This gets the Dropbox error, translated into the user's language
                mErrorMsg = e.body.userError;
                if (mErrorMsg == null) {
                    mErrorMsg = e.body.error;
                }
            } catch (DropboxIOException e) {
                // Happens all the time, probably want to retry automatically.
                mErrorMsg = "Network error.  Try again.";
            } catch (DropboxParseException e) {
                // Probably due to Dropbox server restarting, should retry
                mErrorMsg = "Dropbox error.  Try again.";
            } catch (DropboxException e) {
                // Unknown error
                mErrorMsg = "Unknown error.  Try again.";
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
                mErrorMsg = "Error I/O";
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Long... progress) {
            int percent = (int)(100.0*(double)progress[0]/mFileLen + 0.5);
            mDialog.setProgress(percent);
        }

        @Override
        protected void onPreExecute() {
            mDialog.setMax(100);
            mDialog.setMessage(mContext.getResources().getString(R.string.Subiendo) + mFile.getName());
            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDialog.setProgress(0);
            mDialog.setButton(ProgressDialog.BUTTON_POSITIVE, mContext.getResources().getString(R.string.Cancelar), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // This will cancel the putFile operation
                    mRequest.abort();
                }
            });
            mDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
                if (result) {
                    showToast(mContext.getResources().getString(R.string.Creado));
                } else {
                    showToast(mErrorMsg);
                }
            }
        }

        private void showToast(String msg) {
            Toast error = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
            error.show();
        }

        String getShareURL(String strURL) {
            URLConnection conn = null;
            String redirectedUrl = null;
            try {
                URL inputURL = new URL(strURL);
                conn = inputURL.openConnection();
                conn.connect();

                InputStream is = conn.getInputStream();
                System.out.println("Redirected URL: " + conn.getURL());
                redirectedUrl = conn.getURL().toString();
                is.close();

            } catch (MalformedURLException e) {
                //Log.d(TAG, "Please input a valid URL");
            } catch (IOException ioe) {
                //Log.d(TAG, "Can not connect to the URL");
            }

            return redirectedUrl;
        }
    }

