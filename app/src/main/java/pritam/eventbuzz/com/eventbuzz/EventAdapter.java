package pritam.eventbuzz.com.eventbuzz;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
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
    private final ArrayList<String> image_url;

    public EventAdapter(Context context, ArrayList<String> values, ArrayList<String> date,ArrayList<String> image) {
        super(context, R.layout.rowlayout, values);
        this.context = context;
        this.values = values;
        this.date = date;
        this.image_url = image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView textView1 = (TextView) rowView.findViewById(R.id.firstLine);
        textView1.setText(values.get(position));
        TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
        ImageView iv = (ImageView) rowView.findViewById(R.id.icon);
        if(date.get(position).equals("event_title")) {
            iv.setVisibility(View.GONE);
            textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
            textView2.setVisibility(View.GONE);
        }
        else{
            ImageHandler imageHandler = new ImageHandler(iv,"http://192.168.43.23:3000/images/"+image_url.get(position));
            imageHandler.execute();
            //imageHandler.loadImage("http://192.168.43.23:3000/images/event1.jpg");
            textView2.setText(date.get(position));
        }
        return rowView;
    }
}
