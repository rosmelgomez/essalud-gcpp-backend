INSERT INTO public.rol(id, description, date_creation, date_update, flag, name) VALUES ('1', '','2026-03-05 0:0:0.0','2026-03-05 0:0:0.0','1', 'ADMIN') ON CONFLICT (id) DO NOTHING;
INSERT INTO public.rol(id, description, date_creation, date_update, flag, name) VALUES ('2', '','2026-03-05 0:0:0.0','2026-03-05 0:0:0.0','1', 'ADMIN_RED') ON CONFLICT (id) DO NOTHING;
INSERT INTO public.rol(id, description, date_creation, date_update, flag, name) VALUES ('3', '','2026-03-05 0:0:0.0','2026-03-05 0:0:0.0','1', 'USER') ON CONFLICT (id) DO NOTHING;
INSERT INTO public.rol(id, description, date_creation, date_update, flag, name) VALUES ('4', '','2026-03-05 0:0:0.0','2026-03-05 0:0:0.0','1', 'CONSULTA') ON CONFLICT (id) DO NOTHING;
SELECT setval(pg_get_serial_sequence('rol','id'), (SELECT MAX(id) FROM rol));
