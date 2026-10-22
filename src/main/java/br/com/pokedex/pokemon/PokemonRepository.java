package br.com.pokedex.pokemon;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PokemonRepository extends JpaRepository<Pokemon, Long> {

	// Busca um pokemon pelo próprio id, já trazendo os tipos (evita LazyInitializationException)
	@Query("SELECT p FROM Pokemon p JOIN FETCH p.tipos WHERE p.id = :id")
	Optional<Pokemon> findByIdComTipos(@Param("id") Long id);

	// não pelo id do próprio Pokemon, para trazer todos os pokemons de um tipo.
	@Query("SELECT p FROM Pokemon p JOIN p.tipos t WHERE t.id = :tipoId")
	List<Pokemon> buscarPorTipo(@Param("tipoId") Long tipoId);

	boolean existsByNumeroPokedex(Integer numeroPokedex);

	@Modifying
	@Query("UPDATE Pokemon p SET p.desabilitado = :desabilitado WHERE p.id = :id")
	void atualizaStatus(@Param("id") Long id, @Param("desabilitado") boolean desabilitado);

	List<Pokemon> findTop5ByOrderByNumeroPokedexDesc();
}
