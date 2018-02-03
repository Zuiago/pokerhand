package br.com.royale;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/*
Boa tarde, Iago.


Todos os testes passaram com sucesso, entretanto, os seguintes problemas foram encontrados:

- Os testes deveriam utilizar assert para fazer a verificação real dos resultados. Foi necessário pegar a tabela gerada e comparar com os resultados esperados para verificar se os testes estavam passando.
- Houve uso direto do ArrayList na definição de variáveis e parâmetros de métodos. Sempre deve-se dar preferência para utilizar uma abstração no lugar da classe concreta (List ou Collection nesse caso). Código criado utilizando a implementação no lugar da interface acaba ficando acoplado a essa implementação. Um exemplo seria um método que recebe uma ArrayList. Caso uma outra parte do código tivesse um LinkedList, seria necessário converter para ArrayList para invocar tal método.
- Não foi criada uma abstração para representar uma Carta (a qual teria seu valor e naipe). Ao invés disso, as cartas foram mantidas como texto. Além disso gerar um problema de representação, também gera muitos dos problemas subsequentes.
- Os atributos expostos por uma PokeHandOO (através de getter) ficaram confusos. Para o restante do código, uma PokeHandOO tem um construtor que aceita String e dois atributos: rank (inteiro) e maiores naipes (uma lista de inteiros). Isso demonstra mais um problema de representação, dessa vez do objeto PokeHandOO. O que se esperaria de uma mão de poker seria um atributo com uma coleção de cartas presentes nela. Poderia também possuir um atributo com sua categoria (royal flush, dupla, etc.).
- Foi utilizado inteiro para representar o "rank" da carta. Dessa forma, fica confuso para ler códigos que manipulem essa variável, visto que deve-se consultar o construtor ou os comentários para saber o que cada número se refere. Deve-se notar que o código deve ser claro o suficiente sem que sejam necessários comentários. Os comentários deveriam ser exceção, utilizados apenas para esclarecer casos mais complexos e não para identificar o que significa o que é um valor de uma variável.
- O método compare() recebe desnecessariamente o tamanho das listas. Visto que ele é invocado apenas em caso de empate de categoria, é esperado que as listas possuam o mesmo tamanho.
- Houve muita repetição de código e quase nenhuma reutilização de código (exceto pelo teste de isStraight e isFlush). Cada método verificava manualmente utilizando índices fixos cada possível combinação dos tipos. Isso gerou repetição de código até dentro de um mesmo código. Um exemplo é o isTrinca(), que tem exatamente o mesmo código três vezes, mudando apenas os índices utilizados. Isso se repetiu em praticamente todos os métodos, ficando ainda mais evidente no método isUmPar(), que repete o mesmo código quatro vezes. Além dessa repetição interna nos métodos, não foi reutilizado código para decidir as categorias. Vale notar que existem apenas três lógicas globais para decidir se uma mão de poker está em uma categoria: straight, flush e grupo (cartas de mesmo naipe). Logo, se fossem criados métodos com essas três lógicas, todos os outros métodos ficariam muito mais simples. O método de quadra poderia verificar se existe um grupo de 4 cartas, o de duas duplas verificaria se existem 2 grupos de 2 cartas, o de full house verificaria se existe um grupo de 2 cartas e um grupo de 3 cartas, etc. Logo, havia muita oportunidade para reutilização de código.
- Sobre o uso direto de índices nas categorias, ainda vale lembrar que existem formas mais eficientes e legíveis de se fazer operações em coleções. Recomenda-se o uso de Stream do Java 8 ou técnicas parecidas. Um exemplo seria o uso do groupBy() que automaticamente agrupa objetos de uma lista baseado em um dos atributos.


use streams do java8
faça metodos pequenos e com responsabilidades bem divididas
crie a classe Card rsrsrs
no mais é isso que eu te aconselharia
pense em manutenabilidade e facilidade de evoluir a solução
com esses parametros vc vai fazer um bom código

ahhh
tem uma coisa
use interfaces cara
principalmente pra referenciar estrutura de dados
c tava usando ArrayList pra declarar variavel
não faça isso
rsrsrsrs

*/


