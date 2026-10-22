package br.com.pokedex.tipo;

public interface TipoRepository extends org.springframework.data.jpa.repository.JpaRepository<Tipo, Long> {

	Tipo findByNome(String nome);
}
