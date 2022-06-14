package sample;

public class Book {

    int id_ksiazka;
    String isbn;
    int id_kategoria;
    String tytul;
    String opis;
    int id_autor;
    int id_wydawnictwo;

    public Book(int id_ksiazka, String isbn, int id_kategoria, String tytul, String opis, int id_autor, int id_wydawnictwo) {
        this.id_ksiazka = id_ksiazka;
        this.isbn = isbn;
        this.id_kategoria = id_kategoria;
        this.tytul = tytul;
        this.opis = opis;
        this.id_autor = id_autor;
        this.id_wydawnictwo = id_wydawnictwo;
    }

    public void setId_ksiazka(int id_ksiazka) {
        this.id_ksiazka = id_ksiazka;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setId_kategoria(int id_kategoria) {
        this.id_kategoria = id_kategoria;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public void setId_autor(int id_autor) {
        this.id_autor = id_autor;
    }

    public void setId_wydawnictwo(int id_wydawnictwo) {
        this.id_wydawnictwo = id_wydawnictwo;
    }

    public int getId_ksiazka() {
        return id_ksiazka;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getId_kategoria() {
        return id_kategoria;
    }

    public String getTytul() {
        return tytul;
    }

    public String getOpis() {
        return opis;
    }

    public int getId_autor() {
        return id_autor;
    }

    public int getId_wydawnictwo() {
        return id_wydawnictwo;
    }

    @Override
    public String toString() {
        return  "tytul='" + tytul + '\'' +
                ", opis='" + opis + '\'';
    }
}
