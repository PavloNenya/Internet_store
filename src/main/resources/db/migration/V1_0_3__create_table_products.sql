create table if not exists products(
    id bigint auto_increment,
    description varchar(255),
    is_active bit not null,
    price double not null,
    title varchar(255) not null,
    seller_id bigint not null,
    primary key (id),
    foreign key (seller_id) references accounts(id)
);