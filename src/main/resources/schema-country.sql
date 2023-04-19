-- Table: public.country

CREATE TABLE IF NOT EXISTS public.country
(
    id     SERIAL   PRIMARY KEY,
    shape  GEOMETRY(MultiPolygon, 4326) NOT NULL,
    iso_a2 CHARACTER VARYING(2) COLLATE pg_catalog."default",
    iso_a3 CHARACTER VARYING(3) COLLATE pg_catalog."default",
    name   CHARACTER VARYING(40) COLLATE pg_catalog."default"
    )
    WITH (
        OIDS = FALSE
        )
    TABLESPACE pg_default;

ALTER TABLE public.country
    OWNER TO postgres;

-- Index: sidx_country_shape

-- DROP INDEX public.sidx_country_shape;

CREATE INDEX IF NOT EXISTS sidx_country_shape
    ON public.country USING GIST
    (shape)
    TABLESPACE pg_default;

TRUNCATE TABLE public.country;

