package globalsolutions.findemes.pantallas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import globalsolutions.findemes.R;
import globalsolutions.findemes.database.model.CuentaItem;

/**
 * Created by manuel.molero on 04/02/2015.
 */

public class CuentaAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<CuentaItem> items;

    public CuentaAdapter(Context context, ArrayList<CuentaItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
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
            rowView = inflater.inflate(R.layout.cuenta_item, parent, false);
        }

        // Set data into the view.
        TextView tvNombreCuenta = (TextView) rowView.findViewById(R.id.tvNombreCuenta);
        TextView tvNumeroCuenta = (TextView) rowView.findViewById(R.id.tvNumeroCuenta);
        TextView tvFechaCuenta = (TextView) rowView.findViewById(R.id.tvFechaCuenta);

        CuentaItem item = this.items.get(position);
        tvNombreCuenta.setText(item.getNombre());
        tvNumeroCuenta.setText(item.getNumero());
        tvFechaCuenta.setText(item.getFecha());

        if(position % 2 == 0)
            rowView.setBackgroundColor(context.getResources().getColor(R.color.button_material_light));

        return rowView;
    }

    public void updateReceiptsList(ArrayList<CuentaItem> newlist) {
        items.clear();
        items.addAll(newlist);
        this.notifyDataSetChanged();
    }
}
