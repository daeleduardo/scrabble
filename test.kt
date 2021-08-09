import java.io.File
import java.text.Normalizer
/*Creditos
https://homepages.dcc.ufmg.br/~camarao/cursos/pc/2016a/Lista-de-Palavras.txt
 */
/**
 * Para calcular a melhor jogada possível, a resolução ideal seria através de banco de dados:
 * Criando quatro tabelas ex.: letras, palavras ,ordem_letras_palavras  ponto_por_letras
 * Deste modo tudo poderia ser resolvido por uma única consulta. ;)
 *
 * A partir desta linha de raciocínio a ideia é,
 * criar um fluxo que usasse classes que retornariam "mock's" de possíveis consultas
 *
 */
fun main() {

   // converte a string recebida em array para remover duplicados.
   val parametro: String = "xqueijoQuartoç"

    // Na string recebida: remove acentos, converte para minusculo, aplicar o "trim", converter em array para remover duplicados
    REMOVER TRATAMENTO DE DUPLICADOS!!!
val pechincha: String = Normalizer.normalize(parametro, Normalizer.Form.NFD)
      .replace("[^\\p{ASCII}]".toRegex(), "")
      .trim()
      .toLowerCase()
      .toCharArray()
      .distinct()
      .joinToString("")

   // Busca o "cenario atual"  do jogo e adiciona a nova informação que é a pechincha.
   val dadosJogo = DadosJogo(pechincha)
   // Insere as informações do jogo na classe que executa as ações e por fim executa a jogada desejada.
   val acoesJogo = AcoesJogo(dadosJogo)
   println(acoesJogo.executarJogada())
}

// Registro de palavras, foi criada a classe para garantir que tenha estes quatro atributos iniciais 
data class RegistroPalavra(val palavra: String, val pontuacao: Int, val tamanho: Int, val palavraExpurgoPechincha: String)

// Classe que carrega as informações do jogo
data class DadosJogo(var pechinchas: String) {

   val mapaLetraPontuacao = mapOf(
   "e" to 1, "a" to 1, "i" to 1, "o" to 1, "n" to 1, "r" to 1, "t" to 1, "l" to 1, "s" to 1, "u" to 1,
   "w" to 2, "d" to 2, "g" to 2,
   "b" to 3, "c" to 3, "ç" to 3, "m" to 3, "p" to 3,
   "f" to 4, "h" to 4, "v" to 4,
   "j" to 8, "x" to 8,
   "q" to 10, "z" to 10)

   val arquivoPalavras = File("./dicionario.txt")
}

// Classe que executa as ações do jogo com base nos dados informados.
class AcoesJogo(val dadosJogo: DadosJogo) {

   // Verifica se todas as letras da palavra estão contidas no conjunto de letras da pechincha
   private fun contemLetrasPechincha(palavra: String): Boolean {
      VERIFICAR SE QUANDIDADE DE LETRAS E SUFICIENTE PARA MONTAR A PALAVRA 
      return Regex("[${dadosJogo.pechinchas}]", RegexOption.IGNORE_CASE).findAll(palavra).count() == palavra.length
   }

   // Verifica as letras da pechincha que não estão na palavra
   private fun palavraExpurgoPechincha(palavra: String): String {
      return dadosJogo.pechinchas.replace(Regex("$palavra", RegexOption.IGNORE_CASE), "")
   }

   // Calcula a pontuação da palavra com base em suas letras
   private fun pontuacaoPalavra(palavra: String): Int {

      var pontuacao: Int = 0

      for (letra in palavra) {
         pontuacao += dadosJogo.mapaLetraPontuacao.getOrDefault(letra.toString(), 0)
      }

      return pontuacao
   }

   // "Executa a jogada" buscando a melhor palavra possível
   public fun executarJogada(): RegistroPalavra {
      // Cria objeto que será retornado com a palavra encontrada
      var palavraEncontrada: RegistroPalavra = RegistroPalavra("", 0, 0, "")
      /**
       * Executa a lógica que seria executa em uma consulta de banco de dados,
       * percorrendo os registros das palavras cadastradas.
       */

      dadosJogo.arquivoPalavras.forEachLine(Charsets.UTF_8) {
         palavra ->

         // Se nem todas as letras da palavra estão contidas na pechincha pula o registro
         if (!contemLetrasPechincha(palavra)) {
            return@forEachLine
         }
         // Monta o objeto da palavra para validação
         val dados = RegistroPalavra(palavra, pontuacaoPalavra(palavra), palavra.length, palavraExpurgoPechincha(palavra))

         /**
          * Verifica se a palavra do registro atual possui maior pontuação que a maior já encontrada,
          * ou se possui mesma pontuação porém em tamanho menor.
          * Caso uma destas condições seja verdadeira assume-se que seja a melhor palavra
          */
         if (dados.pontuacao > palavraEncontrada.pontuacao ||
            (dados.pontuacao == palavraEncontrada.pontuacao && dados.tamanho < palavraEncontrada.tamanho)) {
            palavraEncontrada = dados
         }
      }

      return palavraEncontrada
   }
}
