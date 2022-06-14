package projektTrzy;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class disposableAlarm extends Alarm {

    @Override
    public String toString() {
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");

        return "Alarm niecykliczny: " +
                "data: " + dateFormater.format(date) +
                ", nazwaDzwonka: '" + nazwaDzwonka.getName() + '\'' +
                ", glosnoscDzwonka: " + glosnoscDzwonka;
    }


    public disposableAlarm(Date date, File nazwaDzwonka, int glosnoscDzwonka) {
        super(date, nazwaDzwonka, glosnoscDzwonka);
    }


    @Override
    public void terminate() {
    }

    @Override
    public void run() {
    }
}
