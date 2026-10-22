package br.com.pokedex.tipo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TipoService {

	private final TipoRepository repository;
	private final TipoMapper tipoMapper; // Injeção do Mapper

	public TipoService(TipoRepository repository, TipoMapper tipoMapper) {
		this.repository = repository;
		this.tipoMapper = tipoMapper;
	}

	public List<Tipo> getAllTipos() {
		return repository.findAll(Sort.by("nome").ascending());
	}

	// Novo método que devolve apenas DTOs para o frontend
	public List<DadosListagemTipo> getAllTiposListagem() {
		return repository.findAll(Sort.by("nome").ascending())
				.stream()
				.map(tipoMapper::toListagemDto)
				.collect(Collectors.toList());
	}

	public Tipo getTipoById(Long id) {
		return repository.getReferenceById(id);
	}

	public Tipo save(Tipo tipo) {
		if (tipo.getNome() == null || tipo.getNome().trim().isEmpty()) {
			return null;
		}
		return repository.save(tipo);
	}

	public Tipo findByNome(String nome) {
		return repository.findByNome(nome);
	}
}