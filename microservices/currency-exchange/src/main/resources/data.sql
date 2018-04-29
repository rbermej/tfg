--INSERT CURRENCIES--
insert into currency (iso_code, name) values
	('USD', 'Dollar (United States)'),
	('EUR', 'Euro (Euro Member Countries)'),
	('GBP', 'Pound (United Kingdom)'),
	('JPY', 'Yen (Japan)');


--INSERT RATIOS--
insert into ratio (iso_code, day, ratio_exchange) values
	('USD', null, 1);
--	('EUR', '2018-03-18', 0.89),
--	('GBP', '2018-03-18', 0.77),	
--	('JPY', '2018-03-18', 123.0),		 