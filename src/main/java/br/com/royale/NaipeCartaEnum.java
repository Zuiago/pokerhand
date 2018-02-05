package br.com.royale;

import java.util.Arrays;

public enum NaipeCartaEnum {

    HEART(0, "H"),
    SPADES(1, "S"),
    CLUBS(2, "C"),
    DIAMONDS(3, "D");

    private final int posicao;
    private final String valor;

    NaipeCartaEnum(int posicao, String valor) {
        this.posicao = posicao;
        this.valor = valor;
    }

    public int getPosicao() {
        return posicao;
    }

    public String getValor() {
        return valor;
    }

    public static NaipeCartaEnum find(String valor) {
        return Arrays.stream(values())
                .filter(e -> e.getValor().equals(valor))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Tipo não suportado para o valor %s.", valor)));
    }
}
