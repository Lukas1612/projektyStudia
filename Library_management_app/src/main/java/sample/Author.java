package sample;

public class Author {
    String imie;
    String nazwisko;
    int ID;

    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public int getID() {
        return ID;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "imie='" + imie + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ", ID=" + ID;
    }

    public Author(String imie, String nazwisko, int ID) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.ID = ID;
    }
}
