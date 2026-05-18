package service;

import entities.Book;
import entities.Loan;
import entities.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LibraryLoanService implements LoanService {

    private List<Book> books = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Loan> loans = new ArrayList<>();
    private final StorageService storageService;

    public LibraryLoanService(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public void addBook(Book book) {
        books.add(book);
    }

    @Override
    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public void borrowBook(Integer bookId, Integer userId) {

        Book book = books.stream()
                .filter(b -> b.getId().equals(bookId))
                .findFirst()
                .orElse(null);

        User user = users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElse(null);

        if (book == null) {
            System.out.println("Livro não encontrado!");
            return;
        }
        if (user == null) {
            System.out.println("Usuário não encontrado!");
            return;
        }
        if (!book.isAvailable()) {
            System.out.println("Livro não está disponível!");
            return;
        }

        book.setAvailable(false);
        int loanId = loans.size() + 1;
        loans.add(new Loan(loanId, book, user, LocalDate.now()));
        System.out.println("Empréstimo realizado com sucesso!");
    }

    @Override
    public void returnBook(Integer loanId) {

        Loan loan = loans.stream()
                .filter(l -> l.getId().equals(loanId))
                .findFirst()
                .orElse(null);

        if (loan == null) {
            System.out.println("Empréstimo não encontrado!");
            return;
        }
        if (loan.getReturnDate() != null) {
            System.out.println("Livro já foi devolvido!");
            return;
        }

        loan.getBook().setAvailable(true);
        loan.setReturnDate(LocalDate.now());
        System.out.println("Devolução realizada com sucesso!");
    }

    @Override
    public List<Book> listAvailableBooks() {
        List<Book> available = books.stream()
                .filter(Book::isAvailable)
                .collect(java.util.stream.Collectors.toList());
        Collections.sort(available);
        return available;
    }

    @Override
    public List<Loan> listAllLoans() {
        return loans;
    }
}