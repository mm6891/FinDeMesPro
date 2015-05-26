package globalsolutions.findemes.pantallas.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import globalsolutions.findemes.R;

/**
 * Created by manuel.molero on 17/04/2015.
 */
public class Util {
    public static void showToast(Context c, String message) {
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }

    public static int[] prgmImagesOption = {R.drawable.edit, R.drawable.delete};
    public static int[] prgmImagesCharts = {R.drawable.linechart, R.drawable.barchart};

    /**
     * Creates the specified <code>toFile</code> as a byte for byte copy of the
     * <code>fromFile</code>. If <code>toFile</code> already exists, then it
     * will be replaced with a copy of <code>fromFile</code>. The name and path
     * of <code>toFile</code> will be that of <code>toFile</code>.<br/>
     * <br/>
     * <i> Note: <code>fromFile</code> and <code>toFile</code> will be closed by
     * this function.</i>
     *
     * @param fromFile - FileInputStream for the file to copy from.
     * @param toFile   - FileInputStream for the file to copy to.
     */
    public static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }

    //devuelve positivo si f2 es mayor a f1
    public static int compare(String f1, String f2) {
        try {
            return formatoFechaActual().parse(f2).compareTo(formatoFechaActual().parse(f1));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String sumaDias (String fecha, int numDias){
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(formatoFechaActual().parse(fecha));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, numDias); // Adding numDias days
        String output = formatoFechaActual().format(c.getTime());
        return output;
    }

    public static String fechaActual(){

        Calendar c = Calendar.getInstance();
        String output = formatoFechaActual().format(c.getTime());
        return output;
    }

    public static SimpleDateFormat formatoFechaActual(){
        return new SimpleDateFormat("dd/MM/yyyy kk:mm");
    }

    public static SimpleDateFormat formatoFechaSemanal(){
        return new SimpleDateFormat("dd/MM/yyyy");
    }

    public static String formatoMoneda(Context context){
        String moneda = "";
        SharedPreferences prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String formatoActual = prefs.getString("moneda", "");
        if(formatoActual != null && !formatoActual.isEmpty()) {
            moneda = formatoActual;
        }
        else {
            moneda = "\u20ac";
        }

        return moneda;
    }

  /*  public static SimpleDateFormat formatoFechaUsuario(Context context){
        SimpleDateFormat sdf = formatoFechaActual();

        SharedPreferences prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String formatoActual = prefs.getString("formato", "");

        if(formatoActual != null && !formatoActual.isEmpty()) {
            sdf = new SimpleDateFormat(formatoActual);
        }

        return sdf;
    }*/

   /* public static Date formateaFecha(String fecha){
        *//*SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
            Date d = sdf.parse(oldDateString);
            sdf.applyPattern(NEW_FORMAT);
            newDateString = sdf.format(d);*//*

        //se intenta parsear formato por formato
        SimpleDateFormat sdf;
        Date d = new Date();
        try{
            sdf = new SimpleDateFormat("dd/MM/yyyy kk:mm");
            d = sdf.parse(fecha);
            return d;
        }
        catch (ParseException pe){
            sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
        }
        try {
            d = sdf.parse(fecha);
            return d;
        }
        catch (ParseException pe){
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }
        try {
            d = sdf.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }*/

}
