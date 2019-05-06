package me.raddatz.jwarden.common.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class JWardenExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(JWardenException.class)
    @ResponseBody
    protected ResponseEntity<Object> handleJWardenException(JWardenException ex, WebRequest request) {
        int errorCode = 500;
        ResponseStatus[] status = ex.getClass().getDeclaredAnnotationsByType(ResponseStatus.class);
        if (status != null && status.length > 0)
            errorCode = status[0].value().value();

        return ResponseEntity
                .status(errorCode)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(new JWardenExceptionResponse(ex));
    }

}
