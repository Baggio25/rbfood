package com.baggio.projeto.rbfood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baggio.projeto.rbfood.domain.exception.CozinhaNaoEncontradaException;
import com.baggio.projeto.rbfood.domain.exception.EntidadeEmUsoException;
import com.baggio.projeto.rbfood.domain.model.Cozinha;
import com.baggio.projeto.rbfood.domain.repository.CozinhaRepository;

@Service
public class CadastroCozinhaService {

	private static final String MSG_COZINHA_EM_USO = "Cozinha de código %d não pode ser removida, pois está em uso";
	private static final String MSG_COZINHA_NAO_ENCONTRADA = "Não existe cadastro de cozinha com código %d";

	@Autowired
	private CozinhaRepository cozinhaRepository;

	@Transactional(readOnly = true)
	public List<Cozinha> listar() {
		return cozinhaRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Cozinha buscar(Long id) {
		return cozinhaRepository.findById(id)
				.orElseThrow(() -> new CozinhaNaoEncontradaException(String.format(MSG_COZINHA_NAO_ENCONTRADA, id)));
	}

	@Transactional
	public Cozinha salvar(Cozinha cozinha) {
		return cozinhaRepository.save(cozinha);
	}

	@Transactional
	public void remover(Long id) {
		try {
			cozinhaRepository.deleteById(id);
			cozinhaRepository.flush(); //descarrega as mudanças pendentes no banco
		} catch (EmptyResultDataAccessException e) {
			throw new CozinhaNaoEncontradaException(String.format(MSG_COZINHA_NAO_ENCONTRADA, id));
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_COZINHA_EM_USO, id));
		}

	}

}
