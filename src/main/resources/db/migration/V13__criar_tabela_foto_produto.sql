alter table produto
	add nome_arquivo_foto  varchar (150) not null default "sem foto",
    add content_type_foto varchar(80) not null default "sem foto",
    add tamanho_foto int not null default 0;