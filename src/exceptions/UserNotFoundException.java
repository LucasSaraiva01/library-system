package exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Integer userId) {
        super("Usuário com ID " + userId + " não encontrado.");
    }
}