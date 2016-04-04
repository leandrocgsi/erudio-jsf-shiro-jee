package br.com.tecsinapse.model.enums;

public enum TipoJogador {

    SIMPLES("SIMPLES"),
    DUPLAS("DUPLAS"),
    AMBOS("AMBOS");

    private final String descricao;

    TipoJogador(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
