create table tb_cidade (
	id bigint not null auto_increment,
	nome varchar(80) not null,
	nome_estado varchar(80) not null,
	
	constraint cidade_id primary key (id)
) engine=InnoDB default charset=utf8;