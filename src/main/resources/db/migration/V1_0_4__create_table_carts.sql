create table if not exists carts(
    id bigint auto_increment,
    owner_id bigint not null,
    product_id bigint not null,
    primary key (id),
    foreign key (owner_id) references accounts(id),
    foreign key (product_id) references products(id)
);