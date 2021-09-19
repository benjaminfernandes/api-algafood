package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.RestauranteConverter;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.api.model.view.RestauranteView;
import com.algaworks.algafood.api.openapi.controller.RestauranteControllerOpenApi;
import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController implements RestauranteControllerOpenApi {

	@Autowired
	private RestauranteRepository restauranteRepository;
	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	@Autowired
	private RestauranteConverter restauranteConverter;
	
	  @JsonView(RestauranteView.Resumo.class)
	  @GetMapping 
	  public List<RestauranteModel> listar(){ 
		  return restauranteConverter.toCollectionModel(restauranteRepository.findAll()); 
	  }
	  
	  @JsonView(RestauranteView.ApenasNome.class)
	  @GetMapping(params = "projecao=apenas-nome")
	  public List<RestauranteModel> listarApenasNomes(){ 
		  return listar(); 
	  }
	
	/*
	 * @GetMapping public MappingJacksonValue listar(@RequestParam(required = false)
	 * String projecao){ List<Restaurante> restaurantes =
	 * this.restauranteRepository.findAll(); List<RestauranteModel>
	 * restaurantesModel = restauranteConverter.toCollectionModel(restaurantes);
	 * 
	 * MappingJacksonValue restaurantesWrapper = new
	 * MappingJacksonValue(restaurantesModel);
	 * 
	 * restaurantesWrapper.setSerializationView(RestauranteView.Resumo.class);
	 * 
	 * if("apenas-nome".equals(projecao)) {
	 * restaurantesWrapper.setSerializationView(RestauranteView.ApenasNome.class);
	 * }else if("completo".equals(projecao)) {
	 * restaurantesWrapper.setSerializationView(null); }
	 * 
	 * return restaurantesWrapper; }
	 */
	
	@GetMapping("/{codigo}")
	public RestauranteModel buscar(@PathVariable Long codigo) {
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(codigo);
		
		return restauranteConverter.toModel(restaurante);
	}

	@PutMapping("/{restauranteId}")
	public RestauranteModel atualizar(@PathVariable Long restauranteId, @RequestBody @Valid RestauranteInput restauranteInput){

		try {
			//Restaurante restaurante = this.restauranteConverter.toDomain(restauranteInput);
			
			Restaurante restauranteAtual = this.cadastroRestaurante.buscarOuFalhar(restauranteId);
			
			restauranteConverter.copyToDomainObject(restauranteInput, restauranteAtual);
			
			//BeanUtils.copyProperties(restaurante, restauranteAtual, "id", "formasPagamento", "endereco", "dataCadastro", 
				//	"produtos");
			return restauranteConverter.toModel(cadastroRestaurante.salvar(restauranteAtual));
		}catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteModel adicionar(@RequestBody @Valid RestauranteInput restauranteInput){
		
		try {
			Restaurante restaurante = restauranteConverter.toDomain(restauranteInput);
			return restauranteConverter.toModel(cadastroRestaurante.salvar(restaurante));
		}catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}
	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long restauranteId) {
		cadastroRestaurante.ativar(restauranteId);
	}
	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativar(@PathVariable Long restauranteId) {
		this.cadastroRestaurante.inativar(restauranteId);
	}
	
	@PutMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativarMultiplos(@RequestBody List<Long> restauranteIds) {
		try {
			this.cadastroRestaurante.ativar(restauranteIds);
		}catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@DeleteMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativarMultiplos(@RequestBody List<Long> restauranteIds) {
		try {
			this.cadastroRestaurante.inativar(restauranteIds);
		}catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@PutMapping("/{restauranteId}/abertura")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void fechar(@PathVariable Long restauranteId) {
		this.cadastroRestaurante.abrir(restauranteId);
	}
	
	@PutMapping("/{restauranteId}/fechamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void abrir(@PathVariable Long restauranteId) {
		this.cadastroRestaurante.fechar(restauranteId);
	}

	/*
	@PatchMapping("/{restauranteId}")
	public RestauranteModel atualizarParcial(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos, HttpServletRequest request){
		
		Restaurante restauranteAtual = this.cadastroRestaurante.buscarOuFalhar(restauranteId);
		merge(campos, restauranteAtual, request);
		validate(restauranteAtual, "restaurante");//Aula 9.19
		
		Restaurante restaurante = toDomainObject(restauranteInput)
		
		return atualizar(restauranteId, restauranteAtual);
	}

	private void validate(Restaurante restaurante, String objectName) {
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objectName);
		validator.validate(restaurante, bindingResult);
		
		if(bindingResult.hasErrors()) {
			throw new ValidacaoException(bindingResult); //Aula 9.19 ESR
		}
	}

	//Aula ESR 4.34
	private void merge(Map<String, Object> camposOrigem, Restaurante restauranteDestino, HttpServletRequest request) {
		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
			
			Restaurante restauranteOrigem = objectMapper.convertValue(camposOrigem, Restaurante.class);
			
			camposOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
				field.setAccessible(true);
				
				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
				
				ReflectionUtils.setField(field, restauranteDestino, novoValor);
			});
		}catch (IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
		}
	}
	*/
}
