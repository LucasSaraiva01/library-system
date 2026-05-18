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

		// Carrega livros salvos anteriormente
		List<Book> loadedBooks = storageService.loadBooks();
		List<User> loadedUsers = storageService.loadUsers();

		// Cria o serviço com os dados carregados
		LoanService loanService = new LibraryLoanService(storageService);

		// Adiciona livros ao serviço
		if (loadedBooks.isEmpty()) {
			System.out.println("Nenhum livro encontrado. Adicionando livros de exemplo...");
			loanService.addBook(new Book(1, "O Senhor dos Anéis", "J.R.R. Tolkien", true));
			loanService.addBook(new Book(2, "1984", "George Orwell", true));
			loanService.addBook(new Book(3, "Dom Casmurro", "Machado de Assis", true));
			loanService.addBook(new Book(4, "O Pequeno Príncipe", "Antoine de Saint-Exupéry", true));
			loanService.addBook(new Book(5, "A Culpa é das Estrelas", "John Green", true));
			storageService.saveBooks(loanService.listAllBooks());
			System.out.println("5 livros de exemplo adicionados com sucesso!");
		} else {
			loadedBooks.forEach(loanService::addBook);
			System.out.println("Carregados " + loadedBooks.size() + " livros do arquivo.");
		}

		// Adiciona usuários ao serviço
		if (loadedUsers.isEmpty()) {
			System.out.println("Nenhum usuário encontrado. Adicionando usuários de exemplo...");
			loanService.addUser(new User(1, "João Silva", "joao@email.com"));
			loanService.addUser(new User(2, "Maria Santos", "maria@email.com"));
			loanService.addUser(new User(3, "Pedro Oliveira", "pedro@email.com"));
			storageService.saveUsers(loanService.listAllUsers());
			System.out.println("3 usuários de exemplo adicionados com sucesso!");
		} else {
			loadedUsers.forEach(loanService::addUser);
			System.out.println("Carregados " + loadedUsers.size() + " usuários do arquivo.");
		}

		// Carrega empréstimos salvos (precisa das listas já carregadas)
		List<Loan> loadedLoans = storageService.loadLoans(loanService.listAllBooks(), loanService.listAllUsers());
		if (loadedLoans.isEmpty()) {
			System.out.println("Nenhum empréstimo encontrado.");
		} else {
			for (Loan loan : loadedLoans) {
				loanService.addLoan(loan);
			}
			System.out.println("Carregados " + loadedLoans.size() + " empréstimos do arquivo.");
		}

		try (Scanner sc = new Scanner(System.in)) {
			int option = -1;

			while (option != 0) {

				System.out.println("\n=== SISTEMA DE BIBLIOTECA ===");
				System.out.println("1. Cadastrar livro");
				System.out.println("2. Cadastrar usuário");
				System.out.println("3. Realizar empréstimo");
				System.out.println("4. Realizar devolução");
				System.out.println("5. Listar livros disponíveis");
				System.out.println("6. Listar todos os empréstimos");
				System.out.println("7. Listar todos os livros");
				System.out.println("8. Listar todos os usuários");
				System.out.println("9. Listar empréstimos atrasados"); // NOVO
				System.out.println("0. Sair");
				System.out.print("Opção: ");
				option = sc.nextInt();
				sc.nextLine();

				switch (option) {

				case 1 -> {
					System.out.print("ID do livro: ");
					int id = sc.nextInt();
					sc.nextLine();
					System.out.print("Título: ");
					String title = sc.nextLine();
					System.out.print("Autor: ");
					String author = sc.nextLine();
					loanService.addBook(new Book(id, title, author, true));
					storageService.saveBooks(loanService.listAllBooks());
					System.out.println("Livro cadastrado com sucesso!");
				}

				case 2 -> {
					System.out.print("ID do usuário: ");
					int id = sc.nextInt();
					sc.nextLine();
					System.out.print("Nome: ");
					String name = sc.nextLine();
					System.out.print("Email: ");
					String email = sc.nextLine();
					loanService.addUser(new User(id, name, email));
					storageService.saveUsers(loanService.listAllUsers()); // NOVO: salva após cadastrar
				}

				case 3 -> {
					System.out.print("ID do livro: ");
					int bookId = sc.nextInt();
					System.out.print("ID do usuário: ");
					int userId = sc.nextInt();
					sc.nextLine();
					loanService.borrowBook(bookId, userId);
					storageService.saveBooks(loanService.listAllBooks());
					storageService.saveLoans(loanService.listAllLoans());
				}

				case 4 -> {
					System.out.print("ID do empréstimo: ");
					int loanId = sc.nextInt();
					sc.nextLine();
					loanService.returnBook(loanId);
					storageService.saveBooks(loanService.listAllBooks());
					storageService.saveLoans(loanService.listAllLoans());
				}

				case 5 -> {
					List<Book> available = loanService.listAvailableBooks();
					if (available.isEmpty()) {
						System.out.println("Nenhum livro disponível.");
					} else {
						System.out.println("\n=== LIVROS DISPONÍVEIS ===");
						available.forEach(System.out::println);
					}
				}

				case 6 -> {
					List<Loan> loans = loanService.listAllLoans();
					if (loans.isEmpty()) {
						System.out.println("Nenhum empréstimo registrado.");
					} else {
						System.out.println("\n=== EMPRÉSTIMOS ===");
						loans.forEach(System.out::println);
					}
				}

				case 7 -> {
					List<Book> allBooks = loanService.listAllBooks();
					if (allBooks.isEmpty()) {
						System.out.println("Nenhum livro cadastrado.");
					} else {
						System.out.println("\n=== TODOS OS LIVROS ===");
						allBooks.forEach(System.out::println);
					}
				}

				case 8 -> {
					List<User> allUsers = loanService.listAllUsers();
					if (allUsers.isEmpty()) {
						System.out.println("Nenhum usuário cadastrado.");
					} else {
						System.out.println("\n=== TODOS OS USUÁRIOS ===");
						allUsers.forEach(System.out::println);
					}
				}

				case 9 -> {
				    List<Loan> overdueLoans = loanService.listOverdueLoans();
				    if (overdueLoans.isEmpty()) {
				        System.out.println("\n✅ Nenhum empréstimo atrasado!");
				    } else {
				        System.out.println("\n=== EMPRÉSTIMOS ATRASADOS ===");
				        overdueLoans.forEach(System.out::println);
				    }
				}

				case 0 -> System.out.println("Encerrando...");

				default -> System.out.println("Opção inválida!");
				}
			}
		}
	}
}
