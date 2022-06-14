package sample;

public class SortableBook extends Book {
    int hashValue;
    public SortableBook(int id_ksiazka, String isbn, int id_kategoria, String tytul, String opis, int id_autor, int id_wydawnictwo, int hashValue) {
        super(id_ksiazka, isbn, id_kategoria, tytul, opis, id_autor, id_wydawnictwo);
        this.hashValue = hashValue;
    }

    public SortableBook(Book book, int hashValue) {
        super(book.getId_ksiazka(), book.getIsbn(), book.getId_kategoria(), book.getTytul(), book.getOpis(), book.getId_autor(), book.getId_wydawnictwo());
        this.hashValue = hashValue;
    }

    public int getHashValue() {
        return hashValue;
    }

    public void setHashValue(int hashValue) {
        this.hashValue = hashValue;
    }

    @Override
    public String toString() {
        return "tytul='" + tytul + '\'' +
                ", opis='" + opis  + '\'';

    }


}
