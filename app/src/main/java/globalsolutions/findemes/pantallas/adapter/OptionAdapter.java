package globalsolutions.findemes.pantallas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import globalsolutions.findemes.R;
import globalsolutions.findemes.database.model.OptionItem;

/**
 * Created by manuel.molero on 04/02/2015.
 */

public class OptionAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<OptionItem> items;
    int [] imageId;

    public OptionAdapter(Context context, ArrayList<OptionItem> items, int[] prgmImages) {
        this.context = context;
        this.items = items;
        this.imageId = prgmImages;
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
            rowView = inflater.inflate(R.layout.option_item, parent, false);
        }

        // Set data into the view.

        TextView tvDescr = (TextView) rowView.findViewById(R.id.tvOption);

        OptionItem item = this.items.get(position);
        tvDescr.setText(item.getDescripcion());

        ImageView ivOption = (ImageView) rowView.findViewById(R.id.ivOption);
        ivOption.setImageResource(imageId[position]);
        if(position % 2 == 0)
            rowView.setBackgroundColor(context.getResources().getColor(R.color.button_material_light));

        return rowView;
    }
}
