CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists client(
    idClient UUID primary key default gen_random_uuid(),
    name     varchar(50) not null,
    email    varchar(50) not null,
    phone    char(11) not null,
    cpf      char(11) not null
);

create table if not exists restaurant(
    idRestaurant UUID primary key default gen_random_uuid(),
    name         varchar(50) not null,
    email        varchar(50) not null,
    password     varchar(50) not null,
    branch       int not null
);

create table if not exists tables(
    idTable UUID primary key default gen_random_uuid(),
    capacity int not null,
    idRestaurant UUID not null,
    constraint fk_restaurant_tables foreign key (idRestaurant) references restaurant(idRestaurant) on delete cascade
);

create table if not exists reservation(
    idReservation   UUID primary key default gen_random_uuid(),
    dateReservation date not null,
    timeReservation time not null,
    status          varchar(10) not null,
    capacity        int  not null,
    idClient        UUID not null,
    idRestaurant    UUID not null,
    constraint fk_client_reservation foreign key (idClient) references client(idClient) on delete cascade,
    constraint fk_restaurant_reservation foreign key (idRestaurant) references restaurant(idRestaurant) on delete cascade
);

create table if not exists reservation_table(
    idReservationTable UUID primary key default gen_random_uuid(),
    idTable            UUID not null,
    idReservation      UUID not null,
    constraint fk_reservation foreign key (idReservation) references reservation(idReservation) on delete cascade,
    constraint fk_tables foreign key (idTable) references tables(idTable)
);