public class PokerHandOO {

    private final List<Carta> hand = new ArrayList<Carta>();
    private CategoriaPokerHandEnum categoriaPokerHandEnum;
    // quarda os tipos de cartas descendentemente
    private ArrayList<Integer> maioresNaipes = new ArrayList<Integer>();

    /**
     * Constructor
     *
     * @param stringCartas
     */
    public PokerHandOO(String stringCartas) {
        stringCartasParaObjetoCartas(StringUtils.split(stringCartas, " "), this.hand);
        sortCartasPorTipo(this.hand);
        this.categoriaPokerHandEnum = determinePokerRank(this.hand);

    }

    public CategoriaPokerHandEnum determinePokerRank(List<Carta> cartas) {
        if (isRoyalFlush(cartas)) {
            return CategoriaPokerHandEnum.ROYAL_FLUSH;
        } else if (isStraightFlush(cartas)) {
            return CategoriaPokerHandEnum.STRAIGHT_FLUSH;
        } else if (isQuadra(cartas)) {
            return CategoriaPokerHandEnum.QUADRA;
        } else if (isFullHouse(cartas)) {
            return CategoriaPokerHandEnum.FULL_HOUSE;
        } else if (isFlush(cartas)) {
            return CategoriaPokerHandEnum.FLUSH;
        } else if (isStraight(cartas)) {
            return CategoriaPokerHandEnum.STRAIGHT;
        } else if (isTrinca(cartas)) {
            return CategoriaPokerHandEnum.TRINCA;
        } else if (isDoisPares(cartas)) {
            return CategoriaPokerHandEnum.DOIS_PARES;
        } else if (isUmPar(cartas)) {
            return CategoriaPokerHandEnum.UM_PAR;
        } else {
            return CategoriaPokerHandEnum.CARTA_ALTA;
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

    private static ResultEnum compare(ArrayList<Integer> lista1, ArrayList<Integer> lista2, int tamanho) {
        for (int i = 0; i < tamanho; i++) {
            if (lista1.get(i) > lista2.get(i)) {
                return ResultEnum.WIN;
            } else if (lista1.get(i) < lista2.get(i)) {
                return ResultEnum.LOSS;
            }
        }

        return ResultEnum.DRAW;
    }


    public ResultEnum compareWith(PokerHandOO pokeHandOO) {

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
                    return compare(this.getMaioresNaipes(), pokeHandOO.getMaioresNaipes(), 1);
                case QUADRA:
                case FULL_HOUSE:
                    // No caso de  Quadra e Full House
                    return compare(this.getMaioresNaipes(), pokeHandOO.getMaioresNaipes(), 2);
                case TRINCA:
                case DOIS_PARES:
                    // No caso de Trinca e Dois pares
                    return compare(this.getMaioresNaipes(), pokeHandOO.getMaioresNaipes(), 3);
                case UM_PAR:
                    // No caso de Um par
                    return compare(this.getMaioresNaipes(), pokeHandOO.getMaioresNaipes(), 4);
                case FLUSH:
                case CARTA_ALTA:
                    // No caso de Flush e Carta Alta
                    return compare(this.getMaioresNaipes(), pokeHandOO.getMaioresNaipes(), 5);
                default:
                    return ResultEnum.DRAW;
            }
        }
    }


