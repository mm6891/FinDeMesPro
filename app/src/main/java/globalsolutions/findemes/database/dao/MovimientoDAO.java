package globalsolutions.findemes.database.dao;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import globalsolutions.findemes.R;
import globalsolutions.findemes.database.model.Gasto;
import globalsolutions.findemes.database.model.GrupoGasto;
import globalsolutions.findemes.database.model.GrupoIngreso;
import globalsolutions.findemes.database.model.Ingreso;
import globalsolutions.findemes.database.model.MovimientoItem;
import globalsolutions.findemes.database.model.Registro;
import globalsolutions.findemes.database.util.Constantes;
import globalsolutions.findemes.pantallas.util.Util;

/**
 * Created by manuel.molero on 13/02/2015.
 */
public class MovimientoDAO {

    public ArrayList<MovimientoItem> cargaMovimientos(final Context context){
        MovimientoItem[] movsArray;

        //tratamos registros posibles
        Registro[] registros = new RegistroDAO(context).selectRegistros();
        tratarRegistros(registros,context);

        Gasto[] gastos = new GastoDAO(context).selectGastos();
        Ingreso[] ingresos = new IngresoDAO(context).selectIngresos();

        movsArray = new MovimientoItem[gastos.length + ingresos.length];
        for(int i = 0 ; i < gastos.length ; i++){
            MovimientoItem m = new MovimientoItem();
            m.set_id(gastos[i].get_id());
            m.setValor(gastos[i].getValor());
            m.setDescripcion(gastos[i].getDescripcion());
            m.setFecha(gastos[i].getFecha());
            m.setCategoria(gastos[i].getGrupoGasto().getGrupo());
            m.setTipoMovimiento(context.getResources().getString(R.string.TIPO_MOVIMIENTO_GASTO));
            m.set_idRegistro(gastos[i].get_idRegistro());
            movsArray[i] = m;
        }
        for(int j = 0 ; j < ingresos.length ; j++){
            MovimientoItem m = new MovimientoItem();
            m.set_id(ingresos[j].get_id());
            m.setValor(ingresos[j].getValor());
            m.setDescripcion(ingresos[j].getDescripcion());
            m.setFecha(ingresos[j].getFecha());
            m.setCategoria(ingresos[j].getGrupoIngreso().getGrupo());
            m.setTipoMovimiento(context.getResources().getString(R.string.TIPO_MOVIMIENTO_INGRESO));
            m.set_idRegistro(ingresos[j].get_idRegistro());
            movsArray[gastos.length + j] = m;
        }

        ArrayList<MovimientoItem> movs = new ArrayList(Arrays.asList(movsArray));

        //ordenamos los movimientos por fecha descendente
        Collections.sort(movs, new Comparator<MovimientoItem>() {
            @Override
            public int compare(MovimientoItem o1, MovimientoItem o2) {
                try {
                    return Util.formatoFechaActual().parse(o2.getFecha()).compareTo
                            (Util.formatoFechaActual().parse(o1.getFecha()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });

        return movs;
    }

    public ArrayList<MovimientoItem> cargaMovimientosByRegistro(final Context context, int registroID){
        MovimientoItem[] movsArray;
        Gasto[] gastos = new GastoDAO(context).selectGastosByRegistroID(registroID);
        Ingreso[] ingresos = new IngresoDAO(context).selectIngresosByRegistroID(registroID);

        movsArray = new MovimientoItem[gastos.length + ingresos.length];
        for(int i = 0 ; i < gastos.length ; i++){
            MovimientoItem m = new MovimientoItem();
            m.set_id(gastos[i].get_id());
            m.setValor(gastos[i].getValor());
            m.setDescripcion(gastos[i].getDescripcion());
            m.setFecha(gastos[i].getFecha());
            m.setCategoria(gastos[i].getGrupoGasto().getGrupo());
            m.setTipoMovimiento(context.getResources().getString(R.string.TIPO_MOVIMIENTO_GASTO));
            movsArray[i] = m;
        }
        for(int j = 0 ; j < ingresos.length ; j++){
            MovimientoItem m = new MovimientoItem();
            m.set_id(ingresos[j].get_id());
            m.setValor(ingresos[j].getValor());
            m.setDescripcion(ingresos[j].getDescripcion());
            m.setFecha(ingresos[j].getFecha());
            m.setCategoria(ingresos[j].getGrupoIngreso().getGrupo());
            m.setTipoMovimiento(context.getResources().getString(R.string.TIPO_MOVIMIENTO_INGRESO));
            movsArray[gastos.length + j] = m;
        }

        ArrayList<MovimientoItem> movs = new ArrayList(Arrays.asList(movsArray));

        //ordenamos los movimientos por fecha descendente
        Collections.sort(movs, new Comparator<MovimientoItem>() {
            @Override
            public int compare(MovimientoItem o1, MovimientoItem o2) {
                try {
                    return Util.formatoFechaActual().parse(o2.getFecha()).compareTo
                            (Util.formatoFechaActual().parse(o1.getFecha()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });

        return movs;
    }

    private void tratarRegistros(Registro[] registros, Context context){
        //ArrayList<MovimientoItem> ret = new ArrayList<MovimientoItem>();

        for(int i = 0 ; i < registros.length ; i++) {
            if (registros[i].getActivo().equals(Integer.valueOf(Constantes.REGISTRO_ACTIVO.toString()))) {
                //ret = cargaMovimientosByRegistro(context,registros[i].get_id());
                SharedPreferences prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                String fechaActualRegistro = prefs.getString("idRegistro" + String.valueOf(registros[i].get_id()),null);
                if(fechaActualRegistro != null) {
                    String fechaActivacion = fechaActualRegistro;
                    registros[i].setFecha(fechaActivacion);
                    actualizaMovimientos(registros[i], context);
                }
            }
        }
    }

    public void creaMovimientos(Registro nuevoRegistro, Context context){
        if (nuevoRegistro.getActivo().equals(Integer.valueOf(Constantes.REGISTRO_ACTIVO.toString()))) {
            String fechaActivacion = nuevoRegistro.getFecha();
            String fechaActual = Util.fechaActual();
            String fechaActivacionUpdate = null;
            if (nuevoRegistro.getPeriodicidad().equals(context.getResources().getString(R.string.PERIODICIDAD_REGISTRO_DIARIO))){
                //1 movimiento diario a partir de la fecha de activacion del registro
                //mientras que la fecha actual sea mayor que la fecha de activacion
                while(Util.compare(fechaActivacion,fechaActual) > 0){
                    if (nuevoRegistro.getTipo().equals(context.getResources().getString(R.string.TIPO_MOVIMIENTO_GASTO))) {
                        long records = new GastoDAO(context).createRecords(creaGasto(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    else {
                        long records = new IngresoDAO(context).createRecords(creaIngreso(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    //actualizamos fecha de activacion
                    fechaActivacion = Util.sumaDias(fechaActivacion,1);
                }
            }
            else if (nuevoRegistro.getPeriodicidad().equals(context.getResources().getString(R.string.PERIODICIDAD_REGISTRO_SEMANAL))){
                //1 movimiento semanal a partir de la fecha de activacion del registro
                //mientras que la fecha actual sea mayor que la fecha de activacion
                while(Util.compare(fechaActivacion,fechaActual) > 0){
                    if (nuevoRegistro.getTipo().equals(context.getResources().getString(R.string.TIPO_MOVIMIENTO_GASTO))) {
                        long records = new GastoDAO(context).createRecords(creaGasto(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    else {
                        long records = new IngresoDAO(context).createRecords(creaIngreso(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    //actualizamos fecha de activacion
                    fechaActivacion = Util.sumaDias(fechaActivacion,7);
                }
            }
            else if (nuevoRegistro.getPeriodicidad().equals(context.getResources().getString(R.string.PERIODICIDAD_REGISTRO_QUINCENAL))){
                //1 movimiento semanal a partir de la fecha de activacion del registro
                //mientras que la fecha actual sea mayor que la fecha de activacion
                while(Util.compare(fechaActivacion,fechaActual) > 0){
                    if (nuevoRegistro.getTipo().equals(context.getResources().getString(R.string.TIPO_MOVIMIENTO_GASTO))) {
                        long records = new GastoDAO(context).createRecords(creaGasto(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    else {
                        long records = new IngresoDAO(context).createRecords(creaIngreso(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    //actualizamos fecha de activacion
                    fechaActivacion = Util.sumaDias(fechaActivacion,15);
                }
            }
            else if (nuevoRegistro.getPeriodicidad().equals(context.getResources().getString(R.string.PERIODICIDAD_REGISTRO_MENSUAL))){
                //1 movimiento mensual a partir de la fecha de activacion del registro
                while(Util.compare(fechaActivacion,fechaActual) > 0){
                    if (nuevoRegistro.getTipo().equals(context.getResources().getString(R.string.TIPO_MOVIMIENTO_GASTO))) {
                        long records = new GastoDAO(context).createRecords(creaGasto(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    else {
                        long records = new IngresoDAO(context).createRecords(creaIngreso(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    //actualizamos fecha de activacion
                    fechaActivacion = Util.sumaDias(fechaActivacion,30);
                }
            }
            else if (nuevoRegistro.getPeriodicidad().equals(context.getResources().getString(R.string.PERIODICIDAD_REGISTRO_TRIMESTRAL))){
                //1 movimiento mensual a partir de la fecha de activacion del registro
                while(Util.compare(fechaActivacion,fechaActual) > 0){
                    if (nuevoRegistro.getTipo().equals(context.getResources().getString(R.string.TIPO_MOVIMIENTO_GASTO))) {
                        long records = new GastoDAO(context).createRecords(creaGasto(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    else {
                        long records = new IngresoDAO(context).createRecords(creaIngreso(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    //actualizamos fecha de activacion
                    fechaActivacion = Util.sumaDias(fechaActivacion,90);
                }
            }
            else if (nuevoRegistro.getPeriodicidad().equals(context.getResources().getString(R.string.PERIODICIDAD_REGISTRO_ANUAL))){
                //1 movimiento anual a partir de la fecha de activacion del registro
                while(Util.compare(fechaActivacion,fechaActual) > 0){
                    if (nuevoRegistro.getTipo().equals(context.getResources().getString(R.string.TIPO_MOVIMIENTO_GASTO))) {
                        long records = new GastoDAO(context).createRecords(creaGasto(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    else {
                        long records = new IngresoDAO(context).createRecords(creaIngreso(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    //actualizamos fecha de activacion
                    fechaActivacion = Util.sumaDias(fechaActivacion,365);
                }
            }
            if(fechaActivacionUpdate != null) {
                //actualizamos ultima fecha de movimiento insertado para el registro tratado
                SharedPreferences prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                edit.remove("idRegistro" + String.valueOf(nuevoRegistro.get_id()));
                edit.putString("idRegistro" + String.valueOf(nuevoRegistro.get_id()), fechaActivacionUpdate);
                edit.commit();
            }
        }
    }

    public void actualizaMovimientos(Registro nuevoRegistro, Context context){
        if (nuevoRegistro.getActivo().equals(Integer.valueOf(Constantes.REGISTRO_ACTIVO.toString()))) {
            String fechaActual = Util.fechaActual();
            String fechaActivacionUpdate = null;
            if (nuevoRegistro.getPeriodicidad().equals(context.getResources().getString(R.string.PERIODICIDAD_REGISTRO_DIARIO))){
                String fechaActivacion = Util.sumaDias(nuevoRegistro.getFecha(),1);
                //1 movimiento diario a partir de la fecha de activacion del registro
                //mientras que la fecha actual sea mayor que la fecha de activacion
                while(Util.compare(fechaActivacion,fechaActual) > 0){
                    if (nuevoRegistro.getTipo().equals(context.getResources().getString(R.string.TIPO_MOVIMIENTO_GASTO))) {
                        long records = new GastoDAO(context).createRecords(creaGasto(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    else {
                        long records = new IngresoDAO(context).createRecords(creaIngreso(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    //actualizamos fecha de activacion
                    fechaActivacion = Util.sumaDias(fechaActivacion,1);
                }
            }
            else if (nuevoRegistro.getPeriodicidad().equals(context.getResources().getString(R.string.PERIODICIDAD_REGISTRO_SEMANAL))){
                String fechaActivacion = Util.sumaDias(nuevoRegistro.getFecha(),7);
                //1 movimiento semanal a partir de la fecha de activacion del registro
                //mientras que la fecha actual sea mayor que la fecha de activacion
                while(Util.compare(fechaActivacion,fechaActual) > 0){
                    if (nuevoRegistro.getTipo().equals(context.getResources().getString(R.string.TIPO_MOVIMIENTO_GASTO))) {
                        long records = new GastoDAO(context).createRecords(creaGasto(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    else {
                        long records = new IngresoDAO(context).createRecords(creaIngreso(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    //actualizamos fecha de activacion
                    fechaActivacion = Util.sumaDias(fechaActivacion,7);
                }
            }
            else if (nuevoRegistro.getPeriodicidad().equals(context.getResources().getString(R.string.PERIODICIDAD_REGISTRO_QUINCENAL))){
                String fechaActivacion = Util.sumaDias(nuevoRegistro.getFecha(),15);
                //1 movimiento semanal a partir de la fecha de activacion del registro
                //mientras que la fecha actual sea mayor que la fecha de activacion
                while(Util.compare(fechaActivacion,fechaActual) > 0){
                    if (nuevoRegistro.getTipo().equals(context.getResources().getString(R.string.TIPO_MOVIMIENTO_GASTO))) {
                        long records = new GastoDAO(context).createRecords(creaGasto(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    else {
                        long records = new IngresoDAO(context).createRecords(creaIngreso(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    //actualizamos fecha de activacion
                    fechaActivacion = Util.sumaDias(fechaActivacion,15);
                }
            }
            else if (nuevoRegistro.getPeriodicidad().equals(context.getResources().getString(R.string.PERIODICIDAD_REGISTRO_MENSUAL))){
                String fechaActivacion = Util.sumaDias(nuevoRegistro.getFecha(),30);
                //1 movimiento mensual a partir de la fecha de activacion del registro
                while(Util.compare(fechaActivacion,fechaActual) > 0){
                    if (nuevoRegistro.getTipo().equals(context.getResources().getString(R.string.TIPO_MOVIMIENTO_GASTO))) {
                        long records = new GastoDAO(context).createRecords(creaGasto(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    else {
                        long records = new IngresoDAO(context).createRecords(creaIngreso(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    //actualizamos fecha de activacion
                    fechaActivacion = Util.sumaDias(fechaActivacion,30);
                }
            }
            else if (nuevoRegistro.getPeriodicidad().equals(context.getResources().getString(R.string.PERIODICIDAD_REGISTRO_TRIMESTRAL))){
                String fechaActivacion = Util.sumaDias(nuevoRegistro.getFecha(),90);
                //1 movimiento mensual a partir de la fecha de activacion del registro
                while(Util.compare(fechaActivacion,fechaActual) > 0){
                    if (nuevoRegistro.getTipo().equals(context.getResources().getString(R.string.TIPO_MOVIMIENTO_GASTO))) {
                        long records = new GastoDAO(context).createRecords(creaGasto(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    else {
                        long records = new IngresoDAO(context).createRecords(creaIngreso(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    //actualizamos fecha de activacion
                    fechaActivacion = Util.sumaDias(fechaActivacion,90);
                }
            }
            else if (nuevoRegistro.getPeriodicidad().equals(context.getResources().getString(R.string.PERIODICIDAD_REGISTRO_ANUAL))){
                String fechaActivacion = Util.sumaDias(nuevoRegistro.getFecha(),365);
                //1 movimiento anual a partir de la fecha de activacion del registro
                while(Util.compare(fechaActivacion,fechaActual) > 0){
                    if (nuevoRegistro.getTipo().equals(context.getResources().getString(R.string.TIPO_MOVIMIENTO_GASTO))) {
                        long records = new GastoDAO(context).createRecords(creaGasto(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    else {
                        long records = new IngresoDAO(context).createRecords(creaIngreso(nuevoRegistro, fechaActivacion));
                        fechaActivacionUpdate = records > 0 ? fechaActivacion : fechaActivacionUpdate;
                    }
                    //actualizamos fecha de activacion
                    fechaActivacion = Util.sumaDias(fechaActivacion,365);
                }
            }
            if(fechaActivacionUpdate != null) {
                //actualizamos ultima fecha de movimiento insertado para el registro tratado
                SharedPreferences prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                edit.remove("idRegistro" + String.valueOf(nuevoRegistro.get_id()));
                edit.putString("idRegistro" + String.valueOf(nuevoRegistro.get_id()), fechaActivacionUpdate);
                edit.commit();
            }
        }
    }

    private Ingreso creaIngreso(Registro registro, String fechaActivacion){
        Ingreso ingreso = new Ingreso();
        ingreso.set_id(registro.get_id());
        ingreso.setValor(registro.getValor());
        ingreso.setDescripcion(registro.getDescripcion());
        //tratamos el caso especial de la fecha
        ingreso.setFecha(fechaActivacion);
        GrupoIngreso g = new GrupoIngreso();
        g.setGrupo(registro.getGrupo());
        ingreso.setGrupoIngreso(g);
        ingreso.set_idRegistro(registro.get_id());
        return ingreso;
    }
    private Gasto creaGasto(Registro registro, String fechaActivacion){
        Gasto gasto = new Gasto();
        gasto.set_id(registro.get_id());
        gasto.setValor(registro.getValor());
        gasto.setDescripcion(registro.getDescripcion());
        //tratamos el caso especial de la fecha
        gasto.setFecha(fechaActivacion);
        GrupoGasto g = new GrupoGasto();
        g.setGrupo(registro.getGrupo());
        gasto.setGrupoGasto(g);
        gasto.set_idRegistro(registro.get_id());
        return gasto;
    }
}
