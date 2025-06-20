ALTER TABLE _user
    ADD role VARCHAR(100) DEFAULT 'DEFAULT_USER' NOT NULL;

UPDATE _user
SET role = 'SUPER_ADMIN'
WHERE email = 'admin@a4s.dcx.ufpb.br'