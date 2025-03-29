package domain;

public class ContaNaoEncontradaException extends RuntimeException {

    public ContaNaoEncontradaException(int agencia, int conta) {
        super("Não existe conta cadastrada com os dados informados -> Ag.: " + agencia + " Nº: " + conta);
    }

}
