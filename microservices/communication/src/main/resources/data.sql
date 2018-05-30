--INSERT CONVERSATIONS--
insert into conversation (id, user1, user2) values
	(1, 'user6',	'user4'),
	(2, 'user5',	'user6'),
	(3,	'user6',	'user3');
	
--INSERT TEXTS--
insert into text (id, content) values
	(1, 'Hi'),
	(2, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.'),
	(3, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum'),
	(4, 'Lorem ipsum dolor sit amet'),
	(5, 'Lorem ipsum dolor sit amet, consectetur'),
	(6, 'Lorem ipsum dolor sit amet'),
	(7, 'Excellent!'),
	(8, 'Not bad');
	
--INSERT MESSAGES--
insert into message (id, author, date, conversation_id, text_id) values
	(1,	'user6',	'2018-04-22',	1,	1),
	(2,	'user4',	'2018-04-23',	1,	2),
	(3,	'user6',	'2018-04-24',	1,	3),
	(4,	'user5',	'2018-04-21',	2,	4),
	(5,	'user6',	'2018-04-22',	2,	5),
	(6,	'user6',	'2018-04-28',	3,	6);

insert into valuation(id, date, evaluated, evaluator, points, rol, text_id) values
	(1,	'2018-05-24',	'user1',	'user6',	5,	0,	7),
	(2,	'2018-05-25',	'user1',	'user5',	4,	0,	8),
	(3,	'2018-05-29',	'user1',	'user6',	4,	1,	null),
	(4,	'2018-04-30',	'user1',	'user5',	2,	1,	null),
	(5,	'2018-04-15',	'user1',	'user4',	0,	1,	null);