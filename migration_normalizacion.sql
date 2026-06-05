-- =========================================================================
-- SCRIPT DE MIGRACIÓN: NORMALIZACIÓN DE GESTIÓN DOCUMENTAL (POSTGRESQL)
-- =========================================================================

-- 1. NORMALIZACIÓN DE EXTENSIONES PERMITIDAS (1NF)
CREATE TABLE tipo_documento_extension (
    id SERIAL PRIMARY KEY,
    id_tipo_documento INT NOT NULL,
    extension VARCHAR(50) NOT NULL,
    CONSTRAINT fk_tipo_doc_ext FOREIGN KEY (id_tipo_documento) REFERENCES tipo_documento(id) ON DELETE CASCADE
);

INSERT INTO tipo_documento_extension (id_tipo_documento, extension)
SELECT id, TRIM(unnest(string_to_array(extensiones_permitidas, ',')))
FROM tipo_documento;

ALTER TABLE tipo_documento DROP COLUMN extensiones_permitidas;


-- 2. NORMALIZACIÓN DE SOLICITUD Y RESOLUCIÓN (3NF)
CREATE TABLE solicitud_aprobacion_documento (
    id SERIAL PRIMARY KEY,
    prioridad VARCHAR(20) NOT NULL DEFAULT 'MEDIA',
    comentario_solicitante VARCHAR(1000),
    fecha_solicitud TIMESTAMP NOT NULL,
    fecha_limite TIMESTAMP,
    estado VARCHAR(20) NOT NULL DEFAULT 'Pendiente',
    id_version_documento INT NOT NULL,
    id_usuario_solicitante INT NOT NULL,
    id_usuario_aprobador INT NOT NULL,
    CONSTRAINT fk_sol_version FOREIGN KEY (id_version_documento) REFERENCES version_documento(id) ON DELETE CASCADE,
    CONSTRAINT fk_sol_solicitante FOREIGN KEY (id_usuario_solicitante) REFERENCES users(id),
    CONSTRAINT fk_sol_aprobador FOREIGN KEY (id_usuario_aprobador) REFERENCES users(id)
);

CREATE TABLE resolucion_aprobacion_documento (
    id SERIAL PRIMARY KEY,
    id_solicitud_aprobacion INT NOT NULL,
    id_usuario_revisor INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    comentario_aprobador VARCHAR(1000),
    fecha_resolucion TIMESTAMP NOT NULL,
    CONSTRAINT fk_res_solicitud FOREIGN KEY (id_solicitud_aprobacion) REFERENCES solicitud_aprobacion_documento(id) ON DELETE CASCADE,
    CONSTRAINT fk_res_revisor FOREIGN KEY (id_usuario_revisor) REFERENCES users(id)
);

INSERT INTO solicitud_aprobacion_documento (id, prioridad, comentario_solicitante, fecha_solicitud, fecha_limite, estado, id_version_documento, id_usuario_solicitante, id_usuario_aprobador)
SELECT 
    fad.id, 
    fad.prioridad, 
    fad.comentario_solicitante, 
    fad.date_creation, 
    fad.fecha_limite, 
    fad.status, 
    fad.id_version_documento, 
    vd.id_user_creation,
    fad.id_user_aprobador
FROM flujo_aprobacion_documento fad
JOIN version_documento vd ON fad.id_version_documento = vd.id;

SELECT setval('solicitud_aprobacion_documento_id_seq', COALESCE((SELECT MAX(id)+1 FROM solicitud_aprobacion_documento), 1), false);

INSERT INTO resolucion_aprobacion_documento (id_solicitud_aprobacion, id_usuario_revisor, status, comentario_aprobador, fecha_resolucion)
SELECT 
    id, 
    id_user_aprobador, 
    status, 
    comentario_aprobador, 
    COALESCE(fecha_resolucion, date_update)
FROM flujo_aprobacion_documento
WHERE status IN ('Aprobado', 'Rechazado');

ALTER TABLE historial_aprobacion_documento RENAME COLUMN id_flujo_aprobacion TO id_solicitud_aprobacion;
ALTER TABLE historial_aprobacion_documento ADD CONSTRAINT fk_hist_solicitud FOREIGN KEY (id_solicitud_aprobacion) REFERENCES solicitud_aprobacion_documento(id) ON DELETE CASCADE;

DROP TABLE flujo_aprobacion_documento;


-- 3. SEGREGACIÓN DE ARCHIVO BINARIO (RENDIMIENTO)
CREATE TABLE version_documento_archivo (
    id_version_documento INT PRIMARY KEY,
    archivo BYTEA NOT NULL,
    CONSTRAINT fk_ver_doc_arch FOREIGN KEY (id_version_documento) REFERENCES version_documento(id) ON DELETE CASCADE
);

INSERT INTO version_documento_archivo (id_version_documento, archivo)
SELECT id, archivo
FROM version_documento
WHERE archivo IS NOT NULL;

ALTER TABLE version_documento DROP COLUMN archivo;
