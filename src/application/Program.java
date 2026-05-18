package application;

import entities.Book;
import entities.Loan;
import entities.User;
import service.CsvStorageService;
import service.LibraryLoanService;
import service.LoanService;
import service.StorageService;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) {

        Locale.setDefault(Locale.US);

        StorageService storageService = new CsvStorageService();
        LoanService loanService = new LibraryLoanService(storageService);

        // carrega livros salvos anteriormente
        storageService.loadBooks().forEach(loanService::addBook);

        // usuários fixos por enquanto
        loanService.addUser(new User(1, "João Silva", "joao@email.com"));
        loanService.addUser(new User(2, "Maria Santos", "maria@email.com"));

        try (Scanner sc = new Scanner(System.in)) {

            int option = -1;

            while (option != 0) {

                System.out.println("\n=== SISTEMA DE BIBLIOTECA ===");
                System.out.println("1. Cadastrar livro");
                System.out.println("2. Realizar empréstimo");
                System.out.println("3. Realizar devolução");
                System.out.println("4. Listar livros disponíveis");
                System.out.println("5. Listar todos os empréstimos");
                System.out.println("0. Sair");
                System.out.print("Opção: ");
                option = sc.nextInt();
                sc.nextLine();

                switch (option) {

                    case 1 -> {
                        System.out.print("ID: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Título: ");
                        String title = sc.nextLine();
                        System.out.print("Autor: ");
                        String author = sc.nextLine();
                        loanService.addBook(new Book(id, title, author, true));
                        storageService.saveBooks(loanService.listAvailableBooks());
                        System.out.println("Livro cadastrado com sucesso!");
                    }

                    case 2 -> {
                        System.out.print("ID do livro: ");
                        int bookId = sc.nextInt();
                        System.out.print("ID do usuário: ");
                        int userId = sc.nextInt();
                        sc.nextLine();
                        loanService.borrowBook(bookId, userId);
                        storageService.saveBooks(loanService.listAvailableBooks());
                        storageService.saveLoans(loanService.listAllLoans());
                    }

                    case 3 -> {
                        System.out.print("ID do empréstimo: ");
                        int loanId = sc.nextInt();
                        sc.nextLine();
                        loanService.returnBook(loanId);
                        storageService.saveBooks(loanService.listAvailableBooks());
                        storageService.saveLoans(loanService.listAllLoans());
                    }

                    case 4 -> {
                        List<Book> available = loanService.listAvailableBooks();
                        if (available.isEmpty()) {
                            System.out.println("Nenhum livro disponível.");
                        } else {
                            System.out.println("\n=== LIVROS DISPONÍVEIS ===");
                            available.forEach(System.out::println);
                        }
                    }

                    case 5 -> {
                        List<Loan> loans = loanService.listAllLoans();
                        if (loans.isEmpty()) {
                            System.out.println("Nenhum empréstimo registrado.");
                        } else {
                            System.out.println("\n=== EMPRÉSTIMOS ===");
                            loans.forEach(System.out::println);
                        }
                    }

                    case 0 -> System.out.println("Encerrando...");

                    default -> System.out.println("Opção inválida!");
                }
            }
        }
    }
}