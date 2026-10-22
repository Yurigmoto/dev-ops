package br.com.pokedex.pokemon;

import org.springframework.stereotype.Component;

@Component
public class PokemonMapper {

    // Converte de Entidade para o DTO que vai para a tabela do ecrã
    public DadosListagemPokemon toListagemDto(Pokemon pokemon) {
        return new DadosListagemPokemon(pokemon);
    }

    // Atualiza a Entidade existente com os dados vindos do formulário Thymeleaf
    public void updateEntityFromForm(Pokemon entidade, Pokemon formInput) {
        entidade.setNome(formInput.getNome());
        entidade.setNumeroPokedex(formInput.getNumeroPokedex());
        entidade.setAltura(formInput.getAltura());
        entidade.setPeso(formInput.getPeso());
        entidade.setExperienciaBase(formInput.getExperienciaBase());
        entidade.setImagemUrl(formInput.getImagemUrl());
        // A lista de tipos e o status 'desabilitado' são mantidos intactos aqui
    }
}