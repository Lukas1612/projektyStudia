package projektTrzy;

import java.io.File;
import java.util.Date;

public abstract class Alarm implements Runnable {


    protected Date date;
    protected File nazwaDzwonka;
    protected int glosnoscDzwonka;

    public Alarm(Alarm alarm)
    {
        this.date = alarm.date;
        this.nazwaDzwonka = alarm.nazwaDzwonka;
        this.glosnoscDzwonka = alarm.glosnoscDzwonka;
    };
    public Alarm(Date date,  File nazwaDzwonka, int glosnoscDzwonka)
    {
        this.date = date;
        this.nazwaDzwonka = nazwaDzwonka;
        this.glosnoscDzwonka = glosnoscDzwonka;
    };
    public Alarm(int year, int month, int day, int hrs, int min, File nazwaDzwonka, int glosnoscDzwonka)
    {
        this.date = new Date(year, month, day, hrs, min);
        this.nazwaDzwonka = nazwaDzwonka;
        this.glosnoscDzwonka = glosnoscDzwonka;
    };

    public Date getAlarmDate()
    {
        return this.date;
    };

    public int getGlosnoscDzwonka() {
        return glosnoscDzwonka;
    }

    public File getNazwaDzwonka() {
        return nazwaDzwonka;
    }

    public abstract void terminate();
}
