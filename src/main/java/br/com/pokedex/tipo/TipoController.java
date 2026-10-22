package br.com.pokedex.tipo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.pokedex.pokemon.PokemonService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/tipo")
public class TipoController {

	@Autowired
	private TipoRepository tipoRepository;

	@Autowired
	private PokemonService pokemonService;
	
	@Autowired
	private TipoService tipoService;

	@Autowired
	private TipoMapper tipoMapper;

	@GetMapping("/formulario")
	public String carregaPaginaFormulario(Model model) {
		model.addAttribute("pokemons", pokemonService.getAllPokemons());
		model.addAttribute("tipo", new Tipo()); // Objeto vazio para o Thymeleaf preencher
		return "tipo/formulario";
	}

	@GetMapping("/formulario/{id}") 
	public String carregaPaginaFormulario(@PathVariable("id") Long id,
			RedirectAttributes redirectAttributes,
			Model model) {
		try {
			Tipo tipo = tipoRepository.findById(id)
					.orElseThrow(() -> new EntityNotFoundException("Tipo não encontrado"));
			model.addAttribute("pokemons", pokemonService.getAllPokemons());
			model.addAttribute("tipo", tipo);
			return "tipo/formulario";
		} catch (EntityNotFoundException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/tipo";
		}
	}

	@GetMapping 
	public String carregaPaginaListagem(Model model) {  
		// Entrega os DTOs em vez das entidades
		model.addAttribute("lista", tipoService.getAllTiposListagem());
		return "tipo/listagem";
	}

	@PostMapping("/salvar")
	@Transactional
	public String salvar(@Valid @ModelAttribute("tipo") Tipo tipoForm, RedirectAttributes redirectAttributes) {
		if (tipoForm.getId() != null) {
			Tipo tipoOriginal = tipoRepository.findById(tipoForm.getId())
					.orElseThrow(() -> new EntityNotFoundException("Tipo não encontrado"));
			tipoMapper.updateEntityFromForm(tipoOriginal, tipoForm);
			tipoRepository.save(tipoOriginal);
			redirectAttributes.addFlashAttribute("message", "Tipo atualizado com sucesso!");
		} else {
			tipoRepository.save(tipoForm);
			redirectAttributes.addFlashAttribute("message", "Tipo criado com sucesso!");
		}
		return "redirect:/tipo";
	}

	@GetMapping("/delete/{id}") // Alterado para GET para simplificar botões HTML
	@Transactional            
	public String remover(@PathVariable("id") Long id) {
		tipoRepository.deleteById(id);
		return "redirect:/tipo";
	}
}