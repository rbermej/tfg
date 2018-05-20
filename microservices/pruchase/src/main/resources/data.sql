--INSERT AD--
insert into ad (id, offered_amount, offered_currency, demanded_amount, demanded_currency, seller, location, status, date) values
	(1,		100,	'EUR',	80,		'USD',	'user6',	'Madrid',		1, '2018-04-21'),	
	(2,		200,	'EUR',	190,	'JPY',	'user6',	'Barcelona',	0, '2018-04-22'),	
	(3,		300,	'EUR',	250,	'USD',	'user1',	'Madrid',		0, '2018-04-23'),	
	(4,		300,	'EUR',	250,	'USD',	'user1',	'Madrid',		1, '2018-04-23');	
	
--INSERT PURCHASE_REQUEST--
insert into purchase_request (id, applicant, ad_id, date) values
	(1, 'user2',	1,	'2018-04-21'),
	(2, 'user3',	1,	'2018-04-22'),
	(3, 'user4',	1,	'2018-04-23'),
	(4, 'user5',	1,	'2018-04-24'),
	(5, 'user1',	1,	'2018-04-25'),
	(6, 'user6',	3,	'2018-04-25'),
	(7, 'user6',	4,	'2018-04-30'),
	(8, 'user2',	2,	'2018-04-21'),
	(9, 'user3',	2,	'2018-04-22'),
	(10, 'user4',	2,	'2018-04-23'),
	(11, 'user5',	2,	'2018-04-24');
	
--INSERT SALE--
insert into sale (id, buyer, ad_id, date) values
	(2, 'user2',	1,	'2018-04-26');