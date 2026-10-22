package br.com.pokedex.pokemon;

public record DadosListagemPokemon(
		Long id,
		String nome,
		Integer numeroPokedex,
		Integer altura,
		Integer peso,
		String imagemUrl,
		boolean desabilitado) {
	public DadosListagemPokemon(Pokemon pokemon) {
		this(
				pokemon.getId(),
				pokemon.getNome(),
				pokemon.getNumeroPokedex(),
				pokemon.getAltura(),
				pokemon.getPeso(),
				pokemon.getImagemUrl(),
				pokemon.isDesabilitado());
	}
}