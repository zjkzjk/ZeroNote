create table user_infro(
	id varchar(20) primary key ,
    username varchar(100) not null,
    passwd varchar(100) not null,
    birth date not null,
    pic varchar(100) not null,
    email varchar(100),
    mobile int
)
