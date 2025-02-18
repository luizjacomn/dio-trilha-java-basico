public class Main {

    public static void main(String[] args) {
        IPhone iPhone = new IPhone();

        iPhone.selecionarMusica("Asa Branca - Luiz Gonzaga");
        iPhone.tocar();
        iPhone.pausar();

        iPhone.ligar("+5521900115454");
        iPhone.atender();
        iPhone.iniciarCorreioVoz();

        iPhone.exibirPagina("https://www.github.com/luizjacomn");
        iPhone.atualizarPagina();
        iPhone.adicionarNovaAba();

    }

}
