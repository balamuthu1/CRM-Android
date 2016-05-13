package fr.pds.isintheair.crmtab.model.asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.pds.isintheair.crmtab.helper.FormatValidator;
import fr.pds.isintheair.crmtab.model.entity.MapInfo;
import fr.pds.isintheair.crmtab.model.rest.service.CustomerService;

/**
 * Created by tlacouque on 04/02/2016.
 * A sync task used to download and save an image on the disk
 */
public class TileDownloader extends AsyncTask<MapInfo,Integer,Boolean> {

    /**
     * Method called when the TileDownloader Asynctask is executed.
     * Download and save an image on the disk
     * @param params, mapinfo which represent where to download the map
     * @return
     */
    @Override
    protected Boolean doInBackground(MapInfo... params) {
        MapInfo mapInfo = params[0];
        String url = CustomerService.BASE_URL+ FormatValidator.formatUrlTile(mapInfo);
        Bitmap img = null;

        while (img == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
               //do something
            }
            img = downloadBitmap (url);
            if(img == null) {
                Log.d("ImgNull","Image est null");
            }
        }

        return saveImage(img,FormatValidator.formatPathTile(mapInfo));

    }

    /**
     * Method used to download and return a bitmap from an image.
     * @param url, url to the image
     * @return Bitmap, Bitmap from image downloaded
     */
    public Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();

            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
           // return inputStream;
           if (inputStream != null) {

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            Log.d("URLCONNECTIONERROR", e.toString());
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();

            }
        }
        return null;
    }

    /**
     * Method used to save an image on the disk from a bitmap and to a path
     * @param bitmap, bitmap downloaded with the method dowmloadBitmap()
     * @param path, path where the file has to be save
     * @return boolean
     */
    public boolean saveImage(Bitmap bitmap, String path) {
        OutputStream fOut = null;
        File file = new File(path); // the File to save to
        file.getParentFile().mkdirs();
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // saving the Bitmap to a file compressed as a PNG with 100% compression rate
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
            fOut.close(); // do not forget to close the stream
        } catch (IOException e) {
            e.printStackTrace();
        }
       return true;
    }

}
