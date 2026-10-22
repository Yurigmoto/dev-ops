package br.com.pokedex.pokemon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/pokemon")
public class PokemonController {

	@Autowired
	private PokemonRepository pokemonRepository;
	
	@Autowired
	private PokemonService pokemonService;

	@Autowired
	private PokemonMapper pokemonMapper;

	@GetMapping("/formulario/{id}")
	public String carregaPaginaFormulario(@PathVariable("id") Long id,
			RedirectAttributes redirectAttributes,
			Model model) {
		try {
			Pokemon pokemon = pokemonRepository.findById(id)
					.orElseThrow(() -> new EntityNotFoundException("Pokemon não encontrado"));
			model.addAttribute("pokemon", pokemon);
			return "pokemon/formulario";
		} catch (EntityNotFoundException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/pokemon";
		}
	}

	@GetMapping("/formulario")
	public String novoPokemon(Model model) {
		model.addAttribute("pokemon", new Pokemon());
		return "pokemon/formulario";
	}

	@GetMapping 
	public String carregaPaginaListagem(Model model) {
		model.addAttribute("activePage", "pokemon");
		// Utiliza o serviço para enviar apenas os DTOs blindados para o ecrã
		model.addAttribute("lista", pokemonService.getAllPokemonsListagem());
		return "pokemon/listagem";
	}

	@PostMapping("/salvar") 
	public String salvarPokemon(@Valid @ModelAttribute("pokemon") Pokemon pokemonForm, 
			BindingResult result, 
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "pokemon/formulario";
		}
		try {
			if (pokemonForm.getId() != null) {
				// Atualização através do Mapper
				Pokemon pokemonOriginal = pokemonRepository.findById(pokemonForm.getId())
						.orElseThrow(() -> new EntityNotFoundException("Pokemon não encontrado"));
				pokemonMapper.updateEntityFromForm(pokemonOriginal, pokemonForm);
				pokemonRepository.save(pokemonOriginal);
				redirectAttributes.addFlashAttribute("message", "Pokemon '" + pokemonOriginal.getNome() + "' atualizado com sucesso!");
			} else {
				// Criação nova
				pokemonRepository.save(pokemonForm);
				redirectAttributes.addFlashAttribute("message", "Pokemon '" + pokemonForm.getNome() + "' criado com sucesso!");
			}
			return "redirect:/pokemon";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Erro ao salvar pokemon: " + e.getMessage());
			return "redirect:/pokemon/formulario" + (pokemonForm.getId() != null ? "/" + pokemonForm.getId() : "");
		}
	}

	@GetMapping("/{id}/desabilitado/{status}")
	public String updatePokemonDesabilitado(@PathVariable("id") Long id,
			@PathVariable("status") boolean desabilitado,
			RedirectAttributes redirectAttributes) {
		try {
			pokemonRepository.atualizaStatus(id, desabilitado);
			String status = desabilitado ? "desabilitado" : "habilitado";
			redirectAttributes.addFlashAttribute("message", "O pokemon id=" + id + " foi " + status);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/pokemon";
	}

	@GetMapping("/delete/{id}") 
	public String deletePokemon(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		try {
			pokemonRepository.deleteById(id);
			redirectAttributes.addFlashAttribute("message", "O pokemon " + id + " foi apagado!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/pokemon";
	}
}