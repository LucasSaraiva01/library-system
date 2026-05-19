package exceptions;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Integer bookId) {
        super("Livro com ID " + bookId + " não encontrado.");
    }
}