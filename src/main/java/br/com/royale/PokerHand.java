package br.com.royale;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

/*
Boa tarde, Iago.


Todos os testes passaram com sucesso, entretanto, os seguintes problemas foram encontrados:

- Os testes deveriam utilizar assert para fazer a verificação real dos resultados. Foi necessário pegar a tabela gerada e comparar com os resultados esperados para verificar se os testes estavam passando.
- Houve uso direto do ArrayList na definição de variáveis e parâmetros de métodos. Sempre deve-se dar preferência para utilizar uma abstração no lugar da classe concreta (List ou Collection nesse caso). Código criado utilizando a implementação no lugar da interface acaba ficando acoplado a essa implementação. Um exemplo seria um método que recebe uma ArrayList. Caso uma outra parte do código tivesse um LinkedList, seria necessário converter para ArrayList para invocar tal método.
- Não foi criada uma abstração para representar uma Carta (a qual teria seu valor e naipe). Ao invés disso, as cartas foram mantidas como texto. Além disso gerar um problema de representação, também gera muitos dos problemas subsequentes.
- Os atributos expostos por uma PokerHand (através de getter) ficaram confusos. Para o restante do código, uma PokerHand tem um construtor que aceita String e dois atributos: rank (inteiro) e maiores naipes (uma lista de inteiros). Isso demonstra mais um problema de representação, dessa vez do objeto PokerHand. O que se esperaria de uma mão de poker seria um atributo com uma coleção de cartas presentes nela. Poderia também possuir um atributo com sua categoria (royal flush, dupla, etc.).
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
    private final Collection<Carta> hand;

    private CategoriaPokerHandEnum categoriaPokerHandEnum;
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
        if (this.categoriaPokerHandEnum.getRank() > hand.categoriaPokerHandEnum.getRank()) {
            return Result.WIN;
        } else if (this.categoriaPokerHandEnum.getRank() < hand.categoriaPokerHandEnum.getRank()) {
            return Result.LOSS;
        } else {
            switch (this.categoriaPokerHandEnum) {
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


    public ArrayList < Integer > getMaioresNaipes() {
        return this.maioresNaipes;
    }

    private int getKind(String carta) {
        int kind = Arrays.asList(TIPOS).indexOf(StringUtils.substring(carta, 0, 1));
        return kind;
    }
}
