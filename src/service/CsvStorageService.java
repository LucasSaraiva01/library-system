package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entities.Book;
import entities.Loan;
import entities.User;

public class CsvStorageService implements StorageService {

    private final Path booksPath = Path.of("books.csv");
    private final Path loansPath = Path.of("loans.csv");
    private final Path usersPath = Path.of("users.csv");  // NOVO

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
    
 // NOVO: Salvar usuários
    @Override
    public void saveUsers(List<User> users) {
        List<String> lines = users.stream()
                .map(u -> u.getId() + "," + u.getName() + "," + u.getEmail())
                .toList();
        try {
            Files.write(usersPath, lines);
        } catch (IOException e) {
            System.out.println("Erro ao salvar usuários: " + e.getMessage());
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
    
 // NOVO: Carregar usuários
    @Override
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        if (!Files.exists(usersPath)) return users;
        try {
            Files.lines(usersPath).forEach(line -> {
                String[] fields = line.split(",");
                users.add(new User(
                        Integer.parseInt(fields[0]),
                        fields[1],
                        fields[2]
                ));
            });
        } catch (IOException e) {
            System.out.println("Erro ao carregar usuários: " + e.getMessage());
        }
        return users;
    }

    @Override
    public List<Loan> loadLoans(List<Book> books, List<User> users) {
        List<Loan> loans = new ArrayList<>();
        if (!Files.exists(loansPath)) return loans;
        
        try {
            Files.lines(loansPath).forEach(line -> {
                String[] fields = line.split(",");
                // fields[0] = loanId
                // fields[1] = bookId
                // fields[2] = userId
                // fields[3] = loanDate
                // fields[4] = returnDate (pode ser "null")
                
                int loanId = Integer.parseInt(fields[0]);
                int bookId = Integer.parseInt(fields[1]);
                int userId = Integer.parseInt(fields[2]);
                LocalDate loanDate = LocalDate.parse(fields[3]);
                
                // Busca o livro correspondente pelo ID
                Book book = books.stream()
                        .filter(b -> b.getId().equals(bookId))
                        .findFirst()
                        .orElse(null);
                
                // Busca o usuário correspondente pelo ID
                User user = users.stream()
                        .filter(u -> u.getId().equals(userId))
                        .findFirst()
                        .orElse(null);
                
                if (book != null && user != null) {
                    Loan loan = new Loan(loanId, book, user, loanDate);
                    
                    // Se tem data de devolução (não é "null"), preenche
                    if (!fields[4].equals("null")) {
                        LocalDate returnDate = LocalDate.parse(fields[4]);
                        loan.setReturnDate(returnDate);
                    }
                    
                    // Se o livro foi emprestado, marca como indisponível
                    if (loan.getReturnDate() == null) {
                        book.setAvailable(false);
                    }
                    
                    loans.add(loan);
                }
            });
        } catch (IOException e) {
            System.out.println("Erro ao carregar empréstimos: " + e.getMessage());
        }
        return loans;
    }
}