package com.baggio.projeto.rbfood.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.baggio.projeto.rbfood.domain.model.enums.StatusPedido;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_pedido")
public class Pedido {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private BigDecimal subtotal;

	@Column(name = "taxa_frete" ,nullable = false)
	private BigDecimal taxaFrete;

	@Column(name = "valor_total" ,nullable = false)
	private BigDecimal valorTotal;	

	@CreationTimestamp
	@Column(name = "data_criacao", nullable = false, columnDefinition = "datetime")
	private LocalDateTime dataCriacao;

	@Column(name = "data_confirmacao", columnDefinition = "datetime")
	private LocalDateTime dataConfirmacao;

	@Column(name = "data_cancelamento", columnDefinition = "datetime")
	private LocalDateTime dataCancelamento;

	@Column(name = "data_entrega", columnDefinition = "datetime")
	private LocalDateTime dataEntrega;
	
	@JsonIgnore
	@Embedded
	@Column(name = "endereco_entrega")
	private Endereco enderecoEntrega;

	@Enumerated
	@Column(nullable = false)
	private StatusPedido status;

	@ManyToOne
	@JoinColumn(name = "forma_pagamento_id", nullable = false)
	private FormaPagamento formaPagamento;

	@ManyToOne
	@JoinColumn(name = "cliente_id", nullable = false)
	private Usuario cliente;

	@ManyToOne
	@JoinColumn(name = "restaurante_id", nullable = false)
	private Restaurante restaurante;

	@OneToMany(mappedBy = "pedido")
	private List<ItemPedido> itensPedido = new ArrayList<ItemPedido>();

}
