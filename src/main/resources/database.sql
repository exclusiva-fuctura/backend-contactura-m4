create schema contactura;
create table contactura.usuarios(
 usua_id varchar(150);
 usua_email varchar(100);
 usua_nome varchar(60);
 usua_senha varchar(200);
 usua_telefone int;
);
alter table contactura.usuarios
 add constraint usuarios_pk primary key(usua_id);
 
insert into usuarios(usua_id, usua_email, usua_senha, usua_nome, usua_telefone) values 
('e7c555be-0472-4b86-8507-cd30f648b567', 'aluno@fuctura.com.br', 'b7e94be513e96e8c45cd23d162275e5a12ebde9100a425c4ebcdd7fa4dcd897c', 'Aluno Fuctura', 8199998888);
