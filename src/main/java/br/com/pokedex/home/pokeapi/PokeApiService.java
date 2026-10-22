package br.com.pokedex.home.pokeapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PokeApiService {

	// A sintaxe com ':' define a URL padrão caso o application.properties não seja lido
	@Value("${pokeapi.url:https://pokeapi.co/api/v2}")
	private String pokeApiUrl;

	private final RestTemplate restTemplate = new RestTemplate();

	public String buscarPokemonPorNomeOuId(String termo) {
		String termoNormalizado = termo.trim().toLowerCase();
		String url = pokeApiUrl + "/pokemon/" + termoNormalizado;
		return restTemplate.getForObject(url, String.class);
	}
}