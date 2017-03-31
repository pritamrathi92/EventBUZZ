package pritam.eventbuzz.com.eventbuzz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


class EventAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;
    private final ArrayList<String> date;

    public EventAdapter(Context context, ArrayList<String> values, ArrayList<String> date) {
        super(context, R.layout.rowlayout, values);
        this.context = context;
        this.values = values;
        this.date = date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView textView1 = (TextView) rowView.findViewById(R.id.firstLine);
        TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
        textView1.setText(values.get(position));
        textView2.setText(date.get(position));
        return rowView;
    }
}


public class MyListActivity extends ListActivity implements View.OnClickListener {
    public static String EXTRA_MESSAGE = "pritam.eventbuzz.com.eventbuzz";
    private JSONArray events;
    private ArrayList<String> eventTitle = new ArrayList<String>();
    private ArrayList<String> eventDate = new ArrayList<String>();
    private ArrayList<Integer> eventIds = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        findViewById(R.id.floatingActionButton).setOnClickListener(this);
        new GetEvents().execute();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MyListActivity.this,DisplayEvent.class);
        intent.putExtra(EXTRA_MESSAGE,eventIds.get(position).toString());
        startActivity(intent);
    }

    private void updateEvents(){
        for(int i=0; i< events.length();i++){
            try {
                eventIds.add((Integer) events.getJSONObject(i).get("id"));
                eventTitle.add((String) events.getJSONObject(i).get("name"));
                eventDate.add((String) events.getJSONObject(i).get("date"));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        EventAdapter adapter = new EventAdapter(this,eventTitle,eventDate);
        setListAdapter(adapter);
    }

    public void onClick(View v) {
        Intent intent = new Intent(MyListActivity.this,AddEvent.class);
        startActivity(intent);
    }

    public class GetEvents extends AsyncTask<Void, Void, Boolean> {
        private String toast;

        public GetEvents() {
            toast = null;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            HttpHelper httpHelper = new HttpHelper(getResources().getString(R.string.http_server_ip_port));
            events = httpHelper.getJson("/events.json", "", "");
            if(events == null) {
                toast = "Connection problem...";
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success) {
                updateEvents();
            } else {
                Toast.makeText(getBaseContext(), "Connection problem...", Toast.LENGTH_LONG).show();
            }
        }
    }
}