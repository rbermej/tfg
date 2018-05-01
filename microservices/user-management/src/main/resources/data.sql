--INSERT USER--
insert into "USER" (id, rol, username, email, password) values
	(1,		0,	'superadmin',	'superadmin@tfg.uoc',	''),
	(2,		1,	'admin1',		'admin1@tfg.uoc',		''),
	(3,		1,	'admin2',		'admin2@tfg.uoc',		''),
	(4,		1,	'admin3',		'admin3@tfg.uoc',		''),
	(5,		1,	'admin4',		'admin4@tfg.uoc',		''),
	(6,		2,	'user1',		'user1@tfg.uoc',		''),
	(7,		2,	'user2',		'user2@tfg.uoc',		''),
	(8,		2,	'user3',		'user3@tfg.uoc',		''),
	(9,		2,	'user4',		'user4@tfg.uoc',		''),
	(10,	2,	'user5',		'user5@tfg.uoc',		''),
	(11,	2,	'user6',		'user6@tfg.uoc',		'');
	
--INSERT ADMIN--
insert into admin (id, deleted) values
	(2, true),
	(3, false),
	(4, false),
	(5, false);

--INSERT REGISTERED_USER--
insert into registered_user (id, status, tries, default_currency) values
	(6,		0, 3, 'EUR'),
	(7, 	1, 0, 'EUR'),
	(8, 	2, 3, 'EUR'),
	(9, 	0, 2, 'JPY'),
	(10, 	0, 1, 'USD'),
	(11, 	0, 3, 'GBP');
