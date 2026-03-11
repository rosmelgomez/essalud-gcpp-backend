INSERT INTO public.modulo(id, active, name_modulo)VALUES ('1', '1', 'CONSULTA EXTERNA') ON CONFLICT (id) DO NOTHING;
INSERT INTO public.modulo(id, active, name_modulo)VALUES ('2', '0', 'EMERGENCIA') ON CONFLICT (id) DO NOTHING;
SELECT setval(pg_get_serial_sequence('modulo','id'), (SELECT MAX(id) FROM modulo));
