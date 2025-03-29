package domain;

public class ContaJaExistenteException extends RuntimeException {

    public ContaJaExistenteException(String nomeCliente) {
        super("Já existe uma conta cadastrada para um cliente com o nome -> " + nomeCliente);
    }

}
