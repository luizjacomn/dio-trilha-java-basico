package domain;

import lombok.Getter;

import java.util.logging.Logger;

@Getter
public abstract class Conta implements IConta {

    private static final Logger LOGGER = Logger.getLogger(Conta.class.getCanonicalName());

    private static final int AGENCIA_PADRAO = 1;

    private static int sequencial = 1;

    protected int agencia;

    protected int numero;

    protected double saldo;

    protected Cliente cliente;

    protected Conta(Cliente cliente) {
        this.agencia = Conta.AGENCIA_PADRAO;
        this.numero = sequencial++;
        this.cliente = cliente;
    }

    @Override
    public void sacar(double valor) {
        if (valor > saldo) {
            throw new SaldoInsuficienteException("Não é possível efetuar o saque para o valor informado! Consulte seu saldo e tente outro valor.");
        }

        saldo -= valor;
    }

    @Override
    public void depositar(double valor) {
        saldo += valor;
    }

    @Override
    public void transferir(double valor, IConta contaDestino) {
        this.sacar(valor);
        contaDestino.depositar(valor);
    }

    @Override
    public void imprimirExtrato() {
        System.out.println(
            String.format("=== Extrato Conta %s ===", this.tipoConta().getDescricao()) +
            String.format("%nTitular: %s", this.cliente.getNome()) +
            String.format("%nAgência: %d", this.agencia) +
            String.format("%nNúmero: %d", this.numero) +
            String.format("%nSaldo: %.2f", this.saldo)
        );
    }

    @Override
    public String toString() {
        return "Conta { " +
            "Tipo: " + tipoConta().getDescricao() +
            ", Ag.: " + agencia +
            ", Nº: " + numero +
            ", Cliente: " + cliente.getNome() +
        " }";
    }
}
