package shabadoit.exceptions;

public class CharacterManagementException extends RuntimeException {
    public CharacterManagementException(final String message) {
        super(message);
    }

    public CharacterManagementException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
