package globalsolutions.findemes.pantallas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import globalsolutions.findemes.R;

/**
 * Created by manuel.molero on 04/02/2015.
 */

public class CategoriaAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<String> items;
    private ArrayList<String> itemsFiltrado;

    public CategoriaAdapter(Context context, ArrayList<String> items) {
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
            rowView = inflater.inflate(R.layout.category_item, parent, false);
        }

        // Set data into the view.
        TextView tvDescr = (TextView) rowView.findViewById(R.id.tvCategoria);
        tvDescr.setText(itemsFiltrado.get(position));

        if(position % 2 == 0)
            rowView.setBackgroundColor(context.getResources().getColor(R.color.button_material_light));

        return rowView;
    }

    public void updateReceiptsList(ArrayList<String> newlist) {
        itemsFiltrado.clear();
        itemsFiltrado.addAll(newlist);
        this.notifyDataSetChanged();
    }
}
