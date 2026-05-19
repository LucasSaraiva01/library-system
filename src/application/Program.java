package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import entities.Book;
import entities.Loan;
import entities.User;
import service.CsvStorageService;
import service.LibraryLoanService;
import service.LoanService;
import service.StorageService;

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
				System.out.println("9. Listar empréstimos atrasados");
				System.out.println("10. Pesquisar livros");
				System.out.println("11. Histórico de empréstimos por usuário");  // NOVO
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
				
				case 10 -> {
				    System.out.println("\n=== PESQUISAR LIVROS ===");
				    System.out.println("1. Pesquisar por título");
				    System.out.println("2. Pesquisar por autor");
				    System.out.println("3. Pesquisa geral (título ou autor)");
				    System.out.print("Opção: ");
				    int searchOption = sc.nextInt();
				    sc.nextLine();
				    
				    List<Book> results = new ArrayList<>();
				    String searchTerm = "";
				    
				    switch (searchOption) {
				        case 1 -> {
				            System.out.print("Digite o título (ou parte dele): ");
				            searchTerm = sc.nextLine();
				            results = loanService.searchBooksByTitle(searchTerm);
				        }
				        case 2 -> {
				            System.out.print("Digite o autor (ou parte dele): ");
				            searchTerm = sc.nextLine();
				            results = loanService.searchBooksByAuthor(searchTerm);
				        }
				        case 3 -> {
				            System.out.print("Digite a palavra-chave: ");
				            searchTerm = sc.nextLine();
				            results = loanService.searchBooks(searchTerm);
				        }
				        default -> {
				            System.out.println("Opção inválida!");
				            return;
				        }
				    }
				    
				    printSearchResults(results, searchTerm);
				}
				
				case 11 -> {
				    System.out.println("\n=== HISTÓRICO DE EMPRÉSTIMOS POR USUÁRIO ===");
				    
				    List<User> users = loanService.listAllUsers();
				    if (users.isEmpty()) {
				        System.out.println("Nenhum usuário cadastrado.");
				        break;
				    }
				    
				    System.out.println("\nUsuários cadastrados:");
				    users.forEach(u -> System.out.println("  ID: " + u.getId() + " - " + u.getName()));
				    
				    System.out.print("\nDigite o ID do usuário: ");
				    int userId = sc.nextInt();
				    sc.nextLine();
				    
				    User selectedUser = users.stream()
				            .filter(u -> u.getId().equals(userId))
				            .findFirst()
				            .orElse(null);
				    
				    if (selectedUser == null) {
				        System.out.println("Usuário não encontrado!");
				        break;
				    }
				    
				    // Submenu
				    System.out.println("\n1. Histórico completo");
				    System.out.println("2. Apenas empréstimos ativos");
				    System.out.print("Opção: ");
				    int historyOption = sc.nextInt();
				    sc.nextLine();
				    
				    List<Loan> loansToShow;
				    String title;
				    
				    if (historyOption == 2) {
				        loansToShow = loanService.getActiveLoansByUser(userId);
				        title = "EMPRÉSTIMOS ATIVOS";
				    } else {
				        loansToShow = loanService.getLoansByUser(userId);
				        title = "HISTÓRICO COMPLETO";
				    }
				    
				    System.out.println("\n📚 " + title + " - " + selectedUser.getName().toUpperCase());
				    System.out.println("=".repeat(60));
				    
				    if (loansToShow.isEmpty()) {
				        if (historyOption == 2) {
				            System.out.println("Este usuário não possui empréstimos ativos.");
				        } else {
				            System.out.println("Este usuário não realizou nenhum empréstimo.");
				        }
				    } else {
				        System.out.println("Total: " + loansToShow.size() + " empréstimo(s)");
				        System.out.println();
				        
				        for (Loan loan : loansToShow) {
				            System.out.printf("  📖 %s (ID: %d)%n", loan.getBook().getTitle(), loan.getId());
				            System.out.printf("     Data: %s%n",
				                loan.getLoanDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				            
				            if (loan.getReturnDate() == null) {
				                System.out.printf("     Devolução prevista: %s%n",
				                    loan.getExpectedReturnDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				                if (loan.isOverdue()) {
				                    System.out.printf("     ⚠️ ATRASADO! Dias: %d%n", loan.getDaysOverdue());
				                }
				            } else {
				                System.out.printf("     Devolvido em: %s%n",
				                    loan.getReturnDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				            }
				            System.out.println();
				        }
				    }
				    System.out.println("=".repeat(60));
				}
				

				case 0 -> System.out.println("Encerrando...");

				default -> System.out.println("Opção inválida!");
				}
			}
		}
	}
	
	private static void printSearchResults(List<Book> results, String searchTerm) {
	    if (results.isEmpty()) {
	        System.out.println("Nenhum livro encontrado.");
	        return;
	    }
	    
	    System.out.println("\n🔍 Resultados da busca por: \"" + searchTerm + "\"");
	    System.out.println("=".repeat(50));
	    
	    for (int i = 0; i < results.size(); i++) {
	        Book book = results.get(i);
	        System.out.printf("%d. [%d] %s - %s (%s)%n",
	            i + 1,
	            book.getId(),
	            book.getTitle(),
	            book.getAuthor(),
	            book.isAvailable() ? "Disponível" : "Indisponível"
	        );
	    }
	    System.out.println("=".repeat(50));
	}
}
