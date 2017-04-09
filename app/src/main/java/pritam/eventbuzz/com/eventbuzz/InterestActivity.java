package pritam.eventbuzz.com.eventbuzz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class InterestActivity extends Activity {

    private boolean[] thumbnailsSelection;
    private JSONArray interests;
    private JSONObject userInterests;
    private ArrayList<String> interestList = new ArrayList<String>();
    Hashtable<String,Integer> interestMap = new Hashtable<String,Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        populateInterestMap();
        thumbnailsSelection = new boolean[6];
        new GetInterests().execute();

        final Button doneBtn = (Button) findViewById(R.id.doneBtn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new SubmitInterest().execute();
                Intent intent = new Intent(v.getContext(),TabView.class);
                startActivity(intent);
            }
        });
    }

    private void populateInterestMap(){
        interestMap.put("Sports",0);
        interestMap.put("Dance",1);
        interestMap.put("Music",2);
        interestMap.put("Business",3);
        interestMap.put("Exhibitions",4);
        interestMap.put("Workshops",5);
    }

    private void setThumbnailSelection(){
        for (String interest : interestList){
            thumbnailsSelection[interestMap.get(interest)] = true;
        }
    }

    private class SubmitInterest extends AsyncTask<Void, Void, Boolean> {
        private String toast;

        public SubmitInterest() {
            toast = null;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            HttpHelper httpHelper = new HttpHelper(getResources().getString(R.string.http_server_ip_port));
            interests = new JSONArray();
            try {
                userInterests = new JSONObject();
                userInterests.put("email", DataHolder.getEmail());
                if (thumbnailsSelection[0]==true){ interests.put("Sports"); }
                if (thumbnailsSelection[1]==true){ interests.put("Dance");  }
                if (thumbnailsSelection[2]==true){ interests.put("Music");  }
                if (thumbnailsSelection[3]==true){ interests.put("Business");  }
                if (thumbnailsSelection[4]==true){ interests.put("Exhibitions");  }
                if (thumbnailsSelection[5]==true){ interests.put("Workshops");  }
                userInterests.put("interests", interests);
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            if(httpHelper.postJson("/user_interests.json",userInterests,"","")==null)
                return false;
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success) {
            } else {
                Toast.makeText(getBaseContext(), "Connection problem...", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class GetInterests extends AsyncTask<Void, Void, Boolean> {
        private String toast;
        private  JSONArray getUserInterests  =new JSONArray();
        private JSONObject userEmail = new JSONObject();

        public GetInterests() {
            toast = null;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                HttpHelper httpHelper = new HttpHelper(getResources().getString(R.string.http_server_ip_port));
                userEmail.put("email", DataHolder.getEmail());
                getUserInterests = httpHelper.postJsonArray("/user_interests/index.json", userEmail,"", "");
                if (getUserInterests == null) {
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
                for(int i=0; i< getUserInterests.length();i++){
                    try {
                        interestList.add((String) getUserInterests.getJSONObject(i).get("interest"));
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            } else {
                Toast.makeText(getBaseContext(), "Connection problem 2...", Toast.LENGTH_LONG).show();
            }
            setThumbnailSelection();
            GridView gridView = (GridView) findViewById(R.id.InterestImageGrid);
            gridView.setAdapter(new ImageAdapter());
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        public Integer[] mThumbIds = {R.drawable.sports,R.drawable.dance,R.drawable.music,R.drawable.business,R.drawable.exhibitions,R.drawable.workshops};
        private Context mContext;
        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           //this.mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return mThumbIds[position];
        }

        public long getItemId(int position) {
            return 0;
        }

        @Override
        /*public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(this.mContext);
            imageView.setImageResource(mThumbIds[position]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new GridView.LayoutParams(70, 70));
            return imageView;
        }*/

        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.interest_item, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.checkbox.setId(position);
            holder.imageview.setId(position);

          /*  for (int i=0;i<thumbnailsSelection.length;i++){
                System.out.println("###############################################");
                if (thumbnailsSelection[i]){
                    System.out.println("visible");
                    holder.checkbox.setVisibility(View.VISIBLE);
                } else {
                  *//*  System.out.println("invisible");
                    holder.checkbox.setVisibility(View.INVISIBLE);*//*
                }
                System.out.println("###############################################");
                holder.checkbox.setChecked(thumbnailsSelection[i]);
            }*/

            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if (thumbnailsSelection[id]){
                        cb.setChecked(false);
                        thumbnailsSelection[id] = false;
                    } else {
                        cb.setChecked(true);
                        thumbnailsSelection[id] = true;
                    }
                }
            });

            holder.imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView iv = (ImageView) v;
                    int id = iv.getId();
                    if (thumbnailsSelection[id]){
                        thumbnailsSelection[id] = false;
                        holder.checkbox.setVisibility(View.INVISIBLE);
                    } else {
                        thumbnailsSelection[id] = true;
                        holder.checkbox.setVisibility(View.VISIBLE);
                    }
                    holder.checkbox.setChecked(thumbnailsSelection[id]);
                }
            });

            //holder.imageview.setOnClickListener(new ImageClickListener(context, holder,thumbnailsselection));
            holder.imageview.setImageResource(mThumbIds[position]);
            holder.checkbox.setChecked(thumbnailsSelection[position]);
            holder.id = position;
            return convertView;
        }

        class ViewHolder {
            ImageView imageview;
            CheckBox checkbox;
            int id;
        }
    }
}
