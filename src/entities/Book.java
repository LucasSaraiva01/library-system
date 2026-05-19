package entities;

public class Book implements Comparable<Book> {

    private Integer id;
    private String title;
    private String author;
    private String genre;  // NOVO
    private boolean available;

    // Construtor modificado
    public Book(Integer id, String title, String author, String genre, boolean available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.available = available;
    }

    public Integer getId()          { return id; }
    public String getTitle()        { return title; }
    public String getAuthor()       { return author; }
    public String getGenre()        { return genre; }  // NOVO
    public boolean isAvailable()    { return available; }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public int compareTo(Book other) {
        return this.getTitle().compareTo(other.getTitle());
    }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s (%s) [%s]",
            id,
            title,
            author,
            available ? "Disponível" : "Indisponível",
            genre != null ? genre : "Sem gênero"
        );
    }
}