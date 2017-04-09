package pritam.eventbuzz.com.eventbuzz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static pritam.eventbuzz.com.eventbuzz.R.id.parent;

public class DisplayEvent extends Activity implements View.OnClickListener {
    private String eventId;
    private JSONObject event;
    private JSONObject getAttendStatus;
    private Button attendYes;
    private Button attendNo;
    private String attendStatus="";
    private String setAttendStatus="NO";
    TextView tvAttend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_event);
        Intent intent = getIntent();
        eventId = intent.getStringExtra(MyListActivity.EXTRA_MESSAGE);
        attendYes = (Button) findViewById(R.id.attendYes);
        attendNo = (Button) findViewById(R.id.attendNo);
        tvAttend = (TextView) findViewById(R.id.event_attending);
        attendYes.setOnClickListener(this);
        attendNo.setOnClickListener(this);
        new GetAttendStatus().execute();
        new GetEvent().execute();
    }

    private void  updateEvent(){
        try{
            String eventName = event.get("name").toString();
            String eventLocation = event.get("venue").toString();
            String eventDate = event.get("date").toString() + " at " +event.get("time").toString();
            String eventDescription = event.get("description").toString();
            String image = event.get("image").toString();
            ImageView iv = (ImageView) findViewById(R.id.display_image);
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println(image);
            System.out.println(iv);
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            ImageHandler imageHandler = new ImageHandler(iv,"http://192.168.43.23:3000/images/"+image);
            imageHandler.execute();
            TextView location = (TextView) findViewById(R.id.location);
            location.setText(eventLocation);
            TextView date = (TextView) findViewById(R.id.date);
            date.setText(eventDate);
            TextView description = (TextView) findViewById(R.id.description);
            description.setText(eventDescription);
        }catch (JSONException e){ }
    }

    private void attendStatusYes(){
        tvAttend.setText("You are attending");
        attendYes.setClickable(false);
        attendNo.setClickable(false);
        attendYes.setVisibility(View.INVISIBLE);
        attendNo.setVisibility(View.INVISIBLE);
        setAttendStatus = "YES";
    }

    private void attendStatusNo(){
        attendNo.setClickable(false);
        attendNo.setVisibility(View.INVISIBLE);
        setAttendStatus = "MAYBE";
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.attendYes:
                attendStatusYes();
                break;

            case R.id.attendNo:
                attendStatusNo();
                break;
        }
        new sendAttendStatus().execute();
    }

    private void setAttendStatus(){
        if(attendStatus.equalsIgnoreCase("Yes")){
            attendStatusYes();
        }
        else if (attendStatus.equalsIgnoreCase("MayBe")){
            attendStatusNo();
        }
    }

    private class sendAttendStatus extends AsyncTask<Void, Void, Boolean>{
        private String toast;

        public sendAttendStatus() {
            toast = null;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject requestParam = new JSONObject();
            JSONObject response;
            try {
                requestParam.put("email", DataHolder.getEmail());
                requestParam.put("eventId",eventId);
                requestParam.put("status",setAttendStatus);
            }catch(JSONException e){ e.printStackTrace();}
            HttpHelper httpHelper = new HttpHelper(getResources().getString(R.string.http_server_ip_port));
            response = httpHelper.postJson("/user_events.json",requestParam,"","");
            if(response == null) {
                toast = "Connection problem...";
                return false;
            }
            return true;
        }
    }

    private class GetAttendStatus extends AsyncTask<Void, Void, Boolean> {
        private String toast;

        public GetAttendStatus() {
            toast = null;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            JSONObject requestParam = new JSONObject();
            try {
                requestParam.put("email", DataHolder.getEmail());
                requestParam.put("eventId",eventId);
            }catch(JSONException e){ e.printStackTrace();}
            HttpHelper httpHelper = new HttpHelper(getResources().getString(R.string.http_server_ip_port));
            getAttendStatus = httpHelper.postJson("/user_events/get_status.json",requestParam, "", "");
            if(getAttendStatus == null) {
                toast = "Connection problem...";
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success) {
                try {
                    attendStatus = getAttendStatus.get("status").toString();
                    setAttendStatus();
                }catch(JSONException e){ e.printStackTrace();}
            } else {
                Toast.makeText(getBaseContext(), toast, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class GetEvent extends AsyncTask<Void, Void, Boolean> {
        private String toast;

        public GetEvent() {
            toast = null;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            HttpHelper httpHelper = new HttpHelper(getResources().getString(R.string.http_server_ip_port));
            event = httpHelper.getJsonObject("/events/"+eventId +".json", "", "");
            if(event == null) {
                toast = "Connection problem...";
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success) {
                updateEvent();
            } else {
                Toast.makeText(getBaseContext(), "Connection problem...", Toast.LENGTH_LONG).show();
            }
        }
    }
}