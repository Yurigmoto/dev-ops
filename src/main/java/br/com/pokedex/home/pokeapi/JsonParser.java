package br.com.pokedex.home.pokeapi;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pokedex.pokemon.Pokemon;
import br.com.pokedex.tipo.Tipo;

public class JsonParser {

	// Extrai só o essencial pra mostrar na tela de resultado da busca
	// (equivalente ao JsonParser.extracaoFilmeOmdb do projeto original,
	// mas aqui é 1 objeto só, não uma lista de "Search")
	public static DadosResultadoPokeApi extrairResultadoBusca(String json) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(json);

		String nome = root.path("name").asText();
		Integer numero = root.path("id").asInt();
		String imagemUrl = extrairMelhorImagem(root);

		return new DadosResultadoPokeApi(nome, numero, imagemUrl);
	}

	// Extrai o Pokemon inteiro para gravar no banco
	// (equivalente ao JsonParser.extracaoFilme do projeto original)
	public static Pokemon extrairPokemon(String json) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(json);

		Pokemon pokemon = new Pokemon();
		pokemon.setNome(root.path("name").asText());
		pokemon.setNumeroPokedex(root.path("id").asInt());
		pokemon.setAltura(root.path("height").asInt());
		pokemon.setPeso(root.path("weight").asInt());
		pokemon.setExperienciaBase(root.path("base_experience").asInt());
		pokemon.setImagemUrl(extrairMelhorImagem(root));

		// Desmembrando os tipos (equivalente aos "atores" no projeto original)
		List<Tipo> tipos = new ArrayList<>();
		for (JsonNode tipoNode : root.path("types")) {
			String nomeTipo = tipoNode.path("type").path("name").asText();
			tipos.add(new Tipo(nomeTipo));
		}
		pokemon.setTipos(tipos);

		return pokemon;
	}

	// A PokeAPI tem vários campos de imagem; preferimos a artwork oficial
	// (mais bonita) e caímos para o sprite padrão se não existir.
	private static String extrairMelhorImagem(JsonNode root) {
		JsonNode artwork = root.path("sprites").path("other").path("official-artwork").path("front_default");
		if (!artwork.isMissingNode() && !artwork.asText().isBlank()) {
			return artwork.asText();
		}
		return root.path("sprites").path("front_default").asText();
	}
}
