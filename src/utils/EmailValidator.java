package utils;

public class EmailValidator {
    
    public static boolean isValid(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        
        // Validação simples: deve conter @ e . após o @
        int atIndex = email.indexOf('@');
        if (atIndex <= 0 || atIndex != email.lastIndexOf('@')) {
            return false;
        }
        
        String domain = email.substring(atIndex + 1);
        int dotIndex = domain.indexOf('.');
        if (dotIndex <= 0 || dotIndex == domain.length() - 1) {
            return false;
        }
        
        // Não pode ter espaços
        if (email.contains(" ")) {
            return false;
        }
        
        return true;
    }
}