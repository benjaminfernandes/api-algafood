package com.algaworks.algafood.domain.service;

import java.io.InputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.FotoNaoEncontradoException;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import com.algaworks.algafood.domain.service.FotoStorageService.NovaFoto;

@Service
public class CatalogoFotoProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private FotoStorageService fotoStorageService;
	
	@Transactional
	public FotoProduto salvar(FotoProduto foto, InputStream dadosArquivo) {
		Long restauranteId = foto.getRestauranteId();
		Long produtoId = foto.getProduto().getId();
		String novoNomeArquivo = fotoStorageService.gerarNomeArquivo(foto.getNomeArquivo()); 
		String nomeArquivoExistente = null;
		
		Optional<FotoProduto> fotoExistente = this.produtoRepository
				.findFotoById(restauranteId, produtoId);
		
		if(fotoExistente.isPresent()) {
			nomeArquivoExistente = fotoExistente.get().getNomeArquivo();
			this.produtoRepository.delete(fotoExistente.get());
		}
		
		foto.setNomeArquivo(novoNomeArquivo);
		foto = produtoRepository.save(foto);
		produtoRepository.flush();
		
		NovaFoto novaFoto = NovaFoto.builder()
				.nomeArquivo(foto.getNomeArquivo())
				.contentType(foto.getContentType())
				.tamanho(foto.getTamanho())
				.inputStream(dadosArquivo)
				.build();
		
		this.fotoStorageService.substituir(nomeArquivoExistente, novaFoto);
		return foto;
	}
	
	@Transactional
	public void excluir(FotoProduto fotoProduto) {
		this.fotoStorageService.remover(fotoProduto.getNomeArquivo());
		this.produtoRepository.delete(fotoProduto);
		this.produtoRepository.flush();
	}
	
	public FotoProduto buscarOuFalhar(Long restauranteId, Long produtoId) {
		return this.produtoRepository.findFotoById(restauranteId, produtoId)
				.orElseThrow(() -> new FotoNaoEncontradoException(restauranteId, produtoId));
	}
}
