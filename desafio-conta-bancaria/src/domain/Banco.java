package domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Banco {

    @Getter
    private final String nome;

    private final List<Conta> contas;

    public Banco(String nome) {
        this.nome = nome;
        contas = new ArrayList<>();
    }

    public void criarConta(Conta conta, boolean forcarCriacao) {
        if (contas().anyMatch(c -> c.getCliente().getNome().equalsIgnoreCase(conta.cliente.getNome())) && !forcarCriacao) {
            throw new ContaJaExistenteException(conta.cliente.getNome());
        }

        this.contas.add(conta);
    }

    public Stream<Conta> contas() {
        return contas.stream();
    }

}
