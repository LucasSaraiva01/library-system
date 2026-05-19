package exceptions;

public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(String bookTitle) {
        super("O livro \"" + bookTitle + "\" não está disponível para empréstimo.");
    }
}