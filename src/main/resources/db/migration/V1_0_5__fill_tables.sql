insert into accounts(email, name, password) values
    ('pavel.nenya228@gmail.com', 'Pavlo Nenia', '123'),
    ('e.gor195613@gmail.com', 'Ehor Shevchenko', '123'),
    ('pavel.nenya@gmail.com', 'Name Surname', '123');

insert into products(description, is_active, price, title, seller_id) values
    ('description1', true, 123.9, 'Title1', 2),
    ('description2', true, 55.1, 'Title2', 3),
    ('description3', true, 12345, 'Laptop', 1);

insert into carts(owner_id, product_id) values
    (1, 1),
    (3, 3),
    (3, 1);

insert into orders(date, sum, buyer_id, product_id, seller_id) values
    (current_time, 123.9, 3, 1, 2),
    (current_time, 55.1, 1, 2, 3),
    (current_time, 12345, 3, 3, 1);