create table cidade(
	id bigint not null auto_increment,
    nome_cidade varchar(80) not null,
	nome_estado varchar(80) not null,
    PRIMARY KEY (id)
)engine=InnoDB default charset=utf8