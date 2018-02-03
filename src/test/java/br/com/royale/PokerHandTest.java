package br.com.royale;

import dnl.utils.text.table.TextTable;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class PokerHandTest {

    private final ByteArrayOutputStream conteudoDeSaida = new ByteArrayOutputStream();
    private final ByteArrayOutputStream conteudoDeErro = new ByteArrayOutputStream();

    @Before
    public void setUp() {
//        System.setOut(new PrintStream(conteudoDeSaida));
//        System.setErr(new PrintStream(conteudoDeErro));
    }

    @After
    public void tearDown() {
//        System.setOut(System.out);
//        System.setErr(System.err);
    }

    private ResultEnum perdeu = ResultEnum.LOSS;
    private ResultEnum ganhou = ResultEnum.WIN;
    private ResultEnum empate = ResultEnum.DRAW;

    @Test
    public void RoyalFlushGanhaDeFlush() {
        Hand1VSHand2Test("Maior straight flush ganha", ganhou, "TD JD QD KD AD", "2S 8S 4S QS JS");
    }

    @Test
    public void StraightFlushGanhaDeFlush() {
        Hand1VSHand2Test("Maior straight flush ganha", ganhou, "2H 3H 4H 5H 6H", "2S 8S 4S QS JS");
    }

    @Test
    public void StraightFlushGanhaDeQuadra() {
        Hand1VSHand2Test("Straight flush ganha de quadra", ganhou, "2H 3H 4H 5H 6H", "AS AD AC AH JD");
    }

    @Test
    public void MaiorQuadraGanha() {
        Hand1VSHand2Test("Maior quadra ganha", ganhou, "AS AH 2H AD AC", "JS JD JC JH 3D");
    }

    @Test
    public void FullHousePerdeDeQuadra() {
        Hand1VSHand2Test("Quadra ganha de full house", perdeu, "2S AH 2H AS AC", "JS JD JC JH AD");
    }

    @Test
    public void FullHouseGanhaDeFlush() {
        Hand1VSHand2Test("Full house ganha de flush", ganhou, "2S AH 2H AS AC", "2H 3H 5H 6H 7H");
    }

    @Test
    public void MaiorFlushGanha() {
        Hand1VSHand2Test("Maior flush ganha", ganhou, "AS 3S 4S 8S 2S", "2H 3H 5H 6H 7H");
    }

    @Test
    public void FlushGanhaDeStraight() {
        Hand1VSHand2Test("Flush ganha de straight", ganhou, "2H 3H 5H 6H 7H", "2S 3H 4H 5S 6C");
    }

    @Test
    public void StraightIguaisEmpatam() {
        Hand1VSHand2Test("Straight iguais empatam", empate, "2S 3H 4H 5S 6C", "3D 4C 5H 6H 2S");
    }

    @Test
    public void StraightGanhaDeTrinca() {
        Hand1VSHand2Test("Straight ganha de trinca", ganhou, "2S 3H 4H 5S 6C", "AH AC 5H 6H AS");
    }

    @Test
    public void TrincaGanhaDeDoisPares() {
        Hand1VSHand2Test("Trinca ganha de dois pares", perdeu, "2S 2H 4H 5S 4C", "AH AC 5H 6H AS");
    }

    @Test
    public void DoisParesGanhaDeUmPar() {
        Hand1VSHand2Test("2 Pares ganham de um par", ganhou, "2S 2H 4H 5S 4C", "AH AC 5H 6H 7S");
    }

    @Test
    public void MaiorParGanha() {
        Hand1VSHand2Test("Maior par ganha", perdeu, "6S AD 7H 4S AS", "AH AC 5H 6H 7S");
    }

    @Test
    public void ParGanhaDeNada() {
        Hand1VSHand2Test("Par ganha de nada", perdeu, "2S AH 4H 5S KC", "AH AC 5H 6H 7S");
    }

    @Test
    public void MaiorCartaPerde() {
        Hand1VSHand2Test("Maior carta perde", perdeu, "2S 3H 6H 7S 9C", "7H 3C TH 6H 9S");
    }

    @Test
    public void MaiorCartaGanha() {
        Hand1VSHand2Test("Maior carta ganha", ganhou, "4S 5H 6H TS AC", "3S 5H 6H TS AC");
    }

    @Test
    public void CartasIguaisEmpatam() {
        Hand1VSHand2Test("Cartas iguais empatam", empate, "2S AH 4H 5S 6C", "AD 4C 5H 6H 2C");
    }

    @Test
    public void Randomico1Test() {
        Hand1VSHand2Test("Randomico ganha", empate, "TH JH QH KH AH", "TC JC QC KC AC");
    }

    @Test
    public void Randomico2Test() {
        Hand1VSHand2Test("Randomico ganha", ganhou, "7C 7S KH 2H 7H", "7C 7S 3S 7H 5S");
    }

    @Test
    public void Randomico3Test() {
        Hand1VSHand2Test("Randomico ganha", ganhou, "4C 5C 9C 8C KC", "3S 8S 9S 5S KS");
    }

    @Test
    public void FullTest() {

        String[] listaCartasPokerHand1 = {
                "9C TC JC QC KC",
                "TC TH 5C 5H KH",
                "TS TD KC JC 7C",
                "7H 7C QC JS TS",
                "5S 5D 8C 7S 6H",
                "AS AD KD 7C 3D",
                "TS JS QS KS AS",
                "TS JS QS KS AS",
                "TS JS QS KS AS",
                "AC AH AS AS KS",
                "AC AH AS AS KS",
                "TC JS QC KS AC",
                "7H 8H 9H TH JH",
                "7H 8H 9H TH JH",
                "7H 8H 9H TH JH",
                "7H 8H 9H TH JH",
                "7H 8H 9H TH JH",
                "JH JC JS JD TH",
                "JH JC JS JD TH",
                "JH JC JS JD TH",
                "JH JC JS JD TH",
                "4H 5H 9H TH JH",
                "4H 5H 9H TH JH",
                "4H 5H 9H TH JH",
                "7C 8S 9H TH JH",
                "7C 8S 9H TH JH",
                "TS TH TD JH JD",
                "2S 3H 4D 5H 6D",
                "2S 3H 4H 5H 6D",
                "2H 3H 4H 5H 7H",
                "2S 2H 2D 5H 6D",
                "2H 3H 4H 5H 6H",
                "TH JH QH KH AH",
                "TH TH TH TH AS",
                "TH TH TH AH AS",
                "2H 4H 6H 8H AS",
                "2H 2H 2H AH AS",
                "2H 2H 5H AH AS",
                "2H 3C 3D 3S 6H",
                "AS 2S 5S 8S QS",
        };


        String[] listaCartasPokerHand2 = {
                "9C 9H 5C 5H AC",
                "9C 9H 5C 5H AC",
                "JS JC AS KC TD",
                "7D 7C JS TS 6D",
                "7D 7S 5S 5D JS",
                "AD AH KD 7C 4S",
                "AC AH AS AS KS",
                "TC JS QC KS AC",
                "QH QS QC AS 8H",
                "TC JS QC KS AC",
                "QH QS QC AS 8H",
                "QH QS QC AS 8H",
                "JH JC JS JD TH",
                "4H 5H 9H TH JH",
                "7C 8S 9H TH JH",
                "TS TH TD JH JD",
                "JH JD TH TC 4C",
                "4H 5H 9H TH JH",
                "7C 8S 9H TH JH",
                "TS TH TD JH JD",
                "JH JD TH TC 4C",
                "7C 8S 9H TH JH",
                "TS TH TD JH JD",
                "JH JD TH TC 4C",
                "TS TH TD JH JD",
                "JH JD TH TC 4C",
                "JH JD TH TC 4C",
                "5H 6D 7H 8C 9C",
                "2S 3H 4D 5H 6C",
                "2D 3D 4D 5D 8D",
                "5H 5D 5H 8C 9C",
                "5H 6H 7H 8H 9H",
                "TC JC QC KC AC",
                "9C 9C 9C 9C 2S",
                "9C 9C 9C 2C 2S",
                "3C 5C 6C 8C JS",
                "2C 2C 2C JC JS",
                "2C 2C 6C AC AS",
                "2C 3D 4D 5C 6C",
                "KS JS 5S 8S QS",
        };

        ResultEnum[] listaRestulatos = {
                ResultEnum.WIN,
                ResultEnum.WIN,
                ResultEnum.LOSS,
                ResultEnum.WIN,
                ResultEnum.LOSS,
                ResultEnum.LOSS,
                ResultEnum.WIN,
                ResultEnum.WIN,
                ResultEnum.WIN,
                ResultEnum.WIN,
                ResultEnum.WIN,
                ResultEnum.WIN,
                ResultEnum.WIN,
                ResultEnum.WIN,
                ResultEnum.WIN,
                ResultEnum.WIN,
                ResultEnum.WIN,
                ResultEnum.WIN,
                ResultEnum.WIN,
                ResultEnum.WIN,
                ResultEnum.WIN,
                ResultEnum.WIN,
                ResultEnum.LOSS,
                ResultEnum.WIN,
                ResultEnum.LOSS,
                ResultEnum.WIN,
                ResultEnum.WIN,
                ResultEnum.LOSS,
                ResultEnum.DRAW,
                ResultEnum.LOSS,
                ResultEnum.LOSS,
                ResultEnum.LOSS,
                ResultEnum.DRAW,
                ResultEnum.WIN,
                ResultEnum.WIN,
                ResultEnum.WIN,
                ResultEnum.WIN,
                ResultEnum.LOSS,
                ResultEnum.LOSS,
                ResultEnum.WIN,
        };

        String[] columnNames = {
                "pokerHand1",
                "pokerHand2",
                "resultado",
        };


        if (listaCartasPokerHand1.length == listaCartasPokerHand2.length) {

            Object[][] matrix = new Object[listaCartasPokerHand1.length][columnNames.length];
            for (int i = 0; i < matrix.length; i++) {
                matrix[i][0] = listaCartasPokerHand1[i];
                matrix[i][1] = listaCartasPokerHand2[i];
                matrix[i][2] = new PokerHandOO(listaCartasPokerHand1[i]).compareWith(new PokerHandOO(listaCartasPokerHand2[i])).toString();
            }
            TextTable tt = new TextTable(columnNames, matrix);
            // adiciona numeros ao lado esquerdo de cada coluna
            tt.setAddRowNumbering(true);
            // sort pela terceira coluna
            tt.setSort(2);
            tt.printTable();


//            for (int i = 0; i < matrix.length; i++) {
//
//                Hand1VSHand2Test("É esperado que", listaRestulatos[i], listaCartasPokerHand2[i], "JH 8S TH AH QH");
//            }

        }

    }

    private void Hand1VSHand2Test(String descricao, ResultEnum expected, String pokerHand1, String pokerHand2) {
        System.out.println(descricao + ": " + expected.toString());
        Assert.assertEquals(descricao + ": ", expected.toString(),  new PokerHandOO(pokerHand1).compareWith(new PokerHandOO(pokerHand2)).toString());
//        Assert.assertEquals(descricao + ": ", expected.toString(),  new PokerHand(pokerHand1).compareWith(new PokerHand(pokerHand2)).toString());
    }

}
