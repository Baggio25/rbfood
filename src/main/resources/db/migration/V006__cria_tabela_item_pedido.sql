create table tb_item_pedido (
	id bigint not null auto_increment,
	quantidade integer not null,
	preco_unitario decimal(10,2) not null,
	preco_total decimal(10,2) not null,
	observacao varchar(400),
	
	produto_id bigint not null,
	pedido_id bigint not null,
	
	constraint item_pedido_id primary key (id)
) engine=InnoDB default charset=utf8;

alter table tb_item_pedido add constraint fk_produto_item_pedido
foreign key (produto_id) references tb_produto (id);

alter table tb_item_pedido add constraint fk_pedido_item_pedido
foreign key (pedido_id) references tb_pedido (id);