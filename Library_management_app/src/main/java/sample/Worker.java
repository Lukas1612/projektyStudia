package sample;

public class Worker {

    int ID;
    String login;
    String haslo;

    public Worker(int ID, String login, String haslo) {
        this.ID = ID;
        this.login = login;
        this.haslo = haslo;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public int getID() {
        return ID;
    }

    public String getLogin() {
        return login;
    }

    public String getHaslo() {
        return haslo;
    }

    @Override
    public String toString() {
        return "ID=" + ID +
                ", login='" + login + '\'' +
                ", haslo='" + haslo + '\'';
    }
}
