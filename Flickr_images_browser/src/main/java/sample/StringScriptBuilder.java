package sample;

import java.util.ArrayList;

public class StringScriptBuilder {

    public StringScriptBuilder()
    {

    }

    public String simpleGaleryHtmlFromArrayListOfUrls(ArrayList<String> searchedPhotosUrlAdreses)
    {
        String tekst_wynik = "";
        String naglowek = "";
        String koniec = "";

        naglowek = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Sample</title>\n" +
                "    <style>\n" +
                "        * {\n" +
                "            box-sizing: border-box;\n" +
                "        }\n" +
                "\n" +
                "        body {\n" +
                "            margin: 0;\n" +
                "            font-family: Arial;\n" +
                "        }\n" +
                "\n" +
                "        /* The grid: Four equal columns that floats next to each other */\n" +
                "        .column {\n" +
                "            float: left;\n" +
                "            width: 25%;\n" +
                "            padding: 10px;\n" +
                "        }\n" +
                "\n" +
                "        /* Style the images inside the grid */\n" +
                "        .column img {\n" +
                "            opacity: 0.8;\n" +
                "            cursor: pointer;\n" +
                "        }\n" +
                "\n" +
                "        .column img:hover {\n" +
                "            opacity: 1;\n" +
                "        }\n" +
                "\n" +
                "        /* Clear floats after the columns */\n" +
                "        .row:after {\n" +
                "            content: \"\";\n" +
                "            display: table;\n" +
                "            clear: both;\n" +
                "        }\n" +
                "\n" +
                "        /* The expanding image container */\n" +
                "        .container {\n" +
                "            position: relative;\n" +
                "            display: none;\n" +
                "        }\n" +
                "\n" +
                "        /* Expanding image text */\n" +
                "        #imgtext {\n" +
                "            position: absolute;\n" +
                "            bottom: 15px;\n" +
                "            left: 15px;\n" +
                "            color: white;\n" +
                "            font-size: 20px;\n" +
                "        }\n" +
                "\n" +
                "        /* Closable button inside the expanded image */\n" +
                "        .closebtn {\n" +
                "            position: absolute;\n" +
                "            top: 10px;\n" +
                "            right: 15px;\n" +
                "            color: white;\n" +
                "            font-size: 35px;\n" +
                "            cursor: pointer;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<main>\n" +
                "    <div class=\"container\">\n" +
                "        <span onclick=\"this.parentElement.style.display='none'\" class=\"closebtn\">&times;</span>\n" +
                "        <img id=\"expandedImg\" style=\"width:100%\">\n" +
                "        <div id=\"imgtext\"></div>\n" +
                "    </div>\n" +
                "\n" +
                "\n" +
                "    <div class=\"row\">";

        koniec = "    </div>\n" +
                "\n" +
                "</main>\n" +
                "\n" +
                "<script type=\"text/javascript\">\n" +
                "\n" +
                "\n" +
                "    function myFunction(imgs) {\n" +
                "        var expandImg = document.getElementById(\"expandedImg\");\n" +
                "        var imgText = document.getElementById(\"imgtext\");\n" +
                "        expandImg.src = imgs.src;\n" +
                "        imgText.innerHTML = imgs.alt;\n" +
                "        expandImg.parentElement.style.display = \"block\";\n" +
                "\t\tjavaConnector.takeUrlAdresOfPhoto(imgs.src);\n" +
                "        \n" +
                "    }\n" +
                "\n" +
                "\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";


//                "        javaConnector.takeNumberOfPhoto(imgs.alt);\n" +

        tekst_wynik = tekst_wynik + naglowek;
        for(int i = 0; i < searchedPhotosUrlAdreses.size(); ++i)
        {
            tekst_wynik = tekst_wynik + "<div class=\"column\">\n" +
                    "        <img src=\"" + searchedPhotosUrlAdreses.get(i) + "\" alt=\"" + String.valueOf(i) + "\" style=\"width:100%\" onclick=\"myFunction(this);\">\n" +
                    "    </div>";
        }

        tekst_wynik = tekst_wynik + koniec;
        return tekst_wynik;
    }


    public String simpleGaleryHtmlFromArrayListOfUrls_2(ArrayList<String> searchedPhotosUrlAdreses)
    {
        String tekst_wynik = "";
        String naglowek = "";
        String koniec = "";

        naglowek = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Sample</title>\n" +
                "    <style>\n" +
                "        * {\n" +
                "            box-sizing: border-box;\n" +
                "        }\n" +
                "\n" +
                "        body {\n" +
                "            margin: 0;\n" +
                "            font-family: Arial;\n" +
                "        }\n" +
                "\n" +
                "        /* The grid: Four equal columns that floats next to each other */\n" +
                "        .column {\n" +
                "            float: left;\n" +
                "            width: 25%;\n" +
                "            padding: 10px;\n" +
                "        }\n" +
                "\n" +
                "        /* Style the images inside the grid */\n" +
                "        .column img {\n" +
                "            opacity: 0.8;\n" +
                "            cursor: pointer;\n" +
                "        }\n" +
                "\n" +
                "        .column img:hover {\n" +
                "            opacity: 1;\n" +
                "        }\n" +
                "\n" +
                "        /* Clear floats after the columns */\n" +
                "        .row:after {\n" +
                "            content: \"\";\n" +
                "            display: table;\n" +
                "            clear: both;\n" +
                "        }\n" +
                "\n" +
                "        /* The expanding image container */\n" +
                "        .container {\n" +
                "            position: relative;\n" +
                "            display: none;\n" +
                "        }\n" +
                "\n" +
                "        /* Expanding image text */\n" +
                "        #imgtext {\n" +
                "            position: absolute;\n" +
                "            bottom: 15px;\n" +
                "            left: 15px;\n" +
                "            color: white;\n" +
                "            font-size: 20px;\n" +
                "        }\n" +
                "\n" +
                "        /* Closable button inside the expanded image */\n" +
                "        .closebtn {\n" +
                "            position: absolute;\n" +
                "            top: 10px;\n" +
                "            right: 15px;\n" +
                "            color: white;\n" +
                "            font-size: 35px;\n" +
                "            cursor: pointer;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<main>\n" +
                "    <div class=\"container\">\n" +
                "        <span onclick=\"this.parentElement.style.display='none'\" class=\"closebtn\">&times;</span>\n" +
                "        <img id=\"expandedImg\" style=\"width:100%\">\n" +
                "        <div id=\"imgtext\"></div>\n" +
                "    </div>\n" +
                "\n" +
                "\n" +
                "    <div class=\"row\">";

        koniec = "    </div>\n" +
                "\n" +
                "</main>\n" +
                "\n" +
                "<script type=\"text/javascript\">\n" +
                "\n" +
                "\n" +
                "    function myFunction(imgs) {\n" +
                "        javaConnector.deletePhotoFromArrays(imgs.alt);\n" +
                "        \n" +
                "    }\n" +
                "\n" +
                "\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";




        tekst_wynik = tekst_wynik + naglowek;
        for(int i = 0; i < searchedPhotosUrlAdreses.size(); ++i)
        {
            tekst_wynik = tekst_wynik + "<div class=\"column\">\n" +
                    "        <img src=\"" + searchedPhotosUrlAdreses.get(i) + "\" alt=\"" + String.valueOf(i) + "\" style=\"width:100%\" onclick=\"myFunction(this);\">\n" +
                    "    </div>";
        }

        tekst_wynik = tekst_wynik + koniec;
        return tekst_wynik;
    }
}
