package pritam.eventbuzz.com.eventbuzz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pritam on 8/4/17.
 */

public class EventAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;
    private final ArrayList<String> date;
    private boolean title;
    private String category = "";

    public EventAdapter(Context context, ArrayList<String> values, ArrayList<String> date) {
        super(context, R.layout.rowlayout, values);
        this.context = context;
        this.values = values;
        this.date = date;
        this.title = false;
    }

    /*public EventAdapter(Context context, ArrayList<String> values, ArrayList<String> date, String category) {
        super(context, R.layout.rowlayout, values);
        this.context = context;
        this.values = values;
        this.category = category;
        this.date = date;
        this.title = true;
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        /*if(title==true) {
            TextView title = (TextView) rowView.findViewById(R.id.title);
            title.setText(category);
            title.setVisibility(View.VISIBLE);
        }*/
        //else {
            TextView textView1 = (TextView) rowView.findViewById(R.id.firstLine);
            textView1.setText(values.get(position));
            if(date!=null) {
                TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
                textView2.setText(date.get(position));
            }
        //}
        return rowView;
    }
}
