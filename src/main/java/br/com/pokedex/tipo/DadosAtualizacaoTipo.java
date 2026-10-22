package br.com.pokedex.tipo;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoTipo(
		@NotNull
		Long id,
		String nome) {
}
