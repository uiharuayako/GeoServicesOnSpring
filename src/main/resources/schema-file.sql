CREATE TABLE IF NOT EXISTS public.file
(
    id          SERIAL   PRIMARY KEY,
    file_name   TEXT     NOT NULL,
    url         TEXT     NOT NULL,
    description TEXT     ,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
    )
    WITH (
        OIDS = FALSE
        )
    TABLESPACE pg_default;