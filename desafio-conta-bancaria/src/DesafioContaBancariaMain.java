import domain.*;

import java.util.Scanner;

public class DesafioContaBancariaMain {

    private static final Scanner SCANNER = new Scanner(System.in);

    private static final Banco BANCO = new Banco("Banco DIO");

    public static void main(String[] args) {
        var opcao = 0;

        System.out.println("\nBem-vindo(a) ao " + BANCO.getNome() + "\n");

        do {
            System.out.println("Selecione uma opção:");
            System.out.println("1 - Listar contas");
            System.out.println("2 - Criar conta corrente");
            System.out.println("3 - Criar conta poupança");
            System.out.println("4 - Depositar");
            System.out.println("5 - Sacar");
            System.out.println("6 - Transferir");
            System.out.println("7 - Imprimir extrato");
            System.out.println("0 - Sair");

            opcao = SCANNER.nextInt();

            try {
                switch (opcao) {
                    case 1:
                        imprimirContas();
                        break;
                    case 2:
                        criarContaCorrente();
                        break;
                    case 3:
                        criarContaPoupanca();
                        break;
                    case 4:
                        depositar();
                        break;
                    case 5:
                        sacar();
                        break;
                    case 6:
                        transferir();
                        break;
                    case 7:
                        imprimirExtrato();
                        break;
                    default:
                        break;
                }
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
            }
        } while (opcao != 0) ;
    }

    /* OPÇÕES */
    private static void imprimirContas() {
        System.out.println("=== CONTAS ===");
        BANCO.contas().forEach(System.out::println);
        System.out.println();
    }

    private static void criarContaCorrente() {
        var cliente = criarCliente();

        var contaCorrente = new ContaCorrente(cliente);

        criarConta(contaCorrente);
    }

    private static void criarContaPoupanca() {
        var cliente = criarCliente();

        var contaPoupanca = new ContaPoupanca(cliente);

        criarConta(contaPoupanca);
    }

    private static void depositar() {
        buscarConta().depositar(lerValor());
    }

    private static void sacar() {
        buscarConta().sacar(lerValor());
    }

    private static void transferir() {
        var contaOrigem = buscarConta(
            "Informe o número da agência da conta de origem:",
            "Informe o número da conta de origem:"
        );

        var contaDestino = buscarConta(
            "Informe o número da agência da conta de destino:",
            "Informe o número da conta de destino:"
        );

        contaOrigem.transferir(lerValor(), contaDestino);
    }

    private static void imprimirExtrato() {
        buscarConta().imprimirExtrato();
    }

    /* MÉTODOS AUXILIARES */
    private static Cliente criarCliente() {
        System.out.println("Informe o nome do cliente:");
        var nomeCliente = SCANNER.next();

        return new Cliente(nomeCliente);
    }

    private static void criarConta(Conta conta) {
        try {
            BANCO.criarConta(conta, false);
        } catch (ContaJaExistenteException e) {
            System.err.println(e.getMessage() + " Deseja criar mesmo assim? S/N");
            var criar = SCANNER.next();

            if ("S".equalsIgnoreCase(criar)) {
                BANCO.criarConta(conta, true);
            }
        }
    }

    private static Conta buscarConta() {
        return buscarConta(
            "Informe o número da agência:",
            "Informe o número da conta:"
        );
    }

    private static Conta buscarConta(String perguntaAgencia, String perguntaConta) {
        System.out.println(perguntaAgencia);
        int agencia = SCANNER.nextInt();

        System.out.println(perguntaConta);
        int numero = SCANNER.nextInt();

        return BANCO.contas()
             .filter(conta -> conta.getAgencia() == agencia && conta.getNumero() == numero)
             .findAny()
             .orElseThrow(() -> new ContaNaoEncontradaException(agencia, numero));
    }

    private static double lerValor() {
        System.out.println("Informe o valor:");

        return SCANNER.nextDouble();
    }

}
