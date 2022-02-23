/* CREATE TABLE */
create table tb_forma_pagamento (
	id bigint not null auto_increment,
	descricao varchar(60) not null,
	primary key (id)
) engine=InnoDB default charset=utf8;

create table tb_grupo (
	id bigint not null auto_increment,
	nome varchar(60) not null,
	
	primary key (id)
) engine=InnoDB default charset=utf8;

create table tb_grupo_permissao (
	grupo_id bigint not null,
	permissao_id bigint not null,
	
	primary key (grupo_id, permissao_id)
) engine=InnoDB default charset=utf8;

create table tb_permissao (
	id bigint not null auto_increment,
	descricao varchar(60) not null,
	nome varchar(100) not null,
	
	primary key (id)
) engine=InnoDB default charset=utf8;

create table tb_produto (
	id bigint not null auto_increment,
	restaurante_id bigint not null,
	nome varchar(80) not null,
	descricao text not null,
	preco decimal(10,2) not null,
	ativo tinyint(1) not null,
	
	primary key (id)
) engine=InnoDB default charset=utf8;

create table tb_restaurante (
	id bigint not null auto_increment,
	cozinha_id bigint not null,
	nome varchar(80) not null,
	taxa_frete decimal(10,2) not null,
	data_atualizacao datetime not null,
	data_cadastro datetime not null,
	
	endereco_cidade_id bigint,
	endereco_cep varchar(9),
	endereco_logradouro varchar(100),
	endereco_numero varchar(20),
	endereco_complemento varchar(60),
	endereco_bairro varchar(60),
	
	primary key (id)
) engine=InnoDB default charset=utf8;

create table tb_restaurante_forma_pagamento (
	restaurante_id bigint not null,
	forma_pagamento_id bigint not null,
	
	primary key (restaurante_id, forma_pagamento_id)
) engine=InnoDB default charset=utf8;

create table tb_usuario (
	id bigint not null auto_increment,
	nome varchar(80) not null,
	email varchar(255) not null,
	senha varchar(255) not null,
	data_cadastro datetime not null,
	
	primary key (id)
) engine=InnoDB default charset=utf8;

create table tb_usuario_grupo (
	usuario_id bigint not null,
	grupo_id bigint not null,
	
	primary key (usuario_id, grupo_id)
) engine=InnoDB default charset=utf8;



/* ALTER TABLE */
alter table tb_grupo_permissao add constraint fk_grupo_permissao_permissao
foreign key (permissao_id) references tb_permissao (id);

alter table tb_grupo_permissao add constraint fk_grupo_permissao_grupo
foreign key (grupo_id) references tb_grupo (id);

alter table tb_produto add constraint fk_produto_restaurante
foreign key (restaurante_id) references tb_restaurante (id);

alter table tb_restaurante add constraint fk_restaurante_cozinha
foreign key (cozinha_id) references tb_cozinha (id);

alter table tb_restaurante add constraint fk_restaurante_cidade
foreign key (endereco_cidade_id) references tb_cidade (id);

alter table tb_restaurante_forma_pagamento add constraint fk_rest_forma_pagto_forma_pagto
foreign key (forma_pagamento_id) references tb_forma_pagamento (id);

alter table tb_restaurante_forma_pagamento add constraint fk_rest_forma_pagto_restaurante
foreign key (restaurante_id) references tb_restaurante (id);

alter table tb_usuario_grupo add constraint fk_usuario_grupo_grupo
foreign key (grupo_id) references tb_grupo (id);

alter table tb_usuario_grupo add constraint fk_usuario_grupo_usuario
foreign key (usuario_id) references tb_usuario (id);