package br.com.royale;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class PokerHand {

    private final List<Carta> hand = new ArrayList<>();
    private CategoriaPokerHandEnum categoriaPokerHandEnum;
    private List<Integer> maioresValores = new ArrayList<>();
    List<Integer> posicoes = new ArrayList<>();
    private Integer primeiraPosicao = null;
    private Integer segundaPosicao = null;
    private Integer terceiraPosicao = null;
    private Integer quartaPosicao = null;
    private Integer quintaPosicao = null;

    /**
     * Constructor
     *
     * @param stringCartas
     */
    public PokerHand(String stringCartas) {
        stringCartasParaObjetoCartas(StringUtils.split(stringCartas, " "), this.hand);
        sortCartasPorTipo(this.hand);
        this.posicoes = this.hand.stream().map(c -> c.getRank().getPosicao()).collect(toList());
        atribuirPosicoes();
        this.categoriaPokerHandEnum = determinePokerRank(this.hand);
    }

    public CategoriaPokerHandEnum determinePokerRank(List<Carta> cartas) {
        if (isRoyalFlush(cartas)) {
            return CategoriaPokerHandEnum.ROYAL_FLUSH;
        } else if (isStraightFlush(cartas)) {
            adicionarMaioresValoresStraight();
            return CategoriaPokerHandEnum.STRAIGHT_FLUSH;
        } else if (isQuadra(cartas)) {
            adicionarMaioresValoresQuadra();
            return CategoriaPokerHandEnum.QUADRA;
        } else if (isFlush(cartas)) {
            adicionarMaioresValores();
            return CategoriaPokerHandEnum.FLUSH;
        } else if (isStraight(cartas)) {
            adicionarMaioresValoresStraight();
            return CategoriaPokerHandEnum.STRAIGHT;
        } else if (isTrinca(cartas)) {
            adicionarMaioresValoresTrinca();
            if (isFullHouse(cartas)) {
                adicionarMaioresValoresFullHouse();
                return CategoriaPokerHandEnum.FULL_HOUSE;
            } else {
                return CategoriaPokerHandEnum.TRINCA;
            }
        } else if (isDoisPares(cartas)) {
            adicionarMaioresValoresDoisPares();
            return CategoriaPokerHandEnum.DOIS_PARES;
        } else if (isUmPar(cartas)) {
            adicionarMaioresValoresUmPar();
            return CategoriaPokerHandEnum.UM_PAR;
        } else {
            adicionarMaioresValores();
            return CategoriaPokerHandEnum.CARTA_ALTA;
        }
    }

    private static ResultEnum compare(List<Integer> lista1, List<Integer> lista2, int tamanho) {
        for (int i = 0; i < tamanho; i++) {
            if (lista1.get(i) > lista2.get(i)) {
                return ResultEnum.WIN;
            } else if (lista1.get(i) < lista2.get(i)) {
                return ResultEnum.LOSS;
            }
        }

        return ResultEnum.DRAW;
    }

    public ResultEnum compareWith(PokerHand pokeHandOO) {

        // Verifica se o rank da primeira mão é maior que a passada por parametro
        if (this.categoriaPokerHandEnum.getRank() > pokeHandOO.categoriaPokerHandEnum.getRank()) {
            return ResultEnum.WIN;
        } else if (this.categoriaPokerHandEnum.getRank() < pokeHandOO.categoriaPokerHandEnum.getRank()) {
            return ResultEnum.LOSS;
        } else {
            switch (this.categoriaPokerHandEnum) {
                case STRAIGHT_FLUSH:
                case STRAIGHT:
                    // No caso Straight Flush, Straight or Flush
                    return compare(this.maioresValores, pokeHandOO.maioresValores, 1);
                case QUADRA:
                case FULL_HOUSE:
                    // No caso de Quadra e Full House
                    return compare(this.maioresValores, pokeHandOO.maioresValores, 2);
                case TRINCA:
                case DOIS_PARES:
                    // No caso de Trinca e Dois Pares
                    return compare(this.maioresValores, pokeHandOO.maioresValores, 3);
                case UM_PAR:
                    // No caso de UmPar
                    return compare(this.maioresValores, pokeHandOO.maioresValores, 4);
                case FLUSH:
                case CARTA_ALTA:
                    // No caso de Flush e Carta Alta
                    return compare(this.maioresValores, pokeHandOO.maioresValores, 5);
                default:
                    return ResultEnum.DRAW;
            }
        }
    }

    /**
     * Verifica se a mão é categorizada como Royal Flush.
     * Ela é composta pelo ás, rei, dama, valete e dez, todos do mesmo naip
     *
     * @return boolean
     */
    private boolean isRoyalFlush(List<Carta> cartas) {
        if (isStraight(cartas) && isFlush(cartas)) {
            boolean aceExists = false, kingExists = false, queenExists = false, jackExists = false, tenExists = false;
            for (Carta c : cartas) {
                switch (c.getRank()) {
                    case ACE:
                        aceExists = true;
                        break;
                    case KING:
                        kingExists = true;
                        break;
                    case QUEEN:
                        queenExists = true;
                        break;
                    case JACK:
                        jackExists = true;
                        break;
                    case TEN:
                        tenExists = true;
                        break;
                }
            }
            return (aceExists && kingExists && queenExists && jackExists && tenExists);
        } else {
            return false;
        }
    }

    /**
     * Verifica se a mão é categorizada como StraightFlush.
     * Cinco cartas de naipes diferentes em sequência
     *
     * @return boolean
     */
    private boolean isStraightFlush(List<Carta> cartas) {
        return isFlush(cartas) && isStraight(cartas);
    }

    /**
     * Verifica se a mão é categorizada como Quadra
     * Quatro cartas de mesmo valor e uma outra carta como ’kicker’.
     *
     * @return boolean
     */
    private boolean isQuadra(List<Carta> cartas) {
        return isMesmoValor(4, cartas);
    }

    /**
     * Verifica se a mão é categorizada como Quadra
     * Três cartas do mesmo valor e duas outras cartas diferentes também de mesmo valor.
     *
     * @return boolean
     */
    private boolean isFullHouse(List<Carta> cartas) {
        return (isUmPar(cartas) && isTrinca(cartas));
    }

    /**
     * Verifica se a mão é categorizada como flush
     * Cinco cartas de mesmos naipes, não em sequência
     *
     * @return boolean
     */
    private boolean isFlush(List<Carta> cartas) {
        return cartas.stream().allMatch(e -> e.getNaipe().getValor().equals(cartas.get(0).getNaipe().getValor()));
    }

    /**
     * Verifica se a mão é categorizada como Straight.
     * Cinco cartas em sequência
     *
     * @return boolean
     */
    private boolean isStraight(List<Carta> cartas) {
        int noOfCardsInARow = 0;
        int pos = 0;
        boolean isAStraight = false;
        while (pos < cartas.size() - 1 && !isAStraight) {
            if (cartas.get(pos + 1).getRank().getPosicao() - cartas.get(pos).getRank().getPosicao() == 1) {
                noOfCardsInARow++;
                if (noOfCardsInARow == 4) {
                    isAStraight = true;
                } else {
                    pos++;
                }
            } else {
                noOfCardsInARow = 0;
                pos++;
            }
        }

        return isAStraight;
    }

    /**
     * Verifica se a mão é categorizada como Trinca
     * Três cartas de mesmo valor e duas outras cartas não relacionadas.
     *
     * @return boolean
     */
    private boolean isTrinca(List<Carta> cartas) {
        return isMesmoValor(3, cartas);
    }

    /**
     * Verifica se a mão é categorizada como DoisPares
     * Duas cartas de mesmo valor, duas outras cartas de mesmo valor e o ’kicker’.
     *
     * @return boolean
     */
    private boolean isDoisPares(List<Carta> cartas) {
        final List<Map.Entry<String, Long>> lista = cartas.stream()
                .collect(Collectors.groupingBy(c -> c.getValor(), Collectors.counting()))
                .entrySet().stream().filter(t -> t.getValue() == 2).collect(toList());
        return lista.size() == 2;
    }

    /**
     * Verifica se a mão é categorizada como DoisPares
     * Duas cartas de mesmo valor e tr^es outras cartas não relacionadas
     *
     * @return boolean
     */
    private boolean isUmPar(List<Carta> cartas) {
        return isMesmoValor(2, cartas);
    }

    private boolean isMesmoValor(int qtdValor, List<Carta> cartas) {
        Optional<Map.Entry<String, Long>> first = cartas.stream()
                .collect(Collectors.groupingBy(c -> c.getValor(), Collectors.counting()))
                .entrySet().stream().filter(t -> t.getValue() == qtdValor).findFirst();
        return (first.isPresent() && first.get().getValue() == qtdValor);
    }

    private void sortCartasPorTipo(List<Carta> listaCartas) {
        listaCartas.sort(Carta::comparePorPosicao);
    }

    private void adicionarMaioresValores() {
        this.posicoes.sort(Collections.reverseOrder());
        this.maioresValores.addAll(posicoes);
    }

    private void adicionarMaioresValoresQuadra() {
        if (this.posicoes.get(1) == posicoes.get(4)){
            this.posicoes.sort(Collections.reverseOrder());
        }
        this.maioresValores.addAll(posicoes.stream().distinct().collect(toList()));
    }

    private void adicionarMaioresValoresTrinca() {

        Stream<Integer> integerStream = posicoes.stream().filter(i -> Collections.frequency(posicoes, i) > 1);
        this.maioresValores.add(integerStream.findFirst().get());
    }

    private void adicionarMaioresValoresFullHouse() {

        if (this.maioresValores.size() > 0){
            // da 1ª até a 3ª carta
            if (this.maioresValores.get(0) == this.primeiraPosicao) {
                if (this.quartaPosicao == this.quintaPosicao) {
                    this.maioresValores.add(this.quartaPosicao);
                } else {
                    this.maioresValores.add(this.quintaPosicao);
                    this.maioresValores.add(this.quartaPosicao);
                }
            }

            // da 2ª até a 4ª carta
            if (this.maioresValores.get(0) == this.segundaPosicao) {
                this.maioresValores.add(this.quintaPosicao);
                this.maioresValores.add(this.primeiraPosicao);
            }

            // da 3ª até a 5ª carta
            if (this.maioresValores.get(0) == this.terceiraPosicao) {
                if (this.primeiraPosicao == this.segundaPosicao) {
                    this.maioresValores.add(this.primeiraPosicao);
                } else {
                    this.maioresValores.add(this.segundaPosicao);
                    this.maioresValores.add(this.primeiraPosicao);
                }
            }
        }
    }

    private void adicionarMaioresValoresStraight() {

        if (this.quartaPosicao == 3 && quintaPosicao == 12) {
            // Entra aqui quando os tipos se misturam: 2 3 4 5 A
            this.maioresValores.add(this.quartaPosicao);
        } else {
            //  Retorna quando os tipos seguem uma seguencia numerica
            this.maioresValores.add(this.quintaPosicao);
        }
    }

    private void adicionarMaioresValoresDoisPares() {

        // da primeira e a segunda carta, e também da terceira e a quarta carta
        if (this.primeiraPosicao == this.segundaPosicao && this.terceiraPosicao == this.quartaPosicao) {
            this.maioresValores.add(this.terceiraPosicao);
            this.maioresValores.add(this.primeiraPosicao);
            this.maioresValores.add(this.quintaPosicao);
        }

        // da primeira e a segunda carta, e também da quarta e a quinta carta
        if (this.primeiraPosicao == this.segundaPosicao && this.quartaPosicao == this.quintaPosicao) {
            this.maioresValores.add(this.quartaPosicao);
            this.maioresValores.add(this.primeiraPosicao);
            this.maioresValores.add(this.terceiraPosicao);
        }

        // da segunda e a terceira carta, e também da quarta e a quinta carta
        if (this.segundaPosicao == this.terceiraPosicao && this.quartaPosicao == this.quintaPosicao) {
            this.maioresValores.add(this.quartaPosicao);
            this.maioresValores.add(this.segundaPosicao);
            this.maioresValores.add(this.primeiraPosicao);
        }

    }

    private void adicionarMaioresValoresUmPar() {
        if (this.primeiraPosicao == this.segundaPosicao) {
            this.maioresValores.add(this.primeiraPosicao);
            this.maioresValores.add(this.quintaPosicao);
            this.maioresValores.add(this.quartaPosicao);
            this.maioresValores.add(this.terceiraPosicao);
        }
        if (this.segundaPosicao == this.terceiraPosicao) {
            this.maioresValores.add(this.segundaPosicao);
            this.maioresValores.add(this.quintaPosicao);
            this.maioresValores.add(this.quartaPosicao);
            this.maioresValores.add(this.primeiraPosicao);
        }
        if (this.terceiraPosicao == this.quartaPosicao) {
            this.maioresValores.add(this.terceiraPosicao);
            this.maioresValores.add(this.quintaPosicao);
            this.maioresValores.add(this.segundaPosicao);
            this.maioresValores.add(this.primeiraPosicao);
        }
        if (this.quartaPosicao == this.quintaPosicao) {
            this.maioresValores.add(this.quartaPosicao);
            this.maioresValores.add(this.terceiraPosicao);
            this.maioresValores.add(this.segundaPosicao);
            this.maioresValores.add(this.primeiraPosicao);
        }
    }

    private void stringCartasParaObjetoCartas(String[] cartas, List<Carta> hand) {
        for (String cartaString : cartas) {
            String valorCarta = StringUtils.substring(cartaString, 0, 1);
            NaipeCartaEnum naipe = NaipeCartaEnum.find(StringUtils.substring(cartaString, 1, 2));
            RankCartaEnum rank = RankCartaEnum.find(StringUtils.substring(cartaString, 0, 1));
            hand.add(new Carta(valorCarta, naipe, rank));
        }
    }

    private void atribuirPosicoes() {
        this.primeiraPosicao = this.posicoes.get(0);
        this.segundaPosicao = this.posicoes.get(1);
        this.terceiraPosicao = this.posicoes.get(2);
        this.quartaPosicao = this.posicoes.get(3);
        this.quintaPosicao = this.posicoes.get(4);
    }
}
