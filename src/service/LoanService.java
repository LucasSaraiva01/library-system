package service;

import entities.Book;
import entities.Loan;
import entities.User;

import java.util.List;

public interface LoanService {

    void addBook(Book book);
    void addUser(User user);
    void addLoan(Loan loan);
    void borrowBook(Integer bookId, Integer userId);
    void returnBook(Integer loanId);
    List<Book> listAvailableBooks();
    List<Book> listAllBooks();
    List<User> listAllUsers();
    List<Loan> listAllLoans();
    List<Loan> listOverdueLoans();
    
    // MÉTODOS DE BUSCA
    List<Book> searchBooksByTitle(String title);
    List<Book> searchBooksByAuthor(String author);
    List<Book> searchBooks(String keyword);  // Busca em título E autor
    
    
    List<Loan> getLoansByUser(Integer userId);
    List<Loan> getActiveLoansByUser(Integer userId);  // Apenas empréstimos não devolvidos
    
 // Métodos para editar e excluir livros
    void updateBook(Integer id, String newTitle, String newAuthor, String newGenre);
    void deleteBook(Integer id);
}