package com.curtidosbadia.badiapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.curtidosbadia.badiapp.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;

        try {
            /*if(!URLUtil.isHttpsUrl(urldisplay)){
                urldisplay = urldisplay.replace("http", "https");
            }*/

            URL url = new URL(urldisplay);
            HttpsURLConnection ucon = (HttpsURLConnection) url.openConnection();
            InputStream in = ucon.getInputStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            mIcon11 = BitmapFactory.decodeResource(bmImage.getResources(), R.drawable.img_2299);
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}

