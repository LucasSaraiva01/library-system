package exceptions;

public class DuplicateEntityException extends RuntimeException {
    public DuplicateEntityException(String entityType, Integer id) {
        super(entityType + " com ID " + id + " já existe.");
    }
    
    public DuplicateEntityException(String entityType, String email) {
        super(entityType + " com email " + email + " já existe.");
    }
}