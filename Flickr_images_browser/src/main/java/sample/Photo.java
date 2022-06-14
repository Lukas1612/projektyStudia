package sample;

public class Photo {
    private long id;
    private String owner;
    private String secret;
    private String server;
    private int farm;
    private String title;
    private boolean ispublic;
    private boolean isfriend;
    private boolean isfamily;


    public Photo()
    {

    }
    public Photo(long id, String owner, String secret, String server, int farm, String title, boolean ispublic, boolean isfriend, boolean isfamily) {
        this.id = id;
        this.owner = owner;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
        this.title = title;
        this.ispublic = ispublic;
        this.isfriend = isfriend;
        this.isfamily = isfamily;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIspublic(boolean ispublic) {
        this.ispublic = ispublic;
    }

    public void setIsfriend(boolean isfriend) {
        this.isfriend = isfriend;
    }

    public void setIsfamily(boolean isfamily) {
        this.isfamily = isfamily;
    }

    public long getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getSecret() {
        return secret;
    }

    public String getServer() {
        return server;
    }

    public int getFarm() {
        return farm;
    }

    public String getTitle() {
        return title;
    }

    public boolean isIspublic() {
        return ispublic;
    }

    public boolean isIsfriend() {
        return isfriend;
    }

    public boolean isIsfamily() {
        return isfamily;
    }

    @Override
    public String toString() {
        return "Photo{" +
                " id=" + id +
                ", owner='" + owner +
                ", secret='" + secret +
                ", server='" + server +
                ", farm=" + farm +
                ", title='" + title +
                ", ispublic=" + ispublic +
                ", isfriend=" + isfriend +
                ", isfamily=" + isfamily +
                '}';
    }
}
