drop table if exists users_security;
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
insert into USERS_SECURITY (first_name,email, age, login, password)
values  ('pasha', 'blin', 30, 'blin1', '123456' ),
        ('pasha', 'blin', 30, 'blin2', '123456' ),
        ('pasha', 'blin', 30, 'blin3', '$2a$10$Hgy6R.hCX2B6vV66BTnf9OCRxjz8EdmhOnG7JGvxJAhs7mDO8WpZu' ),
        ('pasha', 'blin', 30, 'blin4', '123456' );


DROP TABLE IF EXISTS roles;
create table roles
(
    id serial primary key ,
    name varchar(20) unique not null
);

insert into roles(name)
values ('ADMIN'),
       ('USER');

drop table if exists users_roles;

create table users_roles
(
    id_user BIGINT NOT NULL,
    id_role BIGINT NOT NULL,
    CONSTRAINT users_roles_pkey
        PRIMARY KEY (id_user, id_role),
    CONSTRAINT users_roles_users_fkey
        FOREIGN KEY (id_user)
            REFERENCES users_security (id)
            ON DELETE CASCADE,
    CONSTRAINT users_roles_roles_fkey
        FOREIGN KEY (id_role)
            REFERENCES roles (id)
            ON DELETE CASCADE
);
INSERT INTO users_roles(id_user, id_role)
VALUES (1, 1),
       (2, 2),
       (3, 1),
       (4, 2);
