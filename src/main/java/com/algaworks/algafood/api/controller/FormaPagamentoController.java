package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.FormaPagamentoConverter;
import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.api.model.input.FormaPagamentoInput;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import com.algaworks.algafood.domain.service.CadastroFormaPagamentoService;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {

	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;
	@Autowired
	private CadastroFormaPagamentoService cadastroFormaPagamentoService;
	@Autowired
	private FormaPagamentoConverter formaPagamentoConverter;
	
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FormaPagamentoModel adicionar(@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
		FormaPagamento forma = this.formaPagamentoConverter.toDomain(formaPagamentoInput);
		
		return this.formaPagamentoConverter.toModel(this.cadastroFormaPagamentoService.salvar(forma));
	}
	
	@GetMapping
	public ResponseEntity<List<FormaPagamentoModel>> listar(){
		
		List<FormaPagamentoModel> formasPagamentoModel = this.formaPagamentoConverter
				.toCollectionModel(this.formaPagamentoRepository.findAll());
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS)) //adiciona chace de 10 seg.
				.body(formasPagamentoModel);
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<FormaPagamentoModel> buscar(@PathVariable Long codigo) {
		FormaPagamento forma = this.cadastroFormaPagamentoService.buscarOuFalhar(codigo);
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
				.body(this.formaPagamentoConverter.toModel(forma));
	}
	
	@PutMapping("/{formaPagamentoId}")
	public FormaPagamentoModel atualizar(@PathVariable Long formaPagamentoId,@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
		
		FormaPagamento forma = this.cadastroFormaPagamentoService.buscarOuFalhar(formaPagamentoId);
		
		this.formaPagamentoConverter.copyToDomainObject(formaPagamentoInput, forma);
		
		return this.formaPagamentoConverter.toModel(this.cadastroFormaPagamentoService.salvar(forma));
	}
	
	@DeleteMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long formaPagamentoId) {
		this.cadastroFormaPagamentoService.excluir(formaPagamentoId);
	}
}
