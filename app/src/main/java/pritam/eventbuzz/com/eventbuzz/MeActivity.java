package pritam.eventbuzz.com.eventbuzz;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

class MeAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> options;

    public MeAdapter(Context context, ArrayList<String> options) {
        super(context, R.layout.me_options, options);
        this.context = context;
        this.options = options;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.me_options, parent, false);
        TextView textView1 = (TextView) rowView.findViewById(R.id.optionFirstLine);
        textView1.setText(options.get(position));
        return rowView;
    }
}

public class MeActivity extends ListActivity{

    private ArrayList<String> options = new ArrayList<String>();
    LinkedHashMap<String,Integer> meOptions = new LinkedHashMap<String,Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        meOptions.put("My Interests",1);
        meOptions.put("Events History",2);
        meOptions.put("Events Attending",3);
        meOptions.put("Send Feedback",4);
        meOptions.put("Settings",5);
        meOptions.put("About us",6);
        meOptions.put("Sign out",7);
        updateOptions();
    }

    private void updateOptions(){

        for(Map.Entry m:meOptions.entrySet()){
            options.add((String) m.getKey());
        }
        MeAdapter adapter = new MeAdapter(this,options);
        setListAdapter(adapter);
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = null;
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
        switch(meOptions.get(item)){
            case 1: intent = new Intent(this,InterestActivity.class);
                    break;
            case 2: //Add event history activity
                    intent = new Intent(this,InterestActivity.class);
                    break;
            case 3: intent = new Intent(this,AttendEventListActivity.class);
                    break;
        }
       //intent.putExtra(EXTRA_MESSAGE,eventIds.get(position).toString());
        if (intent!=null)
            startActivity(intent);
    }
}