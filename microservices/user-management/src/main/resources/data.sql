--INSERT USER--
insert into "USER" (id, username, email, password) values
	(1,		'superadmin',	'superadmin@tfg.uoc',	''),
	(2,		'admin1',		'admin1@tfg.uoc',		''),
	(3,		'admin2',		'admin2@tfg.uoc',		''),
	(4,		'admin3',		'admin3@tfg.uoc',		''),
	(5,		'admin4',		'admin4@tfg.uoc',		''),
	(6,		'user1',		'user1@tfg.uoc',		''),
	(7,		'user2',		'user2@tfg.uoc',		''),
	(8,		'user3',		'user3@tfg.uoc',		''),
	(9,		'user4',		'user4@tfg.uoc',		''),
	(10,	'user5',		'user5@tfg.uoc',		''),
	(11,	'user6',		'user6@tfg.uoc',		'');
	
--INSERT ADMIN--
insert into admin (id, deleted) values
	(2, true),
	(3, false),
	(4, false),
	(5, false);

--INSERT REGISTERED_USER--
insert into registered_user (id, status, tries, default_currency) values
	(6,		1, 3, 'EUR'),
	(7, 	2, 0, 'EUR'),
	(8, 	3, 3, 'EUR'),
	(9, 	1, 2, 'JPY'),
	(10, 	1, 1, 'USD'),
	(11, 	1, 3, 'GBP');
