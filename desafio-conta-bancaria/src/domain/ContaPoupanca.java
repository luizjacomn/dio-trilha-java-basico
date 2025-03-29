package domain;

public class ContaPoupanca extends Conta {

    public ContaPoupanca(Cliente cliente) {
        super(cliente);
    }

    @Override
    public TipoConta tipoConta() {
        return TipoConta.POUPANCA;
    }

}
