package br.com.fiap.techchallenge.controller.handler;

import br.com.fiap.techchallenge.exception.RegraNegocioException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final String BASE_URI = "https://techchallenge.fiap.com.br";

    // 1. TRATAMENTO DE VALIDAÇÃO DE FORMULÁRIO (Ex: @Valid, @NotNull, @Email)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationErrors(MethodArgumentNotValidException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Um ou mais campos contêm dados inválidos. Corrija e tente novamente."
        );

        problemDetail.setType(URI.create(BASE_URI));
        problemDetail.setTitle("Erro na validação dos dados enviados");
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

        // Extrai os campos inválidos e joga em uma lista customizada usando o recurso de extensibilidade
        List<Map<String, String>> invalidParams = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String fieldName = (error instanceof org.springframework.validation.FieldError fieldError)
                            ? fieldError.getField()
                            : error.getObjectName(); // Fallback para o nome do DTO caso seja um erro global puro

                    return Map.of(
                            "name", fieldName,
                            "reason", error.getDefaultMessage() != null ? error.getDefaultMessage() : "Valor inválido"
                    );
                })
                .collect(Collectors.toList());

        // Adiciona a propriedade customizada ao JSON (RFC 7807 Extension)
        problemDetail.setProperty("invalid-params", invalidParams);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(problemDetail);
    }

    // 2. TRATAMENTO DE ERROS DE REGRA DE NEGÓCIO
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ProblemDetail> handleRegraNegocio(RegraNegocioException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_CONTENT,
                ex.getMessage()
        );

        problemDetail.setType(URI.create(BASE_URI));
        problemDetail.setTitle("Violação de regra de negócio");
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(problemDetail);
    }

    // 2.1 TRATAMENTO DE CREDENCIAIS INVÁLIDAS (Falha no login com Spring Security)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleBadCredentials(org.springframework.security.authentication.BadCredentialsException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                "Usuário ou senha incorretos. Verifique suas credenciais e tente novamente."
        );

        problemDetail.setType(URI.create(BASE_URI));
        problemDetail.setTitle("Falha na autenticação");
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }

    // 2.2 TRATAMENTO DE TOKEN JWT EXPIRADO
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ProblemDetail> handleTokenExpired(com.auth0.jwt.exceptions.TokenExpiredException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                "O token de acesso enviado está expirado. Por favor, realize um novo login."
        );

        problemDetail.setType(URI.create(BASE_URI));
        problemDetail.setTitle("Token expirado");
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }

    // 2.3 TRATAMENTO DE TOKEN JWT MALFORMATADO / ADULTERADO / INVÁLIDO
    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ProblemDetail> handleInvalidToken(com.auth0.jwt.exceptions.JWTVerificationException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                "O token de acesso enviado é inválido, malformatado ou foi adulterado."
        );

        problemDetail.setType(URI.create(BASE_URI));
        problemDetail.setTitle("Token inválido");
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }

    // 3. TRATAMENTO DE ERROS INTERNOS DO SERVIDOR (Fallback Geral)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleInternalServerError(Exception ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro interno inesperado no servidor. Por favor, tente novamente mais tarde."
        );

        ex.printStackTrace();

        problemDetail.setType(URI.create(BASE_URI));
        problemDetail.setTitle("Erro interno do servidor");
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

        // DICA: Você pode injetar um ID de rastreamento (ex: do Spring Cloud Sleuth/Micrometer) para facilitar a busca nos logs
        problemDetail.setProperty("traceId", "TX-" + System.currentTimeMillis());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }

    // 4. TRATAMENTO DE ERROS DE DE-SERIALIZAÇÃO (Ex: ID inválido enviado para o Enum)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, org.springframework.web.context.request.WebRequest request) {

        ex.printStackTrace();

        // Mensagem padrão caso não consiga extrair a causa raiz
        String mensagemErro = "O corpo da requisição contém dados inválidos ou mal formatados.";

        // Tenta extrair a mensagem customizada ("ID de usuário inválido: X") lançada pelo @JsonCreator do seu Enum
        Throwable causaRaiz = ex.getCause();
        while (causaRaiz != null) {
            if (causaRaiz instanceof IllegalArgumentException) {
                mensagemErro = causaRaiz.getMessage();
                break;
            }
            causaRaiz = causaRaiz.getCause();
        }

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                mensagemErro
        );

        problemDetail.setType(URI.create(BASE_URI));
        problemDetail.setTitle("Erro na leitura dos dados enviados");
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }
}
