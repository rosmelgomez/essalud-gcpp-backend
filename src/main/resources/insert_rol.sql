INSERT INTO public.rol(id, flag, name) VALUES ('1', '1', 'ADMIN') ON CONFLICT (id) DO NOTHING;
INSERT INTO public.rol(id, flag, name) VALUES ('2', '1', 'ADMIN_RED') ON CONFLICT (id) DO NOTHING;
INSERT INTO public.rol(id, flag, name) VALUES ('3', '1', 'USER') ON CONFLICT (id) DO NOTHING;
INSERT INTO public.rol(id, flag, name) VALUES ('4', '1', 'CONSULTA') ON CONFLICT (id) DO NOTHING;
SELECT setval(pg_get_serial_sequence('rol','id'), (SELECT MAX(id) FROM rol));
