package br.com.royale;

public enum CategoriaPokerHandEnum {

    ROYAL_FLUSH(8 ,"Royal Flush"),
    STRAIGHT_FLUSH(8 ,"Straight Flush"),
    QUADRA(7, "Quadra"),
    FULL_HOUSE(6, "Full House"),
    FLUSH(5, "Flush"),
    STRAIGHT(4, "Sequência"),
    TRINCA(3, "Trinca"),
    DOIS_PARES(2, "Dois Pares"),
    UM_PAR(1, "Um Par"),
    CARTA_ALTA(0, "Carta Alta");

    private final int rank;
    private final String categoria;

    CategoriaPokerHandEnum(int rank, String categoria) {
        this.rank = rank;
        this.categoria = categoria;
    }

    public int getRank() {
        return rank;
    }

    public String getCategoria() {
        return categoria;
    }
}
