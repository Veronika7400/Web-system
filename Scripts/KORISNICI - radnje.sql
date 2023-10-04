INSERT INTO KORISNICI (IME, PREZIME, MAIL, KORIME, LOZINKA) VALUES
	 ('Ana', 'Anic', 'aanic@gmail.com', 'aanic', '1213456'),
	 ('Ivo', 'Ivic', 'iivic@gmail.com', 'iivic', 'lozinka123'),
	 ('Pero', 'Horvat', 'phorvat@gmail.com', 'phorvat', 'admin'); 
	 
	
	SELECT * FROM KORISNICI WHERE KORIME = 'phorvat' AND LOZINKA = 'admin'; 
SELECT * FROM KORISNICI; 
	
SELECT * FROM KORISNICI WHERE IME LIKE '%A%' AND PREZIME LIKE '%A%'; 