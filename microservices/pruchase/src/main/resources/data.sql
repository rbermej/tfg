--INSERT AD--
insert into ad (id, offered_amount, offered_currency, demanded_amount, demanded_currency, seller, location, status, date) values
	(1,		100,	'EUR',	80,		'USD',	'user1',	'Madrid',		0, '2018-04-23'),	
	(2,		200,	'EUR',	190,	'USD',	'user1',	'Barcelona',	0, '2018-04-23'),	
	(3,		300,	'EUR',	250,	'USD',	'user1',	'Madrid',		0, '2018-04-23');	
	
--INSERT PURCHASE_REQUEST--
insert into purchase_request (id, applicant, ad_id, date) values
	(1, 'user2',	1,	'2018-04-23'),
	(2, 'user3',	1,	'2018-04-23'),
	(3, 'user4',	1,	'2018-04-23'),
	(4, 'user5',	1,	'2018-04-23'),
	(5, 'user6',	1,	'2018-04-23');
	
--INSERT SALE--
insert into sale (id, buyer, ad_id, date) values
	(2, 'user2',	1,	'2018-04-23');