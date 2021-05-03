create table users_security
(
    id serial not null
        constraint users_security_pkey
             primary key,
    age integer not null,
    first_name varchar(255) not null,
    login varchar(255) unique not null,
    password varchar(255) not null,
    email varchar(255) not null
);

create table roles
(
    id bigserial not null
        constraint roles_pkey
            primary key,
    name varchar(255)
);

create table users_security_roles
(
    user_id integer not null
        constraint fk1duc1ckqjsw1kcqk3kgmkt8vc
            references users_security,
    roles_id bigint not null
        constraint fkiebittp85smk1koa9i9ck1euf
            references roles,
    constraint users_security_roles_pkey
        primary key (user_id, roles_id)
);

create table warehouse
(
    id integer not null
        constraint warehouse_pkey
            primary key,
    name varchar(255),
    quantity integer
);

create table products
(
    product_id serial not null primary key ,
    name varchar(30) unique not null ,
    cost double precision not null ,
    stock boolean not null default false
);










