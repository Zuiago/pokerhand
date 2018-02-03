package br.com.royale;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class PokerHand {

    public enum Result {
        DRAW,
        WIN,
        LOSS
    }
    // Armazena todas os valores das cartas
    private final String[] TIPOS = {
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "T",
            "J",
            "Q",
            "K",
            "A"
    };
    // Armazena as posicoes comecando de 0 até 12 das cartas
    private int[] tipos = new int[5];

    // Armazena as cartas depois do split da string para melhor manipulação
    private String[] cartas;

    private int rank;
    // quarda os tipos de cartas descendentemente
    private ArrayList < Integer > maioresNaipes = new ArrayList < Integer > ();

    /**
     * Constructor
     * @param stringCartas
     */
    public PokerHand(String stringCartas) {
        // Realiza split das cartas jogando em um array do tipo String para dar mais facilidade nos metodos abaixo,
        // deconsiderando o espaço como separador da string
        this.cartas = StringUtils.split(stringCartas, " ");
        sortCartasPorTipo();


        for (int i = 0; i < this.cartas.length; i++) {
            this.tipos[i] = getKind(cartas[i]);
        }

        boolean isStraight = isStraight();
        boolean isFlush = isFlush();

        if (isStraight && isFlush) {
            this.rank = 8;
        } else if (isQuadra()) {
            this.rank = 7;
        } else if (isTrinca()) {

            if (isFullHouse()) {
                this.rank = 6;
            } else {
                this.rank = 3;
            }
        } else if (isFlush) {
            this.rank = 5;
            for (int i = this.tipos.length - 1; i >= 0; i--) {
                this.maioresNaipes.add(this.tipos[i]);
            }
        } else if (isStraight) {
            this.rank = 4;
        } else if (isDoisPares()) {
            this.rank = 2;
        } else if (isUmPar()) {
            this.rank = 1;
        } else {
            this.rank = 0;
            for (int i = this.tipos.length - 1; i >= 0; i--) {
                this.maioresNaipes.add(this.tipos[i]);
            }
        }
    }

    /**
     * Verifica se são cinco cartas em sequencia do mesmo naipe.
     * @return boolean
     */
    private boolean isStraight() {
        boolean isStraight = true;

        for (int i = 0; i < this.cartas.length - 1; i++) {
            // not the consecutive kind
            if (this.tipos[i + 1] != this.tipos[i] + 1) {
                // Entra aqui quando os tipos se misturam: 2 3 4 5 A
                if (this.tipos[i] == 3 && this.tipos[i + 1] == 12) {
                    return true;
                }
                // Retorna quando os tipos seguem uma seguencia numerica
                return false;
            }
        }

        // Retorna a carta com maior naipe
        if (this.tipos[3] == 3 && this.tipos[4] == 12) {
            // Entra aqui quando os tipos se misturam: 2 3 4 5 A
            this.maioresNaipes.add(this.tipos[3]);
        } else {
            //  Retorna quando os tipos seguem uma seguencia numerica
            this.maioresNaipes.add(this.tipos[4]);
        }

        return isStraight;
    }

    /**
     * Verifica se a mão é categorizada como straight
     * @return boolean
     */
    private boolean isFlush() {
        boolean isFlush = true;
        for (int i = 0; i < this.cartas.length - 1; i++) {
            // not the same suit
            if (!StringUtils.substring(cartas[i], 1, 2).equals(cartas[i + 1].substring(1, 2))) {
                return false;
            }
        }
        return isFlush;
    }

    /**
     * Verifica se a mão é categorizada como quadra
     * @return boolean
     */
    private boolean isQuadra() {
        // da primeira carta até a quarta
        if (this.tipos[1] == this.tipos[0] && this.tipos[2] == this.tipos[0] &&
                this.tipos[3] == this.tipos[0]) {
            this.maioresNaipes.add(this.tipos[0]);
            this.maioresNaipes.add(this.tipos[4]);
            return true;
        }

        // da segunda carta até a quinta
        if (this.tipos[2] == this.tipos[1] && this.tipos[3] == this.tipos[1] &&
                this.tipos[4] == this.tipos[1]) {
            this.maioresNaipes.add(this.tipos[1]);
            this.maioresNaipes.add(this.tipos[0]);
            return true;
        }

        return false;
    }

    /**
     * Verifica se a mão é categorizada como uma trinca
     * @return boolean
     */
    private boolean isTrinca() {
        // da primeira carta até a terceira
        if (this.tipos[1] == this.tipos[0] && this.tipos[2] == this.tipos[0]) {
            this.maioresNaipes.add(this.tipos[0]);
            return true;
        }

        // da segunda até a quarta carta
        if (this.tipos[2] == this.tipos[1] && this.tipos[3] == this.tipos[1]) {
            this.maioresNaipes.add(this.tipos[1]);
            return true;
        }

        // da terceira até a quinta carta
        if (this.tipos[3] == this.tipos[2] && this.tipos[4] == this.tipos[2]) {
            this.maioresNaipes.add(this.tipos[2]);
            return true;
        }

        return false;
    }

    /**
     * Verifica se a mão é categorizada como Full House
     * @return boolean
     */
    private boolean isFullHouse() {
        // da primeira carta até a terceira
        if (this.maioresNaipes.get(0) == this.tipos[0]) {
            // another pair
            if (this.tipos[3] == this.tipos[4]) {
                this.maioresNaipes.add(this.tipos[3]);
                return true;
            } else {
                this.maioresNaipes.add(this.tipos[4]);
                this.maioresNaipes.add(this.tipos[3]);
                return false;
            }
        }

        // second case, 2 to 4 cartas
        if (this.maioresNaipes.get(0) == this.tipos[1]) {
            this.maioresNaipes.add(this.tipos[4]);
            this.maioresNaipes.add(this.tipos[0]);
            return false;
        }

        // third case, 3 to 5 cartas
        if (this.maioresNaipes.get(0) == this.tipos[2]) {
            // another pair
            if (this.tipos[0] == this.tipos[1]) {
                this.maioresNaipes.add(this.tipos[0]);
                return true;
            } else {
                this.maioresNaipes.add(this.tipos[1]);
                this.maioresNaipes.add(this.tipos[0]);
                return false;
            }
        }

        return false;
    }

    /**
     * Verifica se a mão é categorizada como Dois Pares
     * @return boolean
     */
    private boolean isDoisPares() {
        // da primeira e a segunda carta, e também da terceira e a quarta carta
        if (this.tipos[0] == this.tipos[1] && this.tipos[2] == this.tipos[3]) {
            this.maioresNaipes.add(this.tipos[2]);
            this.maioresNaipes.add(this.tipos[0]);
            this.maioresNaipes.add(this.tipos[4]);
            return true;
        }


        // da primeira e a segunda carta, e também da quarta e a quinta carta
        if (this.tipos[0] == this.tipos[1] && this.tipos[3] == this.tipos[4]) {
            this.maioresNaipes.add(this.tipos[3]);
            this.maioresNaipes.add(this.tipos[0]);
            this.maioresNaipes.add(this.tipos[2]);
            return true;
        }

        // da segunda e a terceira carta, e também da quarta e a quinta carta
        if (this.tipos[1] == this.tipos[2] && this.tipos[3] == this.tipos[4]) {
            this.maioresNaipes.add(this.tipos[3]);
            this.maioresNaipes.add(this.tipos[1]);
            this.maioresNaipes.add(this.tipos[0]);
            return true;
        }

        return false;
    }

    /**
     * Verifica se a mão é categorizada como Um Par
     * @return boolean
     */
    private boolean isUmPar() {
        // 1st case: 1 & 2
        if (this.tipos[0] == this.tipos[1]) {
            this.maioresNaipes.add(this.tipos[0]);
            this.maioresNaipes.add(this.tipos[4]);
            this.maioresNaipes.add(this.tipos[3]);
            this.maioresNaipes.add(this.tipos[2]);
            return true;
        }

        // 2nd case: 2 & 3
        if (this.tipos[1] == this.tipos[2]) {
            this.maioresNaipes.add(this.tipos[1]);
            this.maioresNaipes.add(this.tipos[4]);
            this.maioresNaipes.add(this.tipos[3]);
            this.maioresNaipes.add(this.tipos[0]);
            return true;
        }

        // 3rd case: 3 & 4
        if (this.tipos[2] == this.tipos[3]) {
            this.maioresNaipes.add(this.tipos[2]);
            this.maioresNaipes.add(this.tipos[4]);
            this.maioresNaipes.add(this.tipos[1]);
            this.maioresNaipes.add(this.tipos[0]);
            return true;
        }

        // 4th case: 4 & 5
        if (this.tipos[3] == this.tipos[4]) {
            this.maioresNaipes.add(this.tipos[3]);
            this.maioresNaipes.add(this.tipos[2]);
            this.maioresNaipes.add(this.tipos[1]);
            this.maioresNaipes.add(this.tipos[0]);
            return true;
        }

        return false;
    }

    private static Result compare(ArrayList < Integer > lista1, ArrayList < Integer > lista2, int tamanho) {
        for (int i = 0; i < tamanho; i++) {
            if (lista1.get(i) > lista2.get(i)) {
                return Result.WIN;
            } else if (lista1.get(i) < lista2.get(i)) {
                return Result.LOSS;
            }
        }

        return Result.DRAW;
    }


    public Result compareWith(PokerHand hand) {

        // Verifica se o rank da primeira mão é maior que a passada por parametro
        if (this.rank > hand.getRank()) {
            return Result.WIN;
        } else if (this.rank < hand.getRank()) {
            return Result.LOSS;
        } else {
            switch (this.rank) {
                case 8:
                case 4:
                    // No caso Straight Flush, Straight or Flush
                    return compare(this.getMaioresNaipes(), hand.getMaioresNaipes(), 1);
                case 7:
                case 6:
                    // for Four Cards of a Kind and Full House
                    return compare(this.getMaioresNaipes(), hand.getMaioresNaipes(), 2);
                case 3:
                case 2:
                    // for Three Cards of a Kind and for Two Pairs
                    return compare(this.getMaioresNaipes(), hand.getMaioresNaipes(), 3);
                case 1:
                    // for One Pair
                    return compare(this.getMaioresNaipes(), hand.getMaioresNaipes(), 4);
                case 5:
                case 0:
                    // for Flush and High Card
                    return compare(this.getMaioresNaipes(), hand.getMaioresNaipes(), 5);
                default:
                    return Result.DRAW;
            }
        }
    }


    private void sortCartasPorTipo() {

        Arrays.sort(cartas, new Comparator < String > () {

            public int compare(String a, String b) {
                int tipoOperator1 = Arrays.asList(TIPOS).indexOf(StringUtils.substring(a, 0, 1));
                int tipoOperator2 = Arrays.asList(TIPOS).indexOf(StringUtils.substring(b, 0, 1));
                return tipoOperator1 - tipoOperator2;
            }

        });
    }


    public int getRank() {
        return this.rank;
    }


    public ArrayList < Integer > getMaioresNaipes() {
        return this.maioresNaipes;
    }

    private int getKind(String carta) {
        int kind = Arrays.asList(TIPOS).indexOf(StringUtils.substring(carta, 0, 1));
        return kind;
    }
}
