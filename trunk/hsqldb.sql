create table spider_resource(
	id int identity not null primary key , 
	pid int default 0 null, 
	url varchar(500) not null, 
	type int not null, --1:list,2:detail
	site varchar(50) null,
	siteid int default 0 not null,
	httpStatus int null, 
	ctime char(20) default 0 not null, 
	save int default 0 not null,
	crawl bit default 0 not null, 
	parser bit default 0 not null,
	childparser bit default 0 not null,
	childsave bit default 0 not null,
    reqCount int default 0 not null,
	unique(url)
);