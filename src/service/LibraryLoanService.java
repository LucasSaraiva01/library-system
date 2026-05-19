package service;

import entities.Book;
import entities.Loan;
import entities.User;
import exceptions.*;
import utils.EmailValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
		boolean exists = books.stream().anyMatch(b -> b.getId().equals(book.getId()));

		if (exists) {
			throw new DuplicateEntityException("Livro", book.getId());
		}

		books.add(book);
	}

	@Override
	public void addUser(User user) {
		boolean idExists = users.stream().anyMatch(u -> u.getId().equals(user.getId()));

		if (idExists) {
			throw new DuplicateEntityException("Usuário", user.getId());
		}

		boolean emailExists = users.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(user.getEmail()));

		if (emailExists) {
			throw new DuplicateEntityException("Usuário", user.getEmail());
		}

		if (!EmailValidator.isValid(user.getEmail())) {
			throw new InvalidEmailException(user.getEmail());
		}

		users.add(user);
	}

	@Override
	public void addLoan(Loan loan) {
		loans.add(loan);
	}

	@Override
	public void borrowBook(Integer bookId, Integer userId) {
		Book book = books.stream().filter(b -> b.getId().equals(bookId)).findFirst()
				.orElseThrow(() -> new BookNotFoundException(bookId));

		User user = users.stream().filter(u -> u.getId().equals(userId)).findFirst()
				.orElseThrow(() -> new UserNotFoundException(userId));

		if (!book.isAvailable()) {
			throw new BookNotAvailableException(book.getTitle());
		}

		book.setAvailable(false);

		int maxId = loans.stream().mapToInt(Loan::getId).max().orElse(0);
		int loanId = maxId + 1;

		loans.add(new Loan(loanId, book, user, LocalDate.now()));
	}

	@Override
	public void returnBook(Integer loanId) {
		Loan loan = loans.stream().filter(l -> l.getId().equals(loanId)).findFirst()
				.orElseThrow(() -> new LoanNotFoundException(loanId));

		if (loan.getReturnDate() != null) {
			throw new LoanAlreadyReturnedException(loanId);
		}

		loan.getBook().setAvailable(true);
		loan.setReturnDate(LocalDate.now());
	}

	@Override
	public List<Book> listAvailableBooks() {
		List<Book> available = books.stream().filter(Book::isAvailable).toList();
		Collections.sort(available);
		return available;
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
	public List<Loan> listAllLoans() {
		return loans;
	}

	@Override
	public List<Loan> listOverdueLoans() {
		return loans.stream().filter(Loan::isOverdue).toList();
	}

	@Override
	public List<Book> searchBooksByTitle(String title) {
		if (title == null || title.isBlank()) {
			return new ArrayList<>();
		}

		String searchTerm = title.toLowerCase().trim();
		return books.stream().filter(book -> book.getTitle().toLowerCase().contains(searchTerm)).toList();
	}

	@Override
	public List<Book> searchBooksByAuthor(String author) {
		if (author == null || author.isBlank()) {
			return new ArrayList<>();
		}

		String searchTerm = author.toLowerCase().trim();
		return books.stream().filter(book -> book.getAuthor().toLowerCase().contains(searchTerm)).toList();
	}

	@Override
	public List<Book> searchBooks(String keyword) {
		if (keyword == null || keyword.isBlank()) {
			return new ArrayList<>();
		}

		String searchTerm = keyword.toLowerCase().trim();
		List<Book> results = books.stream().filter(book -> book.getTitle().toLowerCase().contains(searchTerm)
				|| book.getAuthor().toLowerCase().contains(searchTerm)).toList();

		Collections.sort(results);
		return results;
	}

	@Override
	public List<Loan> getLoansByUser(Integer userId) {
		if (userId == null) {
			return new ArrayList<>();
		}

		return loans.stream().filter(loan -> loan.getUser().getId().equals(userId)).toList();
	}

	@Override
	public List<Loan> getActiveLoansByUser(Integer userId) {
		if (userId == null) {
			return new ArrayList<>();
		}

		return loans.stream().filter(loan -> loan.getUser().getId().equals(userId))
				.filter(loan -> loan.getReturnDate() == null).toList();
	}
}