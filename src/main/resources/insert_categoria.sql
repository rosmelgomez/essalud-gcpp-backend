INSERT INTO public.categoria(id, active, name_categoria, id_modulo) VALUES ('1', '1', 'MEDICAS', '1') ON CONFLICT (id) DO NOTHING;
INSERT INTO public.categoria(id, active, name_categoria, id_modulo) VALUES ('2', '1', 'NO MEDICAS', '1') ON CONFLICT (id) DO NOTHING;
INSERT INTO public.categoria(id, active, name_categoria, id_modulo) VALUES ('3', '1', 'ODONTOLOGICAS', '1') ON CONFLICT (id) DO NOTHING;
INSERT INTO public.categoria(id, active, name_categoria, id_modulo) VALUES ('4', '0', 'EMERGENCIA', '2') ON CONFLICT (id) DO NOTHING;
SELECT setval(pg_get_serial_sequence('categoria','id'), (SELECT MAX(id) FROM categoria));
