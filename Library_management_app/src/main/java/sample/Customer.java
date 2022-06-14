package sample;

public class Customer {

    int ID;
    String login;
    String haslo;

    public Customer(int ID, String login, String haslo) {
        this.ID = ID;
        this.login = login;
        this.haslo = haslo;
    }

    @Override
    public String toString() {
        return "ID=" + ID +
                ", login='" + login + '\'' +
                ", haslo='" + haslo;
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

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }
}
