create table if not exists accounts(
    id bigint auto_increment,
    email varchar(255) not null,
    name varchar(255) not null,
    password varchar(255) not null,
    primary key (id)
);