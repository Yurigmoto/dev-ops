package br.com.pokedex.tipo;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroTipo(
		@NotBlank
		String nome) {
}
