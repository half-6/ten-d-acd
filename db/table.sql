-- CREATE DATABASE tend;
CREATE SCHEMA IF NOT EXISTS public;
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "chkpass";

DROP TYPE IF EXISTS tp_pathology_status cascade;
CREATE TYPE tp_pathology_status AS ENUM ('malignant','benign');

DROP TABLE IF EXISTS public.cancer_type cascade;
CREATE TABLE public.cancer_type(
  cancer_type_id  SERIAL PRIMARY KEY,
  cancer_type_name VARCHAR(200) NOT NULL,
  date_registered TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_updated TIMESTAMP WITH TIME ZONE
) WITH (
    OIDS = FALSE
);

DROP TABLE IF EXISTS public.machine_type cascade;
CREATE TABLE public.machine_type(
  machine_type_id  SERIAL PRIMARY KEY,
  machine_type_name VARCHAR(200) NOT NULL,
  date_registered TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_updated TIMESTAMP WITH TIME ZONE
) WITH (
    OIDS = FALSE
);


DROP TABLE IF EXISTS public.record cascade;
CREATE TABLE public.record(
  record_id  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  record_external_id VARCHAR(200) NOT NULL,
  cancer_type_id INT NOT NULL,
  machine_type_id INT NOT NULL,
  date_registered TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_updated TIMESTAMP WITH TIME ZONE
) WITH (
    OIDS = FALSE
);

DROP TABLE IF EXISTS public.roi_image cascade;
CREATE TABLE public.roi_image(
  roi_image_id  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  record_id UUID references record NOT NULL ,
  original_image UUID NOT NULL,
  roi_image UUID NOT NULL,

  prediction VARCHAR(50),
  probability decimal,
  processing_time decimal,

  pathology tp_pathology_status,
  date_registered TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_updated TIMESTAMP WITH TIME ZONE
) WITH (
    OIDS = FALSE
);

