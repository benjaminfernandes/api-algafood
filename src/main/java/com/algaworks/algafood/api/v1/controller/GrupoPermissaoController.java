package com.algaworks.algafood.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.Algalinks;
import com.algaworks.algafood.api.v1.assembler.PermissaoConverter;
import com.algaworks.algafood.api.v1.model.PermissaoModel;
import com.algaworks.algafood.api.v1.openapi.controller.GrupoPermissaoControllerOpenApi;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.service.CadastroGrupoService;

@RestController
@RequestMapping(value = "/v1/grupos/{grupoId}/permissoes")
public class GrupoPermissaoController implements GrupoPermissaoControllerOpenApi{

	@Autowired
	private CadastroGrupoService grupoService;
	@Autowired
	private PermissaoConverter permissaoConverter;
	@Autowired
	private Algalinks algaLinks;
	
	@GetMapping
	public CollectionModel<PermissaoModel> listar(@PathVariable Long grupoId){
		Grupo grupo = this.grupoService.buscarOuFalhar(grupoId);
		
		CollectionModel<PermissaoModel> permissoesModel 
			= permissaoConverter.toCollectionModel(grupo.getPermissoes())
            .removeLinks()
            .add(algaLinks.linkToGrupoPermissoes(grupoId))
            .add(algaLinks.linkToGrupoPermissaoAssociacao(grupoId, "associar"));
    
    permissoesModel.getContent().forEach(permissaoModel -> {
        permissaoModel.add(algaLinks.linkToGrupoPermissaoDesassociacao(
                grupoId, permissaoModel.getId(), "desassociar"));
    });
    
    return permissoesModel;
	}
	
	@PutMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> associar(@PathVariable Long grupoId ,@PathVariable Long permissaoId) {
		this.grupoService.associarPermissao(grupoId, permissaoId);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> desassociar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		this.grupoService.desassociarPermissao(grupoId, permissaoId);
		return ResponseEntity.noContent().build();
	}
}
