package domain;

public class ContaCorrente extends Conta {

    public ContaCorrente(Cliente cliente) {
        super(cliente);
    }

    @Override
    public TipoConta tipoConta() {
        return TipoConta.CORRENTE;
    }

}
