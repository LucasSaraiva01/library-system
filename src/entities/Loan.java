package entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Loan {

    private Integer id;
    private Book book;
    private User user;
    private LocalDate loanDate;
    private LocalDate expectedReturnDate;  // NOVO
    private LocalDate returnDate;

    public Loan(Integer id, Book book, User user, LocalDate loanDate) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.loanDate = loanDate;
        this.expectedReturnDate = loanDate.plusDays(7);  // 7 dias para devolver
        this.returnDate = null;
    }
    
    // NOVO: Construtor para carregar do CSV com data prevista
    public Loan(Integer id, Book book, User user, LocalDate loanDate, LocalDate expectedReturnDate, LocalDate returnDate) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.loanDate = loanDate;
        this.expectedReturnDate = expectedReturnDate;
        this.returnDate = returnDate;
    }

    public Integer getId() { return id; }
    public Book getBook() { return book; }
    public User getUser() { return user; }
    public LocalDate getLoanDate() { return loanDate; }
    public LocalDate getExpectedReturnDate() { return expectedReturnDate; }  // NOVO
    public LocalDate getReturnDate() { return returnDate; }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    
    // NOVO: Verificar se está atrasado
    public boolean isOverdue() {
        if (returnDate != null) {
            return false;  // Já foi devolvido
        }
        return LocalDate.now().isAfter(expectedReturnDate);
    }
    
    // NOVO: Calcular dias de atraso
    public long getDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(expectedReturnDate, LocalDate.now());
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder sb = new StringBuilder();
        
        sb.append(String.format("[%d] %s | Usuário: %s | Empréstimo: %s | Devolução Prevista: %s",
            id,
            book.getTitle(),
            user.getName(),
            loanDate.format(fmt),
            expectedReturnDate.format(fmt)
        ));
        
        if (returnDate != null) {
            sb.append(String.format(" | Devolvido: %s", returnDate.format(fmt)));
        } else if (isOverdue()) {
            sb.append(String.format(" | ⚠️ ATRASADO! (%d dias)", getDaysOverdue()));
        } else {
            sb.append(" | Pendente");
        }
        
        return sb.toString();
    }
}