--INSERT CURRENCIES--
insert into currency (iso_code, name) values
	('USD', 'Dólar estadounidenses'),
	('EUR', 'Euros'),
	('GBP', 'Libra esterlinas'),
	('JPY', 'Yen');


--INSERT RATIOS--
insert into ratio (iso_code, day, ratio_exchange) values
	('USD', null, 1),
	('EUR', '2018-03-18', 1.02),
	('GBP', '2018-03-18', 0.87),	
	('JPY', '2018-03-18', 123.0),		 
	('EUR', '2018-03-19', 1.05),	
	('GBP', '2018-03-19', 0.879),
	('JPY', '2018-03-19', 124.23),
	('EUR', '2018-03-20', 1.102),
	('GBP', '2018-03-20', 0.889),
	('JPY', '2018-03-20', 126.589),
	('EUR', '2018-03-21', 1.203),
	('GBP', '2018-03-21', 0.84),
	('JPY', '2018-03-21', 122.632);