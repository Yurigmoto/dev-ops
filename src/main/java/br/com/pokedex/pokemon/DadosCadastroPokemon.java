package br.com.pokedex.pokemon;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroPokemon(
		@NotBlank
		String nome,
		Integer numeroPokedex,
		Integer altura,
		Integer peso,
		Integer experienciaBase,
		String imagemUrl) {
}
