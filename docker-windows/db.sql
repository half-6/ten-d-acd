CREATE DATABASE tend;
\c tend
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
  status tp_status default 'active',
  date_registered TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_updated TIMESTAMP WITH TIME ZONE
) WITH (
    OIDS = FALSE
);

TRUNCATE public.cancer_type, public.machine_type RESTART IDENTITY;
insert into public.cancer_type(cancer_type_name,cancer_type_short_name,cancer_type_chinese_name,status)
values ('Thyroid nodules','TH','甲状腺癌','active'),
       ('Breast tumours','BR','乳腺癌','active'),
       ('Prostate lesions','PR','前列腺癌','deleted'),
       ('Kidney lesions','KD','肾脏癌','deleted'),
       ('Lymphoma','LY','淋巴癌','deleted');
insert into public.machine_type(machine_type_name,machine_type_chinese_name)
values ('Philips','飞利浦'),
       ('Samsung','三星'),
       ('Siemens','西门子'),
       ('Toshiba','东芝'),
       ('GE','通用电气'),
       ('eSaote','百胜');
       
DROP VIEW IF EXISTS public.v_roi_image cascade;
CREATE VIEW v_roi_image AS  SELECT record_external_id, roi_image.*,cancer_type_name,machine_type_name,record.cancer_type_id,record.machine_type_id
from roi_image
left join record using(record_id)
left join cancer_type using (cancer_type_id)
left join machine_type using (machine_type_id)
where roi_image.status = 'active';


DROP VIEW IF EXISTS public.v_agg_cancer_type cascade;
CREATE VIEW v_agg_cancer_type AS
SELECT cancer_type_name
     ,count(1) as number_diagnostics
     ,max(processing_time)
     ,min(processing_time)
     ,avg(processing_time)
     ,sum(CASE WHEN prediction = pathology::TEXT and prediction = 'Malignant' THEN 1 ELSE 0 END) AS TP
     ,sum(CASE WHEN prediction = pathology::TEXT and prediction = 'Benign' THEN 1 ELSE 0 END) AS TN
     ,sum(CASE WHEN pathology = 'Malignant'  and prediction = 'Benign' THEN 1 ELSE 0 END) AS FN
     ,sum(CASE WHEN pathology = 'Benign'  and prediction = 'Malignant' THEN 1 ELSE 0 END) AS FP
from v_roi_image
group by cancer_type_name
order by cancer_type_name;

DROP VIEW IF EXISTS public.v_cancer_type cascade;
CREATE VIEW v_cancer_type AS  SELECT * from cancer_type
where status = 'active';

DROP VIEW IF EXISTS public.v_machine_type cascade;
CREATE VIEW v_machine_type AS  SELECT * from machine_type
where status = 'active';


