package sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressIndicator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PhotosSelector {

    Boolean square = null;
    Boolean thumbnail = null;
    Boolean small = null;
    Boolean medium = null;
    Boolean large = null;
    Boolean x_large = null;

    String url;

    Photo[] photos;
    ProgressIndicator progressIndicator;

    public PhotosSelector(Photo[] photos, ProgressIndicator progressIndicator) {
        this.photos = photos;
        this.progressIndicator = progressIndicator;
    }


    public ArrayList<String> getUrlsOfPhotosInChoosenSizes(Boolean square, Boolean thumbnail, Boolean small, Boolean medium, Boolean large, Boolean x_large) throws InterruptedException {

        ArrayList<String> searchedPhotosUrlAdreses = new ArrayList<String>();
        Photo photo;
        double progress = 0.02;
        progressIndicator.setProgress(progress);

        long start;
        long finish = 0;
        long fps = 33; //30 frames per second

        start = System.currentTimeMillis();
        for (int j = 0; j < photos.length; ++j) {

            if (progress < 0.96) {
                progress = (double) (j) / (double) (photos.length) + 0.02;

                finish = System.currentTimeMillis();

                if(finish-start>fps)
                {
                    progressIndicator.setProgress(progress);
                    start = System.currentTimeMillis();
                }

            }



            start = System.currentTimeMillis();

            photo = photos[j];


            //s 75x75
            if (square) {
                url = createUrlToPhotoInGivenSize(photo, 's');
                searchedPhotosUrlAdreses.add(url);
            }

            //t 100
            if (thumbnail) {
                url = createUrlToPhotoInGivenSize(photo, 't');
                searchedPhotosUrlAdreses.add(url);
            }

            //m 240
            if (small) {
                url = createUrlToPhotoInGivenSize(photo, 'm');
                searchedPhotosUrlAdreses.add(url);
            }

            //z 640
            if (medium) {
                url = createUrlToPhotoInGivenSize(photo, 'z');
                searchedPhotosUrlAdreses.add(url);
            }

            //b 1024
            if (large) {
                url = createUrlToPhotoInGivenSize(photo, 'b');
                searchedPhotosUrlAdreses.add(url);
            }

            finish = System.currentTimeMillis();

            /*try {
                Thread.sleep(100);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }*/

        }
        return searchedPhotosUrlAdreses;
    }

    String createUrlToPhotoInGivenSize(Photo photo, char size)
    {
        // https://farm  {farm-id}  .staticflickr.com/  {server-id} / {id} _ {secret} _ [mstzb] .jpg
        // https://farm  {farm-id}  .staticflickr.com/  {server-id} / {id} _ {secret} .jpg

        String url;

        if(size=='s' || size=='t' || size=='m' || size=='z' || size=='b')
        {
            url="https://farm" + String.valueOf(photo.getFarm()) + ".staticflickr.com/" + photo.getServer() + "/" + String.valueOf(photo.getId()) + "_" + photo.getSecret() + "_" + size + ".jpg";
        }else
        {
            url="https://farm" + String.valueOf(photo.getFarm()) + ".staticflickr.com/" + photo.getServer() + "/" + String.valueOf(photo.getId()) + "_" + photo.getSecret() + ".jpg";
        }
        return url;
    }
}
