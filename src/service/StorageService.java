package service;

import entities.Book;
import entities.Loan;

import java.util.List;

public interface StorageService {

    void saveBooks(List<Book> books);
    void saveLoans(List<Loan> loans);
    List<Book> loadBooks();
    List<Loan> loadLoans();
}