CREATE TABLE restaurante_usuario_responsavel(
	restaurante_id bigint not null,
	usuario_id bigint not null,
	
	primary key (restaurante_id, usuario_id)
)engine=InnoDB default charset=utf8;

ALTER TABLE restaurante_usuario_responsavel ADD CONSTRAINT fk_restaurante_usuario_restaurante 
FOREIGN KEY (restaurante_id) REFERENCES restaurante (id);

ALTER TABLE restaurante_usuario_responsavel ADD CONSTRAINT fk_restaurante_usuario_usuario
FOREIGN KEY (usuario_id) REFERENCES usuario (id);