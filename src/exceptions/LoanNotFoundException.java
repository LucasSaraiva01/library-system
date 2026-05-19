package exceptions;

public class LoanNotFoundException extends RuntimeException {
    public LoanNotFoundException(Integer loanId) {
        super("Empréstimo com ID " + loanId + " não encontrado.");
    }
}