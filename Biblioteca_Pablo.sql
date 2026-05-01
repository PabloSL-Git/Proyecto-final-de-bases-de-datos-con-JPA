create database if not exists Biblioteca_Pablo;
use Biblioteca_Pablo;

-- TABLA BIBLIOTECA

create table Biblioteca
(
 id_biblioteca int,
 nombre varchar(100),
 direccion varchar(150),
 constraint pk_biblioteca primary key (id_biblioteca)
);

-- TABLA AUTOR

drop table if exists Autor;
create table Autor
(
 id_autor int,
 nombre varchar(100) not null,
 apellido1 varchar(100) not null,
 apellido2 varchar(100),
 nacionalidad varchar(100),
 constraint pk_autor primary key (id_autor)
);

-- TABLA LIBRO

create table Libro
(
 id_libro int,
 titulo varchar(150) not null,
 anio_publicacion int,
 estado varchar(50),
 id_autor int,
 id_biblioteca int,
 constraint pk_libro primary key (id_libro),
 constraint fk_libro_autor foreign key (id_autor) references Autor(id_autor)
    on delete no action on update cascade,
 constraint fk_libro_biblioteca foreign key (id_biblioteca) references Biblioteca(id_biblioteca)
    on delete no action on update cascade
);

-- TABLA LECTOR

create table Lector
(
 id_lector int,
 nombre varchar(100) not null,
 apellido1 varchar(100) not null,
 apellido2 varchar(100),
 email varchar(100),
 telefono varchar(20),
 id_biblioteca int,
 constraint pk_lector primary key (id_lector),
 constraint fk_lector_biblioteca foreign key (id_biblioteca) references Biblioteca(id_biblioteca)
    on delete no action on update cascade
);

-- TABLA CREDENCIAL 

create table Credencial
(
 id_credencial int,
 numero_tarjeta varchar(50) not null,
 fecha_emision date,
 id_lector int,
 constraint pk_credencial primary key (id_credencial),
 constraint uq_credencial_lector unique (id_lector),
 constraint uq_numero_tarjeta unique (numero_tarjeta),
 constraint fk_credencial_lector foreign key (id_lector) references Lector(id_lector)
    on delete no action on update cascade
);

-- TABLA PRESTAMO

create table Prestamo
(
 id_prestamo int,
 fecha_inicio date not null,
 fecha_fin date,
 id_lector int,
 id_libro int,
 constraint pk_prestamo primary key (id_prestamo),
 constraint fk_prestamo_lector foreign key (id_lector) references Lector(id_lector)
    on delete no action on update cascade,
 constraint fk_prestamo_libro foreign key (id_libro) references Libro(id_libro)
    on delete no action on update cascade
);


-- INSERT 

insert into Biblioteca values
(1, 'Biblioteca Central', 'Calle Mayor 1'),
(2, 'Biblioteca Norte', 'Avenida Norte 45'),
(3, 'Biblioteca Sur', 'Calle Sur 23'),
(4, 'Biblioteca Este', 'Avenida Este 12'),
(5, 'Biblioteca Oeste', 'Calle Oeste 9');

insert into Autor values
(1, 'Gabriel', 'Garcia', null, 'Colombia'),
(2, 'J.R.R.', 'Tolkien', null, 'Reino Unido'),
(3, 'George', 'Orwell', null, 'Reino Unido'),
(4, 'Isabel', 'Allende', null, 'Chile'),
(5, 'Julio', 'Cortazar', null, 'Argentina'),
(6, 'Miguel', 'de Cervantes', 'Saavedra', 'España');


insert into Libro values
(1, 'Cien años de soledad', 1967, 'disponible', 1, 1),
(2, 'El Hobbit', 1937, 'prestado', 2, 2),
(3, '1984', 1949, 'disponible', 3, 3),
(4, 'La casa de los espiritus', 1982, 'disponible', 4, 4),
(5, 'Rayuela', 1963, 'prestado', 5, 5),
(6, 'Don Quijote de la Mancha', 1605, 'disponible', 6, 1);

insert into Lector values
(1, 'Juan', 'Perez', 'Gomez', 'juan@email.com', '123456789', 1),
(2, 'Maria', 'Lopez', 'Fernandez', 'maria@email.com', '987654321', 2),
(3, 'Carlos', 'Ruiz', 'Martinez', 'carlos@email.com', '456123789', 3),
(4, 'Ana', 'Torres', 'Sanchez', 'ana@email.com', '321654987', 4),
(5, 'Luis', 'Gomez', 'Diaz', 'luis@email.com', '654987321', 5);

insert into Credencial values
(1, 'TARJ001', '2024-01-01', 1),
(2, 'TARJ002', '2024-01-02', 2),
(3, 'TARJ003', '2024-01-03', 3),
(4, 'TARJ004', '2024-01-04', 4),
(5, 'TARJ005', '2024-01-05', 5);

insert into Prestamo values
(1, '2025-01-10', '2025-01-20', 1, 2),
(2, '2025-01-11', '2025-01-21', 2, 1),
(3, '2025-01-12', '2025-01-22', 3, 5),
(4, '2025-01-13', '2025-01-23', 4, 3),
(5, '2025-01-14', '2025-01-24', 5, 4);