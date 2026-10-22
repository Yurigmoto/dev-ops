package br.com.pokedex.tipo;

import java.util.List;

import br.com.pokedex.pokemon.Pokemon;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tipo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Tipo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	@ManyToMany(mappedBy = "tipos", fetch = FetchType.LAZY)
	private List<Pokemon> pokemons;

	public Tipo(DadosCadastroTipo dados) {
		this.nome = dados.nome();
	}

	public Tipo(String nome) {
		this.nome = nome;
	}

	public void atualizarInformacoes(DadosAtualizacaoTipo dados) {
		if (dados.nome() != null)
			this.nome = dados.nome();
	}
}
