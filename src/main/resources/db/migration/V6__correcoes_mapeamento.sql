alter table pedido
    add column status_pedido varchar(90);

alter table item_pedido
    change quatidade quantidade int not null;