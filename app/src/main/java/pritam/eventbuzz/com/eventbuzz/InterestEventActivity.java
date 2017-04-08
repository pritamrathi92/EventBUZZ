package pritam.eventbuzz.com.eventbuzz;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class InterestEventActivity extends ListActivity implements View.OnClickListener{
    public static String EXTRA_MESSAGE = "pritam.eventbuzz.com.eventbuzz";
    private HashMap<String,JSONArray > userInterestEventList = new HashMap<String,JSONArray>();
    private ArrayList<String> eventTitle = new ArrayList<String>();
    private ArrayList<String> eventDate = new ArrayList<String>();
    private ArrayList<Integer> eventIds = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_event);
        new GetInterestEvent().execute();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,DisplayEvent.class);
        intent.putExtra(EXTRA_MESSAGE,eventIds.get(position).toString());
        startActivity(intent);
    }

    private void updateEvents(){
        for(Map.Entry m:userInterestEventList.entrySet()) {
            JSONArray events = (JSONArray) m.getValue();
            /*EventAdapter adapter1 = new EventAdapter(this,eventTitle,eventDate,(String)m.getKey());
            setListAdapter(adapter1);*/
            eventTitle.add((String)m.getKey());
            eventDate.add(null);
            for (int i = 0; i < events.length(); i++) {
                try {
                    eventIds.add((Integer) events.getJSONObject(i).get("id"));
                    eventTitle.add((String) events.getJSONObject(i).get("name"));
                    eventDate.add((String) events.getJSONObject(i).get("date"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            EventAdapter adapter = new EventAdapter(this,eventTitle,eventDate);
            setListAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {

    }

    private class GetInterestEvent extends AsyncTask<Void, Void, Boolean> {
        private String toast;
        private JSONObject getUserInterestEvent =new JSONObject();
        private JSONObject userEmail = new JSONObject();

        public GetInterestEvent() {
            toast = null;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                HttpHelper httpHelper = new HttpHelper(getResources().getString(R.string.http_server_ip_port));
                userEmail.put("email", DataHolder.getEmail());
                getUserInterestEvent = httpHelper.postJson("/events/newsfeed.json", userEmail,"", "");
                if (getUserInterestEvent == null) {
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
                try {
                    Iterator<String> keys = getUserInterestEvent.keys();
                    while(keys.hasNext()){
                        String key = (String)keys.next();
                        JSONArray temp;
                        temp = getUserInterestEvent.getJSONArray(key);
                        userInterestEventList.put(key,temp);
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                updateEvents();
            } else {
                Toast.makeText(getBaseContext(), "Connection problem 2...", Toast.LENGTH_LONG).show();
            }
        }
    }
}
