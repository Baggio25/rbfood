create table tb_cozinha (
	id bigint not null auto_increment,
    nome varchar(60) not null,
    
    constraint cozinha_id primary key (id)
)engine=InnoDB default charset=utf8;