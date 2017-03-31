package pritam.eventbuzz.com.eventbuzz;

import java.util.Calendar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

public class AddEvent extends Activity implements OnClickListener {
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    private class CreateEvent extends AsyncTask <Void, Void, String> {
        String mEventName,mEventDescription,mEventDate,mEventTime,mEventDuration,mEventCapacity,mEventVenue,mEventCity,mEventCategory;

        private JSONObject mUser;
        public CreateEvent(Event event){
            mEventName = event.getEventName();
            mEventDescription = event.getEventDescription();
            mEventDate = event.getEventDate();
            mEventTime = event.getEventTime();
            mEventDuration=event.getEventDuration();
            mEventCapacity=event.getEventCapacity();
            mEventVenue=event.getEventVenue();
            mEventCity=event.getEventCity();
            mEventCategory=event.getEventCategory();
            mUser = null;
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONObject myJSON = new JSONObject();
            try{
                myJSON.put("name",mEventName);
                myJSON.put("description",mEventDescription);
                myJSON.put("date",mEventDate);
                myJSON.put("time",mEventTime);
                myJSON.put("duration",mEventDuration);
                myJSON.put("capacity",mEventCapacity);
                myJSON.put("venue",mEventVenue);
                myJSON.put("city",mEventCity);
                myJSON.put("category",mEventCategory);
            }
            catch(JSONException e){}
            HttpHelper myHttp = new HttpHelper(getResources().getString(R.string.http_server_ip_port));
            mUser = myHttp.postJson("/events.json",myJSON,"","");
            return "success";
        }

        protected void onPostExecute(String results) {
            Button b = (Button)findViewById(R.id.btnCreateEvent);
            b.setClickable(true);
            if(mEventName.length()>0) {
                Toast.makeText(getApplicationContext(), "Event Created", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddEvent.this, MyListActivity.class);
                startActivity(intent);
            }
            else Toast.makeText(getApplicationContext(), "Enter event details", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        findViewById(R.id.btnCreateEvent).setOnClickListener(this);
        setCurrentDate();
    }

    public void setCurrentDate(){
        dateView = (Button)findViewById(R.id.setDate);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
    }

    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    public void setDate(View view){
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Select event date",Toast.LENGTH_SHORT).show();
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                year=arg1;
                month=arg2+1;
                day=arg3;
                showDate(arg1, arg2+1, arg3);
            }
    };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
    }

    private String getDate(){
        String date = day+"/"+month+"/"+year;
        return date;
    }

    public void getEventDetails(Event event){
        final EditText eventName = (EditText)findViewById(R.id.EventName);
        final EditText eventDescription = (EditText)findViewById(R.id.EventDescription);
        final EditText eventTime = (EditText)findViewById(R.id.EventTime);
        final EditText eventDuration = (EditText)findViewById(R.id.EventDuration);
        final EditText eventCapacity = (EditText)findViewById(R.id.EventCapacity);
        final EditText eventVenue = (EditText)findViewById(R.id.EventVenue);
        final EditText eventCity = (EditText)findViewById(R.id.EventCity);
        final EditText eventCategory = (EditText)findViewById(R.id.EventCategory);

        event.setEventName(eventName.getText().toString());
        event.setEventDescription(eventDescription.getText().toString());
        event.setEventDate(getDate());
        event.setEventTime(eventTime.getText().toString());
        event.setEventDuration(eventDuration.getText().toString());
        event.setEventCapacity(eventCapacity.getText().toString());
        event.setEventVenue(eventVenue.getText().toString());
        event.setEventCity(eventCity.getText().toString());
        event.setEventCategory(eventCategory.getText().toString());
    }

    @Override
    public void onClick(View arg0) {
        Button b = (Button)findViewById(R.id.btnCreateEvent);
        b.setClickable(false);
        Event event = new Event();
        getEventDetails(event);
        CreateEvent createEvent = new CreateEvent(event);
        createEvent.execute();
    }
}
