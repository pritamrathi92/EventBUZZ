package pritam.eventbuzz.com.eventbuzz;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
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
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity implements OnClickListener {
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private class LongRunningGetIO extends AsyncTask <Void, Void, String> {
        String mEventName,mEventDescription,mEventDate,mEventTime,mEventDuration,mEventCapacity,mEventVenue,mEventCity,mEventCategory;

        private JSONObject mUser;
        public LongRunningGetIO(String eventName,String eventDescription,String eventDate,String eventTime,String eventDuration,String eventCapacity,String eventVenue, String eventCity,String eventCategory){
            mEventName = eventName;
            mEventDescription = eventDescription;
            mEventDate = eventDate;
            mEventTime = eventTime;
            mEventDuration=eventDuration;
            mEventCapacity=eventCapacity;
            mEventVenue=eventVenue;
            mEventCity=eventCity;
            mEventCategory=eventCategory;
            mUser = null;
        }

        @Override
        protected String doInBackground(Void... params) {
            /*HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet("http://192.168.43.23:3000/events/1.json");
            String text = null;
            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);
            } catch (Exception e) {
                return e.getLocalizedMessage();
            }
            return text;*/
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
            /*if (results!=null) {
                EditText et = (EditText)findViewById(R.id.my_edit);
                et.setText(results);
            }*/

            Button b = (Button)findViewById(R.id.btnCreateEvent);
            b.setClickable(true);
            if(mEventName.length()>0) {
                Toast.makeText(getApplicationContext(), "Event Created", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, MyListActivity.class);
                startActivity(intent);
            }
            else Toast.makeText(getApplicationContext(), "Enter event details", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnCreateEvent).setOnClickListener(this);

        dateView = (Button)findViewById(R.id.setDate);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
    }

    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
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
                // TODO Auto-generated method stub
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


    @Override
    public void onClick(View arg0) {
        Button b = (Button)findViewById(R.id.btnCreateEvent);
        b.setClickable(false);
        final EditText eventName = (EditText)findViewById(R.id.EventName);
        final EditText eventDescription = (EditText)findViewById(R.id.EventDescription);
        final EditText eventTime = (EditText)findViewById(R.id.EventTime);
        final EditText eventDuration = (EditText)findViewById(R.id.EventDuration);
        final EditText eventCapacity = (EditText)findViewById(R.id.EventCapacity);
        final EditText eventVenue = (EditText)findViewById(R.id.EventVenue);
        final EditText eventCity = (EditText)findViewById(R.id.EventCity);
        final EditText eventCategory = (EditText)findViewById(R.id.EventCategory);
        System.out.println(eventName.getText().toString());
        System.out.println(eventDescription.getText().toString());
        System.out.println(getDate());
        System.out.println(eventTime.getText().toString());
        System.out.println();
        LongRunningGetIO exec = new LongRunningGetIO(eventName.getText().toString(),eventDescription.getText().toString(),getDate(),eventTime.getText().toString(),eventDuration.getText().toString(),eventCapacity.getText().toString(),eventVenue.getText().toString(),eventCity.getText().toString(),eventCategory.getText().toString());
        exec.execute();
    }
}
