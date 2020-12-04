package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.PedidoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;

@Service
public class CadastroPedidoService implements CadastroService<Pedido> {

	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private CadastroRestauranteService restauranteService;
	@Autowired
	private CadastroFormaPagamentoService formaPagamentoService;
	@Autowired
	private CadastroCidadeService cidadeService;
	@Autowired
	private CadastroUsuarioService usuarioService;
	@Autowired
	private CadastroProdutoService produtoService;
	
	@Override
	public Pedido salvar(Pedido pedido) {
		validarPedido(pedido);
		validarItens(pedido);
		pedido.setTaxaFrete(pedido.getRestaurante().getTaxaFrete());
		pedido.calculaValorTotal();
		
		return pedidoRepository.save(pedido);
	}

	@Override
	public void excluir(Long id) {
		
	}

	@Override
	public Pedido buscarOuFalhar(Long id) {
		
		return this.pedidoRepository.findById(id).orElseThrow(() -> new PedidoNaoEncontradoException(id));
	}
	
	private void validarPedido(Pedido pedido) {
		Restaurante restaurante = this.restauranteService.buscarOuFalhar(pedido.getRestaurante().getId());
		FormaPagamento formaPagamento = this.formaPagamentoService.buscarOuFalhar(pedido.getFormaPagamento().getId());
		Cidade cidade = this.cidadeService.buscarOuFalhar(pedido.getEndereco().getCidade().getId());
		Usuario cliente = this.usuarioService.buscarOuFalhar(pedido.getCliente().getId());
		
		pedido.setRestaurante(restaurante);
		pedido.setCliente(cliente);
		pedido.getEndereco().setCidade(cidade);
		pedido.setFormaPagamento(formaPagamento);
		
		if(restaurante.naoAceitaFormaPagamento(formaPagamento)) {
			throw new NegocioException(String.format("Este restaurante não aceita a forma de pagamento %s", formaPagamento.getDescricao()));
		}
	}
	private void validarItens(Pedido pedido) {
		pedido.getItens().forEach(item -> {
			Produto produto = this.produtoService.buscarOuFalhar(item.getProduto().getId());
			
			if(produto.getAtivo().equals(Boolean.FALSE)) {
				throw new NegocioException(String.format("O produto %s está inativo", produto.getNome()));
			}
			
			item.setPedido(pedido);
			item.setProduto(produto);
			item.setPrecoUnitario(produto.getPreco());
		});
		
	}

}
