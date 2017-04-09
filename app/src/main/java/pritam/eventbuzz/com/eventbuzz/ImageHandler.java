package pritam.eventbuzz.com.eventbuzz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by pritam on 9/4/17.
 */

public class ImageHandler extends AsyncTask<Void, Void, Boolean> {
    Bitmap bitmap;
    private ImageView imageView;
    private String image_location;

    public ImageHandler(ImageView imageView, String image_url){
        this.imageView = imageView;
        this.image_location = image_url;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        URL imageURL = null;
        try {
            imageURL = new URL(image_location);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection connection = (HttpURLConnection) imageURL.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(success) {
            imageView.setImageBitmap(bitmap);
        } else {
            //Toast.makeText(getBaseContext(), "Connection problem...", Toast.LENGTH_LONG).show();
        }
    }
}
