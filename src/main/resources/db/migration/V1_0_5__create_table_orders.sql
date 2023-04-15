create table if not exists orders (
    id bigint auto_increment,
    date datetime(6) not null,
    buyer_id bigint not null,
    seller_id bigint not null,
    product_id bigint not null,
    sum double not null,
    primary key (id),
    foreign key (buyer_id) references accounts(id),
    foreign key (seller_id) references accounts(id),
    foreign key (product_id) references products(id)
);