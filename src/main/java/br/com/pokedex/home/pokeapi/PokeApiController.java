package br.com.pokedex.home.pokeapi;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.pokedex.pokemon.Pokemon;
import br.com.pokedex.pokemon.PokemonRepository;
import br.com.pokedex.tipo.Tipo;
import br.com.pokedex.tipo.TipoService;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/pokeapi")
public class PokeApiController {

	@Autowired
	private PokeApiService pokeApiService;

	@Autowired
	private TipoService tipoService;

	@Autowired
	private PokemonRepository pokemonRepository;

	@GetMapping("/buscar")
	public String exibirPaginaBusca(@RequestParam(value = "termo", required = false) String termo, Model model) {
		if (termo != null && !termo.isBlank()) {
			try {
				String resultado = pokeApiService.buscarPokemonPorNomeOuId(termo);
				DadosResultadoPokeApi encontrado = JsonParser.extrairResultadoBusca(resultado);
				model.addAttribute("resultado", encontrado);
				model.addAttribute("termo", termo);
			} catch (Exception e) {
				// PokeAPI devolve 404 quando o nome/numero não existe
				model.addAttribute("erro", "Pokemon não encontrado. Confira o nome (ex: pikachu) ou o número da pokedex.");
			}
		}
		return "pokeapi/buscar";
	}

	@PostMapping("/importar")
	@Transactional
	public String importar(@RequestParam("termo") String termo,
			RedirectAttributes redirectAttributes) {
		String message;
		try {
			String resultado = pokeApiService.buscarPokemonPorNomeOuId(termo);
			Pokemon pokemon = JsonParser.extrairPokemon(resultado);

			if (pokemonRepository.existsByNumeroPokedex(pokemon.getNumeroPokedex())) {
				message = "O pokemon '" + pokemon.getNome() + "' já consta na base de dados";
			} else {
				// CORRIGIDO em relação ao bug do projeto original: aqui a lista de tipos
				// reaproveitados/persistidos é a que realmente é usada no save final
				// (no projeto original essa lista era montada e depois descartada).
				List<Tipo> tiposTemp = pokemon.getTipos();
				pokemon.setTipos(new ArrayList<>());
				Pokemon pokemonSalvo = pokemonRepository.save(pokemon);

				List<Tipo> tiposPersistidos = new ArrayList<>();
				for (Tipo tipo : tiposTemp) {
					Tipo tipoExistente = tipoService.findByNome(tipo.getNome());
					if (tipoExistente != null) {
						tiposPersistidos.add(tipoExistente); // Reaproveita tipo já existente
					} else {
						Tipo novoTipo = tipoService.save(tipo); // Cria tipo novo
						if (novoTipo != null) {
							tiposPersistidos.add(novoTipo);
						}
					}
				}
				pokemonSalvo.setTipos(tiposPersistidos);
				pokemonRepository.save(pokemonSalvo);
				message = "Pokemon '" + pokemon.getNome() + "' importado com sucesso!";
			}
		} catch (Exception e) {
			message = "Erro ao importar da PokeAPI: " + e.getMessage();
		}

		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/pokeapi/buscar?termo=" + termo;
	}
}
