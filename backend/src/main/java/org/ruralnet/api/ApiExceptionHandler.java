package org.ruralnet.api;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(ResponseStatusException.class)
  ResponseEntity<?> status(ResponseStatusException ex){String code=ex.getStatusCode().value()==404?"NOT_FOUND":ex.getStatusCode().value()==409?"CONFLICT":ex.getStatusCode().value()==422?"VALIDATION_ERROR":"REQUEST_ERROR";return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error",Map.of("code",code,"message",Objects.toString(ex.getReason(),"Request failed"),"details",List.of(),"requestId",UUID.randomUUID().toString())));}
  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<?> invalid(MethodArgumentNotValidException ex){var details=ex.getBindingResult().getFieldErrors().stream().map(e->Map.of("field",e.getField(),"message",Objects.toString(e.getDefaultMessage(),"invalid value"))).toList();return ResponseEntity.badRequest().body(Map.of("error",Map.of("code","VALIDATION_ERROR","message","Check the marked fields and try again.","details",details,"requestId",UUID.randomUUID().toString())));}
  @ExceptionHandler(Exception.class)
  ResponseEntity<?> unexpected(Exception ex){String requestId=UUID.randomUUID().toString();System.err.println("Unexpected request error "+requestId+": "+ex.getClass().getSimpleName());return ResponseEntity.internalServerError().body(Map.of("error",Map.of("code","INTERNAL_ERROR","message","The request could not be completed.","details",List.of(),"requestId",requestId)));}
}
