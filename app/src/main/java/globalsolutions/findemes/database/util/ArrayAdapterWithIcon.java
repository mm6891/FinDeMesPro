package globalsolutions.findemes.database.util;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import globalsolutions.findemes.R;

/**
 * Created by manuel.molero on 17/04/2015.
 */
public class ArrayAdapterWithIcon extends ArrayAdapter<String> {

    private int [] images;

    public ArrayAdapterWithIcon(Context context, String[] items, int [] images) {
        super(context, R.layout.text_icon, items);
        this.images = images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(R.id.tvIcon);
        textView.setCompoundDrawablesWithIntrinsicBounds(images[position], 0, 0, 0);
        textView.setCompoundDrawablePadding(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getContext().getResources().getDisplayMetrics()));
        return view;
    }

}
