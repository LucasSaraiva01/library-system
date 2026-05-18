package service;

import java.util.List;

import entities.Book;
import entities.Loan;
import entities.User;

public interface LoanService {

	void addBook(Book book);
	void addUser(User user);
    void borrowBook(Integer bookId, Integer userId);
    void returnBook(Integer loanId);
    List<Book> listAvailableBooks();
    List<Loan> listAllLoans();
}
