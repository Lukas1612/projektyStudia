package sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class SearchRunnable implements Runnable {

    String searchText = "";
    Boolean square = null;
    Boolean thumbnail = null;
    Boolean small = null;
    Boolean medium = null;
    Boolean large = null;
    Boolean x_large = null;
    String simpleGalleryScriptHtml = null;
    ProgressIndicator progressIndicator;


    ArrayList <Photo> photosArray = null;

    public SearchRunnable(ProgressIndicator progressIndicator, String searchText, Boolean square, Boolean thumbnail, Boolean small, Boolean medium, Boolean large) {
        this.searchText = searchText;
        this.square = square;
        this.thumbnail = thumbnail;
        this.small = small;
        this.medium = medium;
        this.large = large;
        this.x_large = x_large;
        this.progressIndicator = progressIndicator;
    }

    @Override
    public void run() {

        try {
            progressIndicator.setProgress(0.01);


            Photo[] photos = searchPhotos();
            ArrayList<String> urlAdresesOfSelectedPhotos = new ArrayList<String>();

            progressIndicator.setProgress(0.02);

            PhotosSelector photosSelector = new PhotosSelector(photos, progressIndicator);
            urlAdresesOfSelectedPhotos = photosSelector.getUrlsOfPhotosInChoosenSizes(square, thumbnail, small, medium, large, x_large);


            StringScriptBuilder stringScriptBuilder = new StringScriptBuilder();
            simpleGalleryScriptHtml = stringScriptBuilder.simpleGaleryHtmlFromArrayListOfUrls(urlAdresesOfSelectedPhotos);

            progressIndicator.setProgress(1);



            //System.out.println(" ------------->");
            //System.out.println(simpleGalleryScriptHtml);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public String getResult()
    {
            return simpleGalleryScriptHtml;
    }


    private Photo[] searchPhotos() throws IOException{

        String urlString = "https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=c96082722feedbd449fde3082eb123e2&text=" + searchText + "&per_page=100&format=json&nojsoncallback=1";


        URL url = new URL(urlString);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

        String resultString_JSON = br.readLine();
        resultString_JSON = resultString_JSON.replace("{\"photos\":", "");
        resultString_JSON = resultString_JSON.replace(", \"stat\": \"ok\" }", "");

        ObjectMapper objectMapper = new ObjectMapper();

        PhotosList photosList = objectMapper.readValue(resultString_JSON, PhotosList.class);
        Photo[] photos = photosList.getPhoto();

        return photos;

    }
}
