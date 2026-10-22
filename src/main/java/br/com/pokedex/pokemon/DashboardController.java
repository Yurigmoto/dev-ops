package br.com.pokedex.pokemon;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

	private final PokemonService pokemonService;

	public DashboardController(PokemonService pokemonService) {
		this.pokemonService = pokemonService;
	}

	@GetMapping
	public String dashboard(Model model) {
		List<Pokemon> destaques = pokemonService.buscarDestaques();
		model.addAttribute("destaques", destaques);
		model.addAttribute("totalCadastrados", pokemonService.getAllPokemons().size());
		return "home/dashboard";
	}
}
