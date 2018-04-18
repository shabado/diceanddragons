package shabadoit.com.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(SpellManagementException.class)
    private void handleBadRequest(SpellManagementException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(CharacterManagementException.class)
    private void handleBadRequest(CharacterManagementException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    private void handleResourceNotFound(ResourceNotFoundException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    /**
     * Exception handler for @RequestBody validation errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(convert(e.getBindingResult().getAllErrors()));
    }

    /**
     * Exception handler for missing required parameters errors.
     */
    @ExceptionHandler(ServletRequestBindingException.class)
    private ResponseEntity<?> handleServletRequestBindingException(ServletRequestBindingException exception) {

        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    private List<ValidationError> convert(List<ObjectError> objectErrors) {

        List<ValidationError> errors = new ArrayList<>();

        for (ObjectError objectError : objectErrors) {

            String message = objectError.getDefaultMessage();
            if (message == null) {
                //when using custom spring validator org.springframework.validation.Validator need to resolve messages manually
                message = messageSource.getMessage(objectError, null);
            }

            ValidationError error = null;
            if (objectError instanceof FieldError) {
                FieldError fieldError = (FieldError) objectError;
                String value = (fieldError.getRejectedValue() == null ? null : fieldError.getRejectedValue().toString());
                error = new ValidationError(fieldError.getField(), value, message);
            } else {
                error = new ValidationError(objectError.getObjectName(), objectError.getCode(), objectError.getDefaultMessage());
            }

            errors.add(error);
        }

        return errors;
    }
}
