# 📚 Library Management System

A complete library management system built in Java with CSV persistence.

## ✨ Features

- **Book Management**: Create, read, update, and delete books with genre/category
- **User Management**: Register users with email validation
- **Loan System**: Borrow and return books with 7-day return period
- **Overdue Tracking**: Automatic detection of late returns with day count
- **Search**: Find books by title, author, or keyword
- **User History**: View all loans per user (active and completed)
- **CSV Persistence**: Data survives application restarts
- **Error Handling**: Custom exceptions for invalid operations

## 🛠️ Technologies

- Java 17+
- Stream API
- LocalDate API
- NIO.2 for file handling
- No external dependencies

## 📁 Project Structure

```
src/
├── application/
│   └── Program.java
├── entities/
│   ├── Book.java
│   ├── User.java
│   └── Loan.java
├── service/
│   ├── LoanService.java
│   ├── StorageService.java
│   ├── LibraryLoanService.java
│   └── CsvStorageService.java
├── exceptions/
│   ├── BookNotFoundException.java
│   ├── UserNotFoundException.java
│   ├── BookNotAvailableException.java
│   ├── LoanNotFoundException.java
│   ├── LoanAlreadyReturnedException.java
│   ├── DuplicateEntityException.java
│   └── InvalidEmailException.java
└── utils/
    └── EmailValidator.java
```

## 🚀 How to Run

### Prerequisites
- Java 17 or higher

### Steps

1. Clone the repository:
```bash
git clone https://github.com/LucasSaraiva01/library-system.git
cd library-system
```

2. Compile the project:
```bash
javac -d . src/**/*.java
```

3. Run the application:
```bash
java application.Program
```

## 📖 Usage

| Option | Description |
|--------|-------------|
| 1 | Register a new book |
| 2 | Register a new user |
| 3 | Borrow a book |
| 4 | Return a book |
| 5 | List available books |
| 6 | List all loans |
| 7 | List all books |
| 8 | List all users |
| 9 | List overdue loans |
| 10 | Search books |
| 11 | User loan history |
| 12 | Edit a book |
| 13 | Delete a book |
| 0 | Exit |

## 📦 Data Persistence

The system creates three CSV files automatically:
- `books.csv` - Stores all books (ID, title, author, genre, available)
- `users.csv` - Stores all users (ID, name, email)
- `loans.csv` - Stores all loans (ID, bookId, userId, loanDate, expectedReturnDate, returnDate)

## 👨‍💻 Author

Lucas Saraiva

## 📄 License

This project is for educational purposes only.
