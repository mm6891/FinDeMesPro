package globalsolutions.findemes.pantallas.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.shapes.Shape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import globalsolutions.findemes.R;
import globalsolutions.findemes.database.dao.MovimientoDAO;
import globalsolutions.findemes.database.model.MovimientoItem;
import globalsolutions.findemes.database.util.Constantes;
import globalsolutions.findemes.pantallas.util.Util;

/**
 * Created by manuel.molero on 04/02/2015.
 */

public class MovimientoAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private ArrayList<MovimientoItem> items;
    private ArrayList<MovimientoItem> itemsFiltrado;
    private ItemFilter mFilter = new ItemFilter();

    private int mesSeleccionado;
    public int getMesSeleccionado(){
        return mesSeleccionado;
    }
    public void setMesSeleccionado(int mesSeleccionado){
        this.mesSeleccionado = mesSeleccionado;
    }

    private int anyoSeleccionado;
    public int getAnyoSeleccionado() {
        return anyoSeleccionado;
    }
    public void setAnyoSeleccionado(int anyoSeleccionado) {
        this.anyoSeleccionado = anyoSeleccionado;
    }

    public String getCategoriaSeleccionada() {
        return categoriaSeleccionada;
    }
    public void setCategoriaSeleccionada(String categoriaSeleccionada) {
        this.categoriaSeleccionada = categoriaSeleccionada;
    }
    private String categoriaSeleccionada;


    public MovimientoAdapter(Context context, ArrayList<MovimientoItem> items) {
        this.context = context;
        this.items = items;
        this.itemsFiltrado = items;
    }

    @Override
    public int getCount() {
        return this.itemsFiltrado.size();
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

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.movimiento_item, parent, false);
        }

        // Set data into the view.

        TextView tvDescr = (TextView) rowView.findViewById(R.id.tvDescMov);
        TextView tvvalor = (TextView) rowView.findViewById(R.id.tvValorMov);
        TextView tvgrupo = (TextView) rowView.findViewById(R.id.tvGrupoMov);
        TextView tvfecha = (TextView) rowView.findViewById(R.id.tvFechaMov);
        ImageView ivIconMov = (ImageView) rowView.findViewById(R.id.ivIcon);

        MovimientoItem item = this.itemsFiltrado.get(position);
        tvDescr.setText(item.getDescripcion());
        tvvalor.setText(item.getValor() + Util.formatoMoneda(context));
        tvgrupo.setText(item.getCategoria());
        tvfecha.setText(item.getFecha());
        if(item.getTipoMovimiento().trim().equals(context.getResources().getString(R.string.TIPO_MOVIMIENTO_GASTO)))
            ivIconMov.setImageResource(R.drawable.minus);
        if(item.getTipoMovimiento().trim().equals(context.getResources().getString(R.string.TIPO_MOVIMIENTO_INGRESO)))
            ivIconMov.setImageResource(R.drawable.plus);

        if(position % 2 == 0)
            rowView.setBackgroundColor(context.getResources().getColor(R.color.button_material_light));

        return rowView;
    }

    public void updateReceiptsList(ArrayList<MovimientoItem> newlist) {
        itemsFiltrado.clear();
        itemsFiltrado.addAll(newlist);
        this.notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }



    private class ItemFilter extends Filter {

            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();

                String filterableString;
                String filterableEditText = "";
                int mesSeleccionado1 = getMesSeleccionado();
                int anyoSeleccionado = getAnyoSeleccionado();

                items = new MovimientoDAO().cargaMovimientos(context);
                final ArrayList<MovimientoItem> list = items;
                int count = items.size();
                final ArrayList<MovimientoItem> nlist = new ArrayList<MovimientoItem>(count);

                //comprobamos filtro edittext
                if(constraint.toString().contains(";") && constraint.toString().split(";").length > 1) {
                    filterableEditText = constraint.toString().split(";")[1].toLowerCase().trim();
                    constraint = constraint.toString().split(";")[0];
                }
                else if(constraint.toString().contains(";"))
                    constraint = constraint.toString().split(";")[0];

                //gastos + ingresos
                if(constraint.toString().equals(context.getResources().getString(R.string.TIPO_FILTRO_RESETEO))){
                    //filtro mes all y filtro anyo all
                    if(getMesSeleccionado() == Constantes.NUMERO_MES_TODO && getAnyoSeleccionado() == Constantes.NUMERO_ANYO_TODO){
                        if(filterableEditText.isEmpty()) {
                            results.values = items;
                            results.count = items.size();
                            return results;
                        }
                        else{
                            for (int i = 0; i < count; i++) {
                                if (list.get(i).getDescripcion().contains(filterableEditText.toLowerCase().trim())) {
                                    nlist.add(list.get(i));
                                }
                            }
                            results.values = nlist;
                            results.count = nlist.size();
                            return results;
                        }
                    }
                    //filtro mes all y filtro anyo
                    else if(getMesSeleccionado() == Constantes.NUMERO_MES_TODO){
                        for (int i = 0; i < count; i++) {
                            filterableString = list.get(i).getFecha();
                            Calendar cal  = Calendar.getInstance();
                            try {
                                cal.setTime(Util.formatoFechaActual().parse(filterableString));
                            } catch (java.text.ParseException e) {
                                e.printStackTrace();
                            }
                            int anyoMov = cal.get(Calendar.YEAR);
                            if(!filterableEditText.isEmpty()) {
                                if (anyoMov == anyoSeleccionado && list.get(i).getDescripcion().contains(filterableEditText.toLowerCase().trim())) {
                                    nlist.add(list.get(i));
                                }
                            }
                            else{
                                if (anyoMov == anyoSeleccionado) {
                                    nlist.add(list.get(i));
                                }
                            }
                        }
                        results.values = nlist;
                        results.count = nlist.size();
                        return results;
                    }
                    //filtro anyo all y filtro mes
                    else if(getAnyoSeleccionado() == Constantes.NUMERO_ANYO_TODO){
                        for (int i = 0; i < count; i++) {
                            filterableString = list.get(i).getFecha();
                            Calendar cal  = Calendar.getInstance();
                            try {
                                cal.setTime(Util.formatoFechaActual().parse(filterableString));
                            } catch (java.text.ParseException e) {
                                e.printStackTrace();
                            }
                            int mesMov = cal.get(Calendar.MONTH);
                            if(!filterableEditText.isEmpty()) {
                                if (mesMov == mesSeleccionado1 && list.get(i).getDescripcion().contains(filterableEditText.toLowerCase().trim())) {
                                    nlist.add(list.get(i));
                                }
                            }
                            else{
                                if (mesMov == mesSeleccionado1) {
                                    nlist.add(list.get(i));
                                }
                            }
                        }
                        results.values = nlist;
                        results.count = nlist.size();
                        return results;
                    }
                    //filtro por mes y anyo, que son filtros permanentes
                    for (int i = 0; i < count; i++) {
                        filterableString = list.get(i).getFecha();
                        Calendar cal  = Calendar.getInstance();
                        try {
                            cal.setTime(Util.formatoFechaActual().parse(filterableString));
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                        int mesMov = cal.get(Calendar.MONTH);
                        int anyoMov = cal.get(Calendar.YEAR);
                        if(!filterableEditText.isEmpty()) {
                            if (mesMov == mesSeleccionado1 && anyoMov == anyoSeleccionado &&
                                    list.get(i).getDescripcion().contains(filterableEditText.toLowerCase().trim())) {
                                nlist.add(list.get(i));
                            }
                        }
                        else{
                            if (mesMov == mesSeleccionado1 && anyoMov == anyoSeleccionado) {
                                nlist.add(list.get(i));
                            }
                        }
                    }

                    results.values = nlist;
                    results.count = nlist.size();
                }
                //filtro por gasto o ingreso
                else {
                    String filterString = constraint.toString().toLowerCase();

                    //filtro mes all y filtro anyo all
                    if(getMesSeleccionado() == Constantes.NUMERO_MES_TODO && getAnyoSeleccionado() == Constantes.NUMERO_ANYO_TODO){
                        for (int i = 0; i < count; i++) {
                            filterableString = list.get(i).getTipoMovimiento();
                            if(!filterableEditText.isEmpty()) {
                                if (getCategoriaSeleccionada() != null && !getCategoriaSeleccionada().isEmpty()) {
                                    if (filterableString.toLowerCase().trim().equals(filterString.trim())
                                            && list.get(i).getCategoria().toLowerCase().trim().equals(getCategoriaSeleccionada().toLowerCase())
                                            && list.get(i).getDescripcion().toLowerCase().trim().contains(filterableEditText.toLowerCase().trim()))
                                        nlist.add(list.get(i));
                                } else {
                                    if (filterableString.toLowerCase().trim().equals(filterString.trim())
                                            && list.get(i).getDescripcion().toLowerCase().trim().contains(filterableEditText.toLowerCase().trim())) {
                                        nlist.add(list.get(i));
                                    }
                                }
                            }
                            else{
                                if (getCategoriaSeleccionada() != null && !getCategoriaSeleccionada().isEmpty()) {
                                    if (filterableString.toLowerCase().trim().equals(filterString.trim())
                                            && list.get(i).getCategoria().toLowerCase().trim().equals(getCategoriaSeleccionada().toLowerCase()))
                                        nlist.add(list.get(i));
                                } else {
                                    if (filterableString.toLowerCase().trim().equals(filterString.trim())) {
                                        nlist.add(list.get(i));
                                    }
                                }
                            }
                        }
                        results.values = nlist;
                        results.count = nlist.size();
                        return results;
                    }
                    //filtro mes all y filtro anyo
                    else if(getMesSeleccionado() == Constantes.NUMERO_MES_TODO){
                        for (int i = 0; i < count; i++) {
                            filterableString = list.get(i).getFecha();
                            Calendar cal  = Calendar.getInstance();
                            try {
                                cal.setTime(Util.formatoFechaActual().parse(filterableString));
                            } catch (java.text.ParseException e) {
                                e.printStackTrace();
                            }
                            int anyoMov = cal.get(Calendar.YEAR);
                            filterableString = list.get(i).getTipoMovimiento();
                            if(filterableEditText.isEmpty()) {
                                if (getCategoriaSeleccionada() != null && !getCategoriaSeleccionada().isEmpty()) {
                                    if (filterableString.toLowerCase().trim().equals(filterString.trim())
                                            && list.get(i).getCategoria().toLowerCase().trim().equals(getCategoriaSeleccionada().toLowerCase())) {
                                        if (anyoMov == anyoSeleccionado)
                                            nlist.add(list.get(i));
                                    }
                                } else {
                                    if (filterableString.toLowerCase().trim().equals(filterString.trim())) {
                                        if (anyoMov == anyoSeleccionado)
                                            nlist.add(list.get(i));
                                    }
                                }
                            }
                            else {
                                if (getCategoriaSeleccionada() != null && !getCategoriaSeleccionada().isEmpty()) {
                                    if (filterableString.toLowerCase().trim().equals(filterString.trim())
                                            && list.get(i).getCategoria().toLowerCase().trim().equals(getCategoriaSeleccionada().toLowerCase())
                                            && list.get(i).getDescripcion().toLowerCase().trim().contains(filterableEditText.toLowerCase().trim())) {
                                        if (anyoMov == anyoSeleccionado)
                                            nlist.add(list.get(i));
                                    }
                                } else {
                                    if (filterableString.toLowerCase().trim().equals(filterString.trim())
                                            && list.get(i).getDescripcion().toLowerCase().trim().contains(filterableEditText.toLowerCase().trim())) {
                                        if (anyoMov == anyoSeleccionado)
                                            nlist.add(list.get(i));
                                    }
                                }
                            }
                        }
                        results.values = nlist;
                        results.count = nlist.size();
                        return results;
                    }
                    //filtro anyo all y filtro mes
                    else if(getAnyoSeleccionado() == Constantes.NUMERO_ANYO_TODO){
                        for (int i = 0; i < count; i++) {
                            filterableString = list.get(i).getFecha();
                            Calendar cal  = Calendar.getInstance();
                            try {
                                cal.setTime(Util.formatoFechaActual().parse(filterableString));
                            } catch (java.text.ParseException e) {
                                e.printStackTrace();
                            }
                            int mesMov = cal.get(Calendar.MONTH);
                            filterableString = list.get(i).getTipoMovimiento();
                            if(filterableEditText.isEmpty()) {
                                if (getCategoriaSeleccionada() != null && !getCategoriaSeleccionada().isEmpty()) {
                                    if (filterableString.toLowerCase().trim().equals(filterString.trim())
                                            && list.get(i).getCategoria().toLowerCase().trim().equals(getCategoriaSeleccionada().toLowerCase())) {
                                        if (mesMov == mesSeleccionado1)
                                            nlist.add(list.get(i));
                                    }
                                } else {
                                    if (filterableString.toLowerCase().trim().equals(filterString.trim())) {
                                        if (mesMov == mesSeleccionado1)
                                            nlist.add(list.get(i));
                                    }
                                }
                            }
                            else{
                                if (getCategoriaSeleccionada() != null && !getCategoriaSeleccionada().isEmpty()) {
                                    if (filterableString.toLowerCase().trim().equals(filterString.trim())
                                            && list.get(i).getCategoria().toLowerCase().trim().equals(getCategoriaSeleccionada().toLowerCase())
                                            && list.get(i).getDescripcion().toLowerCase().trim().contains(filterableEditText.toLowerCase().trim())) {
                                        if (mesMov == mesSeleccionado1)
                                            nlist.add(list.get(i));
                                    }
                                } else {
                                    if (filterableString.toLowerCase().trim().equals(filterString.trim())
                                            && list.get(i).getDescripcion().toLowerCase().trim().contains(filterableEditText.toLowerCase().trim())) {
                                        if (mesMov == mesSeleccionado1)
                                            nlist.add(list.get(i));
                                    }
                                }
                            }
                        }
                        results.values = nlist;
                        results.count = nlist.size();
                        return results;
                    }

                    for (int i = 0; i < count; i++) {
                        String fecha = list.get(i).getFecha();
                        Calendar cal  = Calendar.getInstance();
                        try {
                            cal.setTime(Util.formatoFechaActual().parse(fecha));
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                        int mesMov = cal.get(Calendar.MONTH);
                        int anyoMov = cal.get(Calendar.YEAR);
                        filterableString = list.get(i).getTipoMovimiento();
                        if(filterableEditText.isEmpty()) {
                            if (getCategoriaSeleccionada() != null && !getCategoriaSeleccionada().isEmpty()) {
                                if (filterableString.toLowerCase().trim().equals(filterString.trim())
                                        && list.get(i).getCategoria().toLowerCase().trim().equals(getCategoriaSeleccionada().toLowerCase())) {
                                    if (mesMov == mesSeleccionado1 && anyoMov == anyoSeleccionado)
                                        nlist.add(list.get(i));
                                }
                            } else {
                                if (filterableString.toLowerCase().trim().equals(filterString.trim())) {
                                    if (mesMov == mesSeleccionado1 && anyoMov == anyoSeleccionado)
                                        nlist.add(list.get(i));
                                }
                            }
                        }
                        else{
                            if (getCategoriaSeleccionada() != null && !getCategoriaSeleccionada().isEmpty()) {
                                if (filterableString.toLowerCase().trim().equals(filterString.trim())
                                        && list.get(i).getCategoria().toLowerCase().trim().equals(getCategoriaSeleccionada().toLowerCase())
                                        && list.get(i).getDescripcion().toLowerCase().trim().contains(filterableEditText.toLowerCase().trim())) {
                                    if (mesMov == mesSeleccionado1 && anyoMov == anyoSeleccionado)
                                        nlist.add(list.get(i));
                                }
                            } else {
                                if (filterableString.toLowerCase().trim().equals(filterString.trim())
                                        && list.get(i).getDescripcion().toLowerCase().trim().contains(filterableEditText.toLowerCase().trim())) {
                                    if (mesMov == mesSeleccionado1 && anyoMov == anyoSeleccionado)
                                        nlist.add(list.get(i));
                                }
                            }
                        }
                    }
                    results.values = nlist;
                    results.count = nlist.size();
                }

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                updateReceiptsList((ArrayList<MovimientoItem>) results.values);
            }

        }
}
