package br.com.pokedex.pokemon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.pokedex.tipo.Tipo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pokemon")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pokemon implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;
	private Integer numeroPokedex; 
	private Integer altura; // decimetros
	private Integer peso; // hectogramas
	private Integer experienciaBase;
	private String imagemUrl;
	private boolean desabilitado;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST) // fetchType.Lazy -> atualiza apenas quando requisitado
	@JoinTable(							   // cascadeType.Persist -> ao apagar um registro, registros relacionados se mantem
			name = "pokemon_tipo", 
			joinColumns = @JoinColumn(name = "pokemon_id"),    // tabela de associacao entre tipo e pokemon M:N
			inverseJoinColumns = @JoinColumn(name = "tipo_id") 
	)
	private List<Tipo> tipos = new ArrayList<>();

	public Pokemon(DadosCadastroPokemon dados) {    //construtores personalizados
		this.nome = dados.nome();
		this.numeroPokedex = dados.numeroPokedex();
	}

	public void atualizarInformacoes(DadosAtualizacaoPokemon dados) {
		if (dados.nome() != null)
			this.nome = dados.nome();
		if (dados.numeroPokedex() != null)
			this.numeroPokedex = dados.numeroPokedex();
	}
}
