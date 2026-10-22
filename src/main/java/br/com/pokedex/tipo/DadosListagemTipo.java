package br.com.pokedex.tipo;

public record DadosListagemTipo(
		Long id,
		String nome) {
	public DadosListagemTipo(Tipo tipo) {
		this(tipo.getId(), tipo.getNome());
	}
}
