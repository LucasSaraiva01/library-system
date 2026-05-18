package service;

import entities.Book;
import entities.Loan;
import entities.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvStorageService implements StorageService {

    private final Path booksPath = Path.of("books.csv");
    private final Path loansPath = Path.of("loans.csv");

    @Override
    public void saveBooks(List<Book> books) {
        List<String> lines = books.stream()
                .map(b -> b.getId() + "," + b.getTitle() + "," + b.getAuthor() + "," + b.isAvailable())
                .toList();
        try {
            Files.write(booksPath, lines);
        } catch (IOException e) {
            System.out.println("Erro ao salvar livros: " + e.getMessage());
        }
    }

    @Override
    public void saveLoans(List<Loan> loans) {
        List<String> lines = loans.stream()
                .map(l -> l.getId() + "," +
                          l.getBook().getId() + "," +
                          l.getUser().getId() + "," +
                          l.getLoanDate() + "," +
                          (l.getReturnDate() != null ? l.getReturnDate() : "null"))
                .toList();
        try {
            Files.write(loansPath, lines);
        } catch (IOException e) {
            System.out.println("Erro ao salvar empréstimos: " + e.getMessage());
        }
    }

    @Override
    public List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();
        if (!Files.exists(booksPath)) return books;
        try {
            Files.lines(booksPath).forEach(line -> {
                String[] fields = line.split(",");
                books.add(new Book(
                        Integer.parseInt(fields[0]),
                        fields[1],
                        fields[2],
                        Boolean.parseBoolean(fields[3])
                ));
            });
        } catch (IOException e) {
            System.out.println("Erro ao carregar livros: " + e.getMessage());
        }
        return books;
    }

    @Override
    public List<Loan> loadLoans() {
        return new ArrayList<>();
    }
}