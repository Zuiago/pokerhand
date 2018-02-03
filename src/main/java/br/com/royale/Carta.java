package br.com.royale;

public class Carta {

    private String valor;
    private NaipeCartaEnum naipe;
    private RankCartaEnum rank;

    public Carta(String valor, NaipeCartaEnum naipe, RankCartaEnum rank) {
        this.valor = valor;
        this.naipe = naipe;
        this.rank = rank;
    }

    public static int comparePorPosicao(Carta c1, Carta c2) {
        return c1.getRank().getPosicao() - c2.getRank().getPosicao();
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public NaipeCartaEnum getNaipe() {
        return naipe;
    }

    public void setNaipe(NaipeCartaEnum naipe) {
        this.naipe = naipe;
    }

    public RankCartaEnum getRank() {
        return rank;
    }

    public void setRank(RankCartaEnum rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "Carta{" +
                "valor='" + valor + '\'' +
                ", naipe=" + naipe +
                ", rank=" + rank +
                '}';
    }
}
