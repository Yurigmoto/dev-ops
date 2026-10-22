package br.com.pokedex.home.pokeapi;

// DTO simples só para exibir o resultado da busca na tela,
// equivalente ao DadosListagemOmdb do projeto original.
// Diferente da OMDb (que retorna uma LISTA de resultados parecidos),
// a PokeAPI só retorna 1 resultado exato por nome/numero, então
// aqui não precisamos de uma lista de resultados.
public record DadosResultadoPokeApi(
		String nome,
		Integer numeroPokedex,
		String imagemUrl) {
}
