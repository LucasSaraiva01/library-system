package exceptions;

public class LoanAlreadyReturnedException extends RuntimeException {
    public LoanAlreadyReturnedException(Integer loanId) {
        super("O empréstimo " + loanId + " já foi devolvido.");
    }
}