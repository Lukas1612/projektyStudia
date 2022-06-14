package sample;

import java.util.Arrays;

public class SizesList {
    int canblog;
    int canprint;
    int candownload;
    Size[] size;

    public  SizesList()
    {

    }
    public SizesList(int canblog, int canprint, int candownload, String stat, Size[] size) {
        this.canblog = canblog;
        this.canprint = canprint;
        this.candownload = candownload;
        this.size = size;
    }

    public int getCanblog() {
        return canblog;
    }

    public int getCanprint() {
        return canprint;
    }

    public int getCandownload() {
        return candownload;
    }

    public Size[] getSize() {
        return size;
    }

    public void setCanblog(int canblog) {
        this.canblog = canblog;
    }

    public void setCanprint(int canprint) {
        this.canprint = canprint;
    }

    public void setCandownload(int candownload) {
        this.candownload = candownload;
    }

    public void setSize(Size[] size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "SizesList{" +
                "canblog=" + canblog +
                ", canprint=" + canprint +
                ", candownload=" + candownload +
                ", size=" + Arrays.toString(size) +
                '}';
    }
}
