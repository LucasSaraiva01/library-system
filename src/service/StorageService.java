package service;

import java.util.List;

import entities.Book;
import entities.Loan;
import entities.User;

public interface StorageService {

    void saveBooks(List<Book> books);
    void saveLoans(List<Loan> loans);
    void saveUsers(List<User> users);
    
    List<Book> loadBooks();
    List<Loan> loadLoans();
    List<User> loadUsers();
}