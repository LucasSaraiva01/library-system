package service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entities.Book;
import entities.Loan;
import entities.User;
import utils.EmailValidator;

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

		// Verifica se já existe um livro com o mesmo ID
		boolean exists = books.stream().anyMatch(b -> b.getId().equals(book.getId()));

		if (exists) {
			System.out.println("ERRO: Já existe um livro com o ID " + book.getId() + "!");
			return;
		}

		books.add(book);
		System.out.println("Livro adicionado com sucesso!");

	}

	@Override
	public void addUser(User user) {
		// Verifica se já existe um usuário com o mesmo ID
		boolean idExists = users.stream().anyMatch(u -> u.getId().equals(user.getId()));

		if (idExists) {
			System.out.println("ERRO: Já existe um usuário com o ID " + user.getId() + "!");
			return;
		}

		// Verifica se já existe um usuário com o mesmo email
		boolean emailExists = users.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(user.getEmail()));

		if (emailExists) {
			System.out.println("ERRO: Já existe um usuário com o email " + user.getEmail() + "!");
			return;
		}

		// Valida o formato do email
		if (!EmailValidator.isValid(user.getEmail())) {
			System.out.println("ERRO: Email inválido! O email deve conter @ e um domínio válido.");
			System.out.println("      Exemplo: usuario@dominio.com");
			return;
		}

		users.add(user);
		System.out.println("Usuário adicionado com sucesso!");
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
	    
	    // Gera ID baseado no maior ID existente + 1
	    int maxId = loans.stream()
	            .mapToInt(Loan::getId)
	            .max()
	            .orElse(0);
	    int loanId = maxId + 1;
	    
	    loans.add(new Loan(loanId, book, user, LocalDate.now()));
	    System.out.println("Empréstimo realizado com sucesso!");
	}

	@Override
	public void returnBook(Integer loanId) {

		Loan loan = loans.stream().filter(l -> l.getId().equals(loanId)).findFirst().orElse(null);

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
		List<Book> available = books.stream().filter(Book::isAvailable).collect(java.util.stream.Collectors.toList());
		Collections.sort(available);
		return available;
	}

	@Override
	public List<Loan> listAllLoans() {
		return loans;
	}
	
	@Override
	public List<Book> listAllBooks() {
	    return books;
	}
	
	@Override
	public List<User> listAllUsers() {
	    return users;
	}
	
	@Override
	public void addLoan(Loan loan) {
	    loans.add(loan);
	}
	
	@Override
	public List<Loan> listOverdueLoans() {
	    return loans.stream()
	            .filter(Loan::isOverdue)
	            .collect(java.util.stream.Collectors.toList());
	}
}