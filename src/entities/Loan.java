package entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Loan {

    private Integer id;
    private Book book;
    private User user;
    private LocalDate loanDate;
    private LocalDate returnDate;

    public Loan(Integer id, Book book, User user, LocalDate loanDate) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.loanDate = loanDate;
        this.returnDate = null; // ainda não devolvido
    }

    public Integer getId()              { return id; }
    public Book getBook()               { return book; }
    public User getUser()               { return user; }
    public LocalDate getLoanDate()      { return loanDate; }
    public LocalDate getReturnDate()    { return returnDate; }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("[%d] %s | Usuário: %s | Empréstimo: %s | Devolução: %s",
            id,
            book.getTitle(),
            user.getName(),
            loanDate.format(fmt),
            returnDate != null ? returnDate.format(fmt) : "Pendente"
        );
    }
}