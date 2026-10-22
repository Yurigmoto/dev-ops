package br.com.pokedex.tipo;

import org.springframework.stereotype.Component;

@Component
public class TipoMapper {

    // Converte a Entidade para o DTO de listagem seguro
    public DadosListagemTipo toListagemDto(Tipo tipo) {
        return new DadosListagemTipo(tipo);
    }

    // Atualiza a Entidade original com os dados preenchidos no formulário
    public void updateEntityFromForm(Tipo entidade, Tipo formInput) {
        entidade.setNome(formInput.getNome());
    }
}