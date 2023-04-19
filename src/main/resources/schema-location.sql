-- Table: public.location

DROP TABLE IF EXISTS public.location;

CREATE TABLE IF NOT EXISTS public.location
(
  id       SERIAL   PRIMARY KEY,
  geometry GEOMETRY NOT NULL,
  name     CHARACTER VARYING(255) COLLATE pg_catalog."default",
  user_id  CHARACTER VARYING(30) COLLATE pg_catalog."default"
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.location
  OWNER TO postgres;

-- Index: sidx_location_geometry

-- DROP INDEX public.sidx_location_geometry;

CREATE INDEX IF NOT EXISTS sidx_location_geometry
  ON public.location USING GIST
  (geometry)
TABLESPACE pg_default;