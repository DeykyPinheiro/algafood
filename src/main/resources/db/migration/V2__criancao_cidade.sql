create table cidade
(
    id          bigint primary key auto_increment not null,
    nome_estado varchar(80),
    nome_cidade varchar(80)
) engine=InnoDB default charset=utf8;