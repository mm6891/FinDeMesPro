package globalsolutions.findemes.pantallas.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import globalsolutions.findemes.R;
import globalsolutions.findemes.database.dao.MovimientoDAO;
import globalsolutions.findemes.database.model.InformeItem;
import globalsolutions.findemes.database.model.MovimientoItem;
import globalsolutions.findemes.database.util.Constantes;
import globalsolutions.findemes.pantallas.util.Util;

/**
 * Created by manuel.molero on 04/02/2015.
 */

public class InformeAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private ArrayList<InformeItem> items;
    private ArrayList<InformeItem> itemsFiltrado;
    private ItemFilter mFilter = new ItemFilter();

    //mes, array de movimientos
    private HashMap<Integer,ArrayList<MovimientoItem>> informes = new HashMap<Integer,ArrayList<MovimientoItem>>();

    public InformeAdapter(Context context, ArrayList<InformeItem> items) {
        this.context = context;
        this.items = items;
        this.itemsFiltrado = items;
    }

    @Override
    public int getCount() {
        return this.itemsFiltrado.size();
    }

    public ArrayList<InformeItem> getItemsActuales(){
        return this.itemsFiltrado;
    }

    @Override
    public Object getItem(int position) {
        return this.itemsFiltrado.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        // Create a new view into the list.
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //establer el item segun el tipo de movimiento
        InformeItem item = this.itemsFiltrado.get(position);

        if(item.getTipoInforme().equals(context.getResources().getString(R.string.TIPO_FILTRO_RESETEO))){
            rowView = inflater.inflate(R.layout.informe_item, parent, false);
            // Set data into the view.
            TextView tvPeriodo = (TextView) rowView.findViewById(R.id.tvPeriodoInforme);
            TextView tvIngresoValor = (TextView) rowView.findViewById(R.id.tvIngresosInformeValor);
            TextView tvGastoValor = (TextView) rowView.findViewById(R.id.tvGastosInformeValor);
            TextView tvTotalValor = (TextView) rowView.findViewById(R.id.tvTotalInformeValor);

            tvPeriodo.setText(item.getPeriodoDesc());
            tvIngresoValor.setText(item.getIngresoValor() + Util.formatoMoneda(context));
            tvGastoValor.setText(item.getGastoValor() + Util.formatoMoneda(context));
            tvTotalValor.setText(item.getTotalValor() + Util.formatoMoneda(context));
        }
        else if(item.getTipoInforme().equals(context.getResources().getString(R.string.TIPO_MOVIMIENTO_GASTO))){
            rowView = inflater.inflate(R.layout.informe_item_gasto, parent, false);
            // Set data into the view.
            TextView tvPeriodo = (TextView) rowView.findViewById(R.id.tvPeriodoInforme);
            TextView tvGastoValor = (TextView) rowView.findViewById(R.id.tvGastosInformeValor);

            tvPeriodo.setText(item.getPeriodoDesc());
            tvGastoValor.setText(item.getGastoValor() + Util.formatoMoneda(context));
        }
        if(item.getTipoInforme().equals(context.getResources().getString(R.string.TIPO_MOVIMIENTO_INGRESO))){
            rowView = inflater.inflate(R.layout.informe_item_ingreso, parent, false);
            // Set data into the view.
            TextView tvPeriodo = (TextView) rowView.findViewById(R.id.tvPeriodoInforme);
            TextView tvIngresoValor = (TextView) rowView.findViewById(R.id.tvIngresosInformeValor);

            tvPeriodo.setText(item.getPeriodoDesc());
            tvIngresoValor.setText(item.getIngresoValor() + Util.formatoMoneda(context));
        }

        if(position % 2 == 0)
            rowView.setBackgroundColor(context.getResources().getColor(R.color.button_material_light));

        return rowView;
    }

    public void updateReceiptsList(ArrayList<InformeItem> newlist) {
        itemsFiltrado.clear();
        itemsFiltrado.addAll(newlist);
        this.notifyDataSetChanged();
    }

    public interface OnDataChangeListener{
        public void onDataChanged(ArrayList<InformeItem> informes);
    }

    OnDataChangeListener mOnDataChangeListener;
    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        mOnDataChangeListener = onDataChangeListener;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            String[] filtersString = constraint.toString().split(";");
            String tipoMovimiento = filtersString[0];
            String periodo = filtersString[1];
            String periodoFiltro = filtersString[2];

            //recuperamos movimientos
            final ArrayList<MovimientoItem> movs = new MovimientoDAO().cargaMovimientos(context);
            informes.clear();

            int anyoActual = new Integer(periodoFiltro).intValue();

            for(int i = 0 ; i < movs.size() ; i++) {
                String fecha = movs.get(i).getFecha();
                Calendar cal = Calendar.getInstance();
                try {
                    cal.setTime(Util.formatoFechaActual().parse(fecha));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                int anyoMovimiento = cal.get(Calendar.YEAR);

                if (anyoMovimiento == anyoActual) {
                    int periodoMovimiento = Integer.MIN_VALUE;
                    //diario
                    if(periodo.equals(context.getResources().getString(R.string.TIPO_FILTRO_INFORME_DIARIO))) {
                        periodoMovimiento = cal.get(Calendar.DAY_OF_YEAR);
                    }
                    //semanal
                    else if(periodo.equals(context.getResources().getString(R.string.TIPO_FILTRO_INFORME_SEMANAL))) {
                        periodoMovimiento = cal.get(Calendar.WEEK_OF_YEAR);
                    }
                    //quincenal
                    else if(periodo.equals(context.getResources().getString(R.string.TIPO_FILTRO_INFORME_QUINCENAL))){
                        int dia = cal.get(Calendar.DATE);
                        int mes = cal.get(Calendar.MONTH);
                        mes = (mes + 1) * 2;
                        periodoMovimiento = (dia < 16) ? mes - 1 : mes;
                    }
                    //mensual
                    else if(periodo.equals(context.getResources().getString(R.string.TIPO_FILTRO_INFORME_MENSUAL))) {
                        periodoMovimiento = cal.get(Calendar.MONTH);
                    }
                    //trimestral
                    else if(periodo.equals(context.getResources().getString(R.string.TIPO_FILTRO_INFORME_TRIMESTRAL))){
                        periodoMovimiento = (cal.get(Calendar.MONTH) / 3) + 1;
                    }
                    //anual
                    else if(periodo.equals(context.getResources().getString(R.string.TIPO_FILTRO_INFORME_ANUAL))) {
                        periodoMovimiento = anyoMovimiento;
                    }

                    boolean existePeriodoInforme = existePeriodoInforme(periodoMovimiento);
                    if(!existePeriodoInforme){
                        nuevoMesInforme(periodoMovimiento, movs.get(i));
                    }
                    else{
                        actualizaMesInforme(periodoMovimiento, movs.get(i));
                    }
                }
            }

            ArrayList<InformeItem> resultado = calculaInformes(tipoMovimiento,periodo,anyoActual);
            results.values = resultado;
            results.count = resultado.size();

            if(mOnDataChangeListener != null){
                mOnDataChangeListener.onDataChanged(resultado);
            }

            return results;
        }

        //tipoPeriodo:  MENSUAL, TRIMESTRAL, QUINCENAL
        private ArrayList<InformeItem> calculaInformes(String tipoMovimiento, String tipoPeriodo, int anyoActual){
            ArrayList<InformeItem> result = new ArrayList<InformeItem>(informes.size());
            Map<Integer,ArrayList<MovimientoItem>> treeMap = new TreeMap<Integer,ArrayList<MovimientoItem>>(informes);
            for(Integer integer : treeMap.keySet()){
                ArrayList<MovimientoItem> movsMes = informes.get(integer);

                Double ingresos = new Double(0.00);
                Double gastos = new Double(0.00);
                Double saldo = new Double(0.00);

                for(MovimientoItem mov : movsMes){
                    if (mov.getTipoMovimiento().equals(context.getResources().getString(R.string.TIPO_MOVIMIENTO_GASTO)))
                        gastos += Double.valueOf(mov.getValor());
                    else if (mov.getTipoMovimiento().equals(context.getResources().getString(R.string.TIPO_MOVIMIENTO_INGRESO)))
                        ingresos += Double.valueOf(mov.getValor());
                }

                //si es un gasto o ingreso sin valor, no se incluye en la lista
                if(tipoMovimiento.equals(context.getResources().getString(R.string.TIPO_MOVIMIENTO_GASTO)) &&
                        gastos.equals(new Double(0.00)))
                    continue;
                if(tipoMovimiento.equals(context.getResources().getString(R.string.TIPO_MOVIMIENTO_INGRESO)) &&
                        ingresos.equals(new Double(0.00)))
                    continue;

                InformeItem nuevoInforme = new InformeItem();
                nuevoInforme.setTipoInforme(tipoMovimiento);
                nuevoInforme.setMovimientos(movsMes);

                nuevoInforme.setGastoValor(String.valueOf(gastos));
                nuevoInforme.setIngresoValor(String.valueOf(ingresos));
                saldo = ingresos - gastos;
                nuevoInforme.setTotalValor(String.valueOf(saldo));

                if(tipoPeriodo.equals(context.getResources().getString(R.string.TIPO_FILTRO_INFORME_DIARIO))) {
                    nuevoInforme.setPeriodoDesc(movsMes.get(0).getFecha());
                }
                if(tipoPeriodo.equals(context.getResources().getString(R.string.TIPO_FILTRO_INFORME_SEMANAL))) {
                    SimpleDateFormat sdf = Util.formatoFechaSemanal();
                    int week = integer.intValue();
                    int anyo = anyoActual;

                    Calendar cld = Calendar.getInstance();
                    cld.clear();
                    cld.set(Calendar.YEAR, anyo);
                    cld.set(Calendar.WEEK_OF_YEAR, week);
                    cld.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    Date dateres = cld.getTime();
                    String f1 = sdf.format(dateres);

                    Calendar cld2 = Calendar.getInstance();
                    cld2.clear();
                    cld2.set(Calendar.YEAR, anyo);
                    int week2 = week + 1;
                    cld2.set(Calendar.WEEK_OF_YEAR, week2);
                    cld2.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    Date dateres2 = cld2.getTime();
                    String f2 = sdf.format(dateres2);
                    nuevoInforme.setPeriodoDesc(f1 + " " + context.getResources().getString(R.string.ConjuncionSemana) + " " + f2);
                }
                else if(tipoPeriodo.equals(context.getResources().getString(R.string.TIPO_FILTRO_INFORME_QUINCENAL))) {
                    String periodoQuincenal;
                    //quincenas
                    switch (integer.intValue()){
                        case 1:
                            periodoQuincenal = "1 " + new DateFormatSymbols().getMonths()[0] + "-"
                                    + "15 " + new DateFormatSymbols().getMonths()[0];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 2:
                            periodoQuincenal = "16 " + new DateFormatSymbols().getMonths()[0] + "-"
                                    + "31 " + new DateFormatSymbols().getMonths()[0];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 3:
                            periodoQuincenal = "1 " + new DateFormatSymbols().getMonths()[1] + "-"
                                    + "15 " + new DateFormatSymbols().getMonths()[1];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 4:
                            periodoQuincenal = "16 " + new DateFormatSymbols().getMonths()[1] + "-"
                                    + "28 " + new DateFormatSymbols().getMonths()[1];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 5:
                            periodoQuincenal = "1 " + new DateFormatSymbols().getMonths()[2] + "-"
                                    + "15 " + new DateFormatSymbols().getMonths()[2];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 6:
                            periodoQuincenal = "16 " + new DateFormatSymbols().getMonths()[2] + "-"
                                    + "31 " + new DateFormatSymbols().getMonths()[2];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 7:
                            periodoQuincenal = "1 " + new DateFormatSymbols().getMonths()[3] + "-"
                                    + "15 " + new DateFormatSymbols().getMonths()[3];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 8:
                            periodoQuincenal = "16 " + new DateFormatSymbols().getMonths()[3] + "-"
                                    + "30 " + new DateFormatSymbols().getMonths()[3];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 9:
                            periodoQuincenal = "1 " + new DateFormatSymbols().getMonths()[4] + "-"
                                    + "15 " + new DateFormatSymbols().getMonths()[4];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 10:
                            periodoQuincenal = "16 " + new DateFormatSymbols().getMonths()[4] + "-"
                                    + "31 " + new DateFormatSymbols().getMonths()[4];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 11:
                            periodoQuincenal = "1 " + new DateFormatSymbols().getMonths()[5] + "-"
                                    + "15 " + new DateFormatSymbols().getMonths()[5];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 12:
                            periodoQuincenal = "16 " + new DateFormatSymbols().getMonths()[5] + "-"
                                    + "30 " + new DateFormatSymbols().getMonths()[5];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 13:
                            periodoQuincenal = "1 " + new DateFormatSymbols().getMonths()[6] + "-"
                                    + "15 " + new DateFormatSymbols().getMonths()[6];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 14:
                            periodoQuincenal = "16 " + new DateFormatSymbols().getMonths()[6] + "-"
                                    + "31 " + new DateFormatSymbols().getMonths()[6];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 15:
                            periodoQuincenal = "1 " + new DateFormatSymbols().getMonths()[7] + "-"
                                    + "15 " + new DateFormatSymbols().getMonths()[7];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 16:
                            periodoQuincenal = "16 " + new DateFormatSymbols().getMonths()[7] + "-"
                                    + "31 " + new DateFormatSymbols().getMonths()[7];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 17:
                            periodoQuincenal = "1 " + new DateFormatSymbols().getMonths()[8] + "-"
                                    + "15 " + new DateFormatSymbols().getMonths()[8];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 18:
                            periodoQuincenal = "16 " + new DateFormatSymbols().getMonths()[8] + "-"
                                    + "30 " + new DateFormatSymbols().getMonths()[8];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 19:
                            periodoQuincenal = "1 " + new DateFormatSymbols().getMonths()[9] + "-"
                                    + "15 " + new DateFormatSymbols().getMonths()[9];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 20:
                            periodoQuincenal = "16 " + new DateFormatSymbols().getMonths()[9] + "-"
                                    + "31 " + new DateFormatSymbols().getMonths()[9];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 21:
                            periodoQuincenal = "1 " + new DateFormatSymbols().getMonths()[10] + "-"
                                    + "15 " + new DateFormatSymbols().getMonths()[10];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 22:
                            periodoQuincenal = "16 " + new DateFormatSymbols().getMonths()[10] + "-"
                                    + "30 " + new DateFormatSymbols().getMonths()[10];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 23:
                            periodoQuincenal = "1 " + new DateFormatSymbols().getMonths()[11] + "-"
                                    + "15 " + new DateFormatSymbols().getMonths()[11];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                        case 24:
                            periodoQuincenal = "16 " + new DateFormatSymbols().getMonths()[11] + "-"
                                    + "31 " + new DateFormatSymbols().getMonths()[11];
                            nuevoInforme.setPeriodoDesc(periodoQuincenal);
                            break;
                    }
                }
                else if(tipoPeriodo.equals(context.getResources().getString(R.string.TIPO_FILTRO_INFORME_MENSUAL))) {
                    nuevoInforme.setPeriodoDesc(new DateFormatSymbols().getMonths()[integer.intValue()].toUpperCase());
                }
                else if(tipoPeriodo.equals(context.getResources().getString(R.string.TIPO_FILTRO_INFORME_TRIMESTRAL))) {
                    String periodoTrimestral;
                    //trimestres
                    switch (integer.intValue()){
                        case 1:
                            periodoTrimestral = "1 " + new DateFormatSymbols().getMonths()[0] + "-"
                                    + "31 " + new DateFormatSymbols().getMonths()[2];
                            nuevoInforme.setPeriodoDesc(periodoTrimestral);
                            break;
                        case 2:
                            periodoTrimestral = "1 " + new DateFormatSymbols().getMonths()[3] + "-"
                                    + "30 " + new DateFormatSymbols().getMonths()[5];
                            nuevoInforme.setPeriodoDesc(periodoTrimestral);
                            break;
                        case 3:
                            periodoTrimestral = "1 " + new DateFormatSymbols().getMonths()[6] + "-"
                                    + "31 " + new DateFormatSymbols().getMonths()[8];
                            nuevoInforme.setPeriodoDesc(periodoTrimestral);
                            break;
                        case 4:
                            periodoTrimestral = "1 " + new DateFormatSymbols().getMonths()[9] + "-"
                                    + "31 " + new DateFormatSymbols().getMonths()[11];
                            nuevoInforme.setPeriodoDesc(periodoTrimestral);
                            break;
                    }
                }
                else if(tipoPeriodo.equals(context.getResources().getString(R.string.TIPO_FILTRO_INFORME_ANUAL))) {
                    nuevoInforme.setPeriodoDesc(integer.toString());
                }

                result.add(nuevoInforme);
            }
            return result;
        }

        public boolean existePeriodoInforme(int key){
            return informes.containsKey(new Integer(key));
        }

        public void actualizaMesInforme(int key, MovimientoItem mov){
            informes.get(key).add(mov);
        }

        public void nuevoMesInforme(int key,MovimientoItem nuevo){
            ArrayList<MovimientoItem> nuevoArray = new ArrayList<MovimientoItem>();
            nuevoArray.add(nuevo);
            informes.put(key, nuevoArray);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            updateReceiptsList((ArrayList<InformeItem>) results.values);
        }

        }
}
