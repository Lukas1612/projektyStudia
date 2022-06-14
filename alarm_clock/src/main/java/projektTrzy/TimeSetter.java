package projektTrzy;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TimeSetter implements Runnable {

    TimeSetter(TextArea hours, TextArea minutes, TextArea seconds, TextArea date)
    {
        running = true;
        this.hoursArea = hours;
        this.minutesArea = minutes;
        this.secondsArea = seconds;
        this.dateArea = date;

    }
    private volatile boolean running = true;

    private Date date = null;

    private TextArea hoursArea = null;
    private TextArea minutesArea = null;
    private TextArea secondsArea = null;
    private TextArea dateArea = null;

    public void terminate()
    {
        running = false;
    }

    @Override
    public void run() {
        SimpleDateFormat hoursFormater = new SimpleDateFormat("HH");
        SimpleDateFormat minutesFormater = new SimpleDateFormat("mm");
        SimpleDateFormat secondsFormater = new SimpleDateFormat("ss");
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");

        while (running)
        {
            date = new Date(System.currentTimeMillis());

            if(date != null)
            {
                if(dateArea != null && dateFormater != null)
                {
                    dateArea.setText(dateFormater.format(date));
                }

                if(secondsArea != null && secondsFormater != null)
                {
                    secondsArea.setText(secondsFormater.format(date));
                }
                if(hoursArea != null && hoursFormater != null)
                {
                    hoursArea.setText(hoursFormater.format(date));
                }
                if(minutesArea != null && minutesFormater != null)
                {
                    minutesArea.setText(minutesFormater.format(date));
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // System.out.println("I'm still running");
        }

    }
}