    public boolean isRoyalFlush(List<Carta> cartas) {
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
     * @return boolean
     */
    private boolean isStraightFlush(List<Carta> cartas) {
        return isFlush(cartas) && isStraight(cartas);
    }

    /**
     * Verifica se a mão é categorizada como Straight.
     * Cinco cartas de naipes diferentes em sequência
     * @return boolean
     */
    public boolean isStraight(List<Carta> cartas) {
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
     * Verifica se a mão é categorizada como flush
     * Cinco cartas de mesmos naipes, não em sequência
     * @return boolean
     */
    public boolean isFlush(List<Carta> cartas) {
        return cartas.stream().allMatch(e -> e.getNaipe().getValor().equals(cartas.get(0).getNaipe().getValor()));
    }

    /**
     * Verifica se a mão é categorizada como Quadra
     * Quatro cartas de mesmo valor e uma outra carta como ’kicker’.
     * @return boolean
     */
    public boolean isQuadra(List<Carta> cartas) {
        cartas.stream().collect(Collectors.groupingBy(c -> c.getNaipe(), Collectors.counting()))
                .entrySet().stream().filter(t -> t.getValue() > 1).collect(toList());
        return false;
    }

    private boolean isTrinca(List<Carta> cartas) {
        int cardRepeats = 1;
        boolean isThreeOfAKind = false;
        int i = 0;
        int k = i + 1;
        while (i < cartas.size() && !isThreeOfAKind) {
            cardRepeats = 1;
            while (k < cartas.size() && !isThreeOfAKind) {
                if (cartas.get(i).getRank().getPosicao() == cartas.get(k).getRank().getPosicao()) {
                    cardRepeats++;
                    if (cardRepeats == 3) {
                        isThreeOfAKind = true;
                    }
                }
                k++;
            }
            i++;
        }
        return isThreeOfAKind;
    }

    private boolean isDoisPares(List<Carta> cartas) {
        int cardRepeats;
        int noOfCardRepeats = 0;
        boolean isTwoPair = false;
        int i = 0;
        int k = i + 1;
        while (i < cartas.size() && !isTwoPair) {
            cardRepeats = 1;
            while (k < cartas.size() && !isTwoPair) {
                if (cartas.get(i).getRank().getPosicao() == cartas.get(k).getRank().getPosicao()) {
                    cardRepeats++;
                    if (cardRepeats == 2) {
                        cardRepeats = 1;
                        noOfCardRepeats++;
                        if (noOfCardRepeats == 2) {
                            isTwoPair = true;

                        }
                    }

                }
                k++;
            }
            i++;
        }
        return isTwoPair;
    }

    private boolean isUmPar(List<Carta> cartas) {
        int cardRepeats;
        boolean isPair = false;
        int i = 0;
        int k = i + 1;
        while (i < cartas.size() && !isPair) {
            cardRepeats = 1;
            while (k < cartas.size() && !isPair) {
                if (cartas.get(i).getRank().getPosicao() == cartas.get(k).getRank().getPosicao()) {
                    cardRepeats++;
                    if (cardRepeats == 2) {
                        isPair = true;
                    }
                }
                k++;
            }
            i++;
        }
        return isPair;
    }
    public Comparator<Carta> byRank = (Carta left, Carta right) -> {
        if (left.getRank().getPosicao() < right.getRank().getPosicao()) {
            return -1;
        } else {
            return 1;
        }
    };

    private boolean isFullHouse(List<Carta> cartas) {
        int noOfRepeats = 1;
        boolean isThreeOfAKind = false;
        boolean isTwoOfAKind = false;
        for (int i = 0; i < cartas.size() - 1; i++) {
            if (cartas.get(i).getRank().getPosicao() == cartas.get(i + 1).getRank().getPosicao()) {
                noOfRepeats++;
                if (noOfRepeats == 3) {
                    isThreeOfAKind = true;
                    noOfRepeats = 1;
                } else if (noOfRepeats == 2) {
                    isTwoOfAKind = true;
                    noOfRepeats = 1;
                }
            } else {
                noOfRepeats = 1;
            }
        }
        return (isTwoOfAKind && isThreeOfAKind);

    }

    public Carta getHighCard(List<Carta> cartas) {
        return cartas.get(0);
    }

    private void sortCartasPorTipo(List<Carta> listaCartas) {
        listaCartas.sort(Carta::comparePorPosicao);
    }

    public ArrayList<Integer> getMaioresNaipes() {
        return this.maioresNaipes;
    }
}
