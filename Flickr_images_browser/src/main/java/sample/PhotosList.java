package sample;

import java.util.ArrayList;

public class PhotosList {

    private int page;
    private int pages;
    private int perpage;
    private int total;
    private Photo[] photo;


    public  PhotosList()
    {

    };

    public PhotosList(int page, int pages, int perpage, int total,  Photo[] photo, String stat) {
        this.page = page;
        this.pages = pages;
        this.perpage = perpage;
        this.total = total;
        this.photo = photo;
    }


    public void setPage(int page) {
        this.page = page;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setPhoto(Photo[] photo) {
        this.photo = photo;
    }

    public int getPage() {
        return page;
    }

    public int getPages() {
        return pages;
    }

    public int getPerpage() {
        return perpage;
    }

    public int getTotal() {
        return total;
    }

    public  Photo[] getPhoto() {
        return photo;
    }

    @Override
    public String toString() {


        String tmp = null;
        for(int i = 0; i < photo.length; i++)
        {
            tmp = tmp + photo[i].toString();
            tmp = tmp + '\'' + ",";
        }

        return "PhotosList{" +
                "page=" + page +
                ", pages=" + pages +
                ", perpage=" + perpage +
                ", total=" + total +
                ", photo=[" + '\'' +
                   tmp + "]" +
                 '\'' +"}" ;
    }
}
