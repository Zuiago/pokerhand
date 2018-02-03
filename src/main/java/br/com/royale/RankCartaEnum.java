package br.com.royale;

import java.util.Arrays;

public enum RankCartaEnum {
    TWO(0, "2"),
    TREE(1, "3"),
    FOUR(2, "4"),
    FIVE(3, "5"),
    SIX(4, "6"),
    SERVEN(5, "7"),
    EIGHT(6, "8"),
    NINE(7, "9"),
    TEN(8, "T"),
    JACK(9, "J"),
    QUEEN(10, "Q"),
    KING(11, "K"),
    ACE(12, "A");

    private final int posicao;
    private final String valor;

    RankCartaEnum(int posicao, String valor) {
        this.posicao = posicao;
        this.valor = valor;
    }

    public int getPosicao() {
        return posicao;
    }

    public String getValor() {
        return valor;
    }

    public static RankCartaEnum find(String valor) {
        return Arrays.stream(RankCartaEnum.values())
                .filter(e -> e.getValor().equals(valor))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Tipo não suportado para o valor %s.", valor)));
    }
}
