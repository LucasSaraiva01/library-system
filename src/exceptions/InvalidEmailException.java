package exceptions;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String email) {
        super("Email inválido: " + email + ". O email deve conter @ e um domínio válido.");
    }
}