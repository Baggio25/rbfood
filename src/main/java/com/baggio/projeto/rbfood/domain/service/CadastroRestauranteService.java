package com.baggio.projeto.rbfood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baggio.projeto.rbfood.domain.exception.EntidadeEmUsoException;
import com.baggio.projeto.rbfood.domain.exception.RestauranteNaoEncontradoException;
import com.baggio.projeto.rbfood.domain.model.Cozinha;
import com.baggio.projeto.rbfood.domain.model.Restaurante;
import com.baggio.projeto.rbfood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {

	private static final String MSG_RESTAURANTE_EM_USO = "Restaurante de código %d não pode ser removido, pois está em uso";
	private static final String MSG_RESTAURANTE_NAO_ENCONTRADO = "Não existe cadastro de restaurante com código %d";

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroCozinhaService cadastroCozinhaService;
	
	@Transactional(readOnly = true)
	public List<Restaurante> listar() {
		return restauranteRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Restaurante buscar(Long id) {
		return restauranteRepository.findById(id).orElseThrow(() -> 
					new RestauranteNaoEncontradoException(String.format(MSG_RESTAURANTE_NAO_ENCONTRADO, id)));		
	}

	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		Cozinha cozinha = cadastroCozinhaService.buscar(cozinhaId);
		
		restaurante.setCozinha(cozinha);
		return restauranteRepository.save(restaurante);
	}

	public void remover(Long id) {
		try {
			restauranteRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new RestauranteNaoEncontradoException(String.format(MSG_RESTAURANTE_NAO_ENCONTRADO, id));
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_RESTAURANTE_EM_USO, id));
		}

	}

}
