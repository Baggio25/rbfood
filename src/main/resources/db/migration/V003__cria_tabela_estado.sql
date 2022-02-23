create table tb_estado (
	id bigint not null auto_increment,
	nome varchar(80) not null,
	
	constraint estado_id primary key (id)
) engine=InnoDB default charset=utf8;

alter table tb_cidade add column estado_id bigint not null;
alter table tb_cidade add constraint estado_cidade_fk foreign key (estado_id) references tb_estado(id);
alter table tb_cidade drop column nome_estado;