package br.com.pokedex.pokemon;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PokemonService {

	private final PokemonRepository pokemonRepository;
	private final PokemonMapper pokemonMapper; // Injetando o Mapper

	public PokemonService(PokemonRepository pokemonRepository, PokemonMapper pokemonMapper) {
		this.pokemonRepository = pokemonRepository;
		this.pokemonMapper = pokemonMapper;
	}

	// Agora devolve uma lista de DTOs, não de Entidades
	public List<DadosListagemPokemon> getAllPokemonsListagem() {
		return pokemonRepository.findAll(Sort.by("nome").ascending())
				.stream()
				.map(pokemonMapper::toListagemDto)
				.collect(Collectors.toList());
	}

	public Pokemon getPokemonById(Long id) {
		return pokemonRepository.getReferenceById(id);
	}

	public List<Pokemon> findAllById(List<Long> ids) {
		return pokemonRepository.findAllById(ids);
	}

	public List<Pokemon> buscarDestaques() {
		return pokemonRepository.findTop5ByOrderByNumeroPokedexDesc();
	}

	public List<Pokemon> buscarPorTipo(Long tipoId) {
		return pokemonRepository.buscarPorTipo(tipoId);
	}
	
	public List<Pokemon> getAllPokemons() {
		return pokemonRepository.findAll(Sort.by("nome").ascending());
	}
}