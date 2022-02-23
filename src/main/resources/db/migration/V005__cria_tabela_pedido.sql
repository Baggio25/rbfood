create table tb_pedido (
	id bigint not null auto_increment,
	subtotal decimal(10,2) not null,
	taxa_frete decimal(10,2) not null,
	valor_total decimal(10,2) not null,
	data_criacao datetime not null,
	data_confirmacao datetime,
	data_cancelamento datetime,
	data_entrega datetime,
	
	endereco_cidade_id bigint not null,
	endereco_cep varchar(9) not null,
	endereco_logradouro varchar(100) not null,
	endereco_numero varchar(20)not null,
	endereco_complemento varchar(60),
	endereco_bairro varchar(60) not null,
	
	forma_pagamento_id bigint not null,
	cliente_id bigint not null,
	restaurante_id bigint not null,
	
	constraint pedido_id primary key (id)
) engine=InnoDB default charset=utf8;

alter table tb_pedido add constraint fk_forma_pagamento
foreign key (forma_pagamento_id) references tb_forma_pagamento (id);

alter table tb_pedido add constraint fk_cliente
foreign key (cliente_id) references tb_usuario (id);

alter table tb_pedido add constraint fk_restaurante
foreign key (restaurante_id) references tb_restaurante (id);