package com.api.gestaodehotel.config;

import com.api.gestaodehotel.dto.response.ErroCampoDTO;
import com.api.gestaodehotel.dto.response.ErroResponseDTO;
import com.api.gestaodehotel.exceptions.QuartoExistenteException;
import com.api.gestaodehotel.exceptions.QuartoNaoExisteException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponseDTO> handlerGeneric(RuntimeException ex, HttpServletRequest request) {
        ErroResponseDTO erro = new ErroResponseDTO(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    @ExceptionHandler(QuartoExistenteException.class)
    public ResponseEntity<ErroResponseDTO> handlerQuartoExistente(QuartoExistenteException ex, HttpServletRequest request) {
        ErroResponseDTO erro = new ErroResponseDTO(
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(QuartoNaoExisteException.class)
    public ResponseEntity<ErroResponseDTO> handlerQuartoNaoExiste(QuartoNaoExisteException ex, HttpServletRequest request) {
        ErroResponseDTO erro = new ErroResponseDTO(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> handlerMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<ErroCampoDTO> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ErroCampoDTO(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        ErroResponseDTO resposta = new ErroResponseDTO(
                "Campos Inválidos",
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI(),
                erros);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponseDTO> handlerMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request){
        String mensagem = "O valor do campo " + ex.getName() + " deve ser do tipo " + ex.getRequiredType().getSimpleName();
        ErroResponseDTO erro = new ErroResponseDTO(
                mensagem,
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErroResponseDTO> handlerNoHandlerFound(HttpServletRequest request){
        ErroResponseDTO erro = new ErroResponseDTO(
            "Endpoint não encontrado",
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

}