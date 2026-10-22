package br.com.pokedex.pokemon;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoPokemon( //dto basica
		@NotNull 
		Long id,
		String nome,
		Integer numeroPokedex) {
}
