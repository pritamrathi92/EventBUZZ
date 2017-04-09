package pritam.eventbuzz.com.eventbuzz;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class AttendEventListActivity extends ListActivity {

    public static String EXTRA_MESSAGE = "pritam.eventbuzz.com.eventbuzz";
    private JSONArray events;
    private ArrayList<String> eventTitle = new ArrayList<String>();
    private ArrayList<String> eventDate = new ArrayList<String>();
    private ArrayList<Integer> eventIds = new ArrayList<Integer>();
    private ArrayList<String> eventImage = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_event_list);
        new GetAttendingEvent().execute();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(AttendEventListActivity.this,DisplayEvent.class);
        intent.putExtra(EXTRA_MESSAGE,eventIds.get(position).toString());
        startActivity(intent);
    }

    private void updateEvents(){
        for(int i=0; i< events.length();i++){
            try {
                eventIds.add((Integer) events.getJSONObject(i).get("id"));
                eventTitle.add((String) events.getJSONObject(i).get("name"));
                eventDate.add((String) events.getJSONObject(i).get("date"));
                eventImage.add((String) events.getJSONObject(i).get("image"));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        EventAdapter adapter = new EventAdapter(this,eventTitle,eventDate,eventImage);
        setListAdapter(adapter);
    }

    private class GetAttendingEvent extends AsyncTask<Void, Void, Boolean> {
        private String toast;
        private JSONObject requestParams = new JSONObject();

        public GetAttendingEvent() {
            toast = null;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                HttpHelper httpHelper = new HttpHelper(getResources().getString(R.string.http_server_ip_port));
                requestParams.put("email", DataHolder.getEmail());
                events = httpHelper.postJsonArray("/user_events/index.json", requestParams,"", "");
                if (events == null) {
                    toast = "Connection problem 1...";
                    return false;
                }
            }
            catch(JSONException e){
                e.printStackTrace();
                return  false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success) {
                updateEvents();
            } else {
                Toast.makeText(getBaseContext(), toast, Toast.LENGTH_LONG).show();
            }
        }
    }
}
