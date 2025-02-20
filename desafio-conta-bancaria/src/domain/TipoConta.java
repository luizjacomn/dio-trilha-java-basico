package domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TipoConta {

    CORRENTE("Corrente"), POUPANCA("Poupança");

    private final String descricao;

}
