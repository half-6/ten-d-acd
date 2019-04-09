-- CREATE DATABASE tend;
CREATE SCHEMA IF NOT EXISTS public;
CREATE EXTENSION IF NOT EXISTS "pgcrypto";


DROP TYPE IF EXISTS tp_pathology_status cascade;
CREATE TYPE tp_pathology_status AS ENUM('Malignant', 'Benign');

DROP TYPE IF EXISTS tp_status cascade;
CREATE TYPE tp_status AS ENUM ('active','deleted');

DROP TABLE IF EXISTS public.cancer_type cascade;
CREATE TABLE public.cancer_type(
  cancer_type_id  SERIAL PRIMARY KEY,
  cancer_type_name VARCHAR(200) NOT NULL,
  cancer_type_chinese_name VARCHAR(200) NOT NULL,
  cancer_type_short_name VARCHAR(20) NOT NULL,
  status tp_status default 'active',
  date_registered TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_updated TIMESTAMP WITH TIME ZONE
) WITH (
    OIDS = FALSE
);

DROP TABLE IF EXISTS public.machine_type cascade;
CREATE TABLE public.machine_type(
  machine_type_id  SERIAL PRIMARY KEY,
  machine_type_name VARCHAR(200) NOT NULL,
  machine_type_chinese_name VARCHAR(200) NOT NULL,
  status tp_status default 'active',
  date_registered TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_updated TIMESTAMP WITH TIME ZONE
) WITH (
    OIDS = FALSE
);


DROP TABLE IF EXISTS public.record cascade;
CREATE TABLE public.record(
  record_id  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  record_external_id VARCHAR(200) NOT NULL,
  hospital_id UUID references hospital NOT NULL,
  cancer_type_id INT references cancer_type NOT NULL,
  machine_type_id INT references machine_type NOT NULL,
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
  status tp_status default 'active',
  date_registered TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_updated TIMESTAMP WITH TIME ZONE
) WITH (
    OIDS = FALSE
);

DROP TABLE IF EXISTS public.hospital cascade;
CREATE TABLE public.hospital(
  hospital_id  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  hospital_name VARCHAR(200) NOT NULL,
  hospital_chinese_name VARCHAR(200) NOT NULL,
  hospital_address VARCHAR(400),
  status tp_status default 'active',
  date_registered TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_updated TIMESTAMP WITH TIME ZONE
) WITH (
    OIDS = FALSE
);

DROP TABLE IF EXISTS public.jobs cascade;
CREATE TABLE public.jobs(
  jobs_id  SERIAL PRIMARY KEY,
  logs VARCHAR(500),
  complete bool default false,
  date_registered TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_updated TIMESTAMP WITH TIME ZONE
) WITH (
    OIDS = FALSE
  );


DROP TABLE IF EXISTS public.roi_history cascade;
CREATE TABLE public.roi_history(
   roi_history_id  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

   original_image UUID NOT NULL,
   roi_image UUID NOT NULL,
   hospital_id UUID references hospital NOT NULL,
   cancer_type_id INT references cancer_type NOT NULL,

   prediction VARCHAR(50),
   probability decimal,
   processing_time decimal,

   date_registered TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
   date_updated TIMESTAMP WITH TIME ZONE
) WITH (
    OIDS = FALSE
  );
