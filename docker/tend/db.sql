CREATE DATABASE tend;
\c tend
-- CREATE DATABASE tend;
/*****************************
CREATE TABLE
*****************************/
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

  coordinate jsonb,
  ai_version VARCHAR(50),
  detection_result jsonb,

  pathology tp_pathology_status,
  status tp_status default 'active',
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

   coordinate jsonb,
   ai_version VARCHAR(50),
   detection_result jsonb,

   prediction VARCHAR(50),
   probability decimal,
   processing_time decimal,

   date_registered TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
   date_updated TIMESTAMP WITH TIME ZONE
) WITH (
    OIDS = FALSE
  );

/*****************************
CREATE VIEW
*****************************/
DROP VIEW IF EXISTS public.v_roi_image cascade;
CREATE VIEW v_roi_image AS SELECT
    record_external_id,
    roi_image.*,
    record.cancer_type_id,
    cancer_type_name,
    record.machine_type_id,
    machine_type_name,
    record.hospital_id,
    hospital.hospital_name
from roi_image
left join record using(record_id)
left join cancer_type using (cancer_type_id)
left join machine_type using (machine_type_id)
left join hospital using (hospital_id)
where roi_image.status = 'active'
order by record.date_registered DESC
;

DROP VIEW IF EXISTS public.v_roi_history cascade;
CREATE VIEW v_roi_history AS SELECT
     roi_history.*,
     cancer_type_name,
     hospital.hospital_name
 from roi_history
          left join cancer_type using (cancer_type_id)
          left join hospital using (hospital_id);

DROP VIEW IF EXISTS public.v_agg_cancer_type cascade;
CREATE VIEW v_agg_cancer_type AS
SELECT ai_version
     ,cancer_type_name
     ,count(1) as number_diagnostics
     ,max(processing_time)
     ,min(processing_time)
     ,avg(processing_time)
     ,sum(CASE WHEN prediction = pathology::TEXT and prediction = 'Malignant' THEN 1 ELSE 0 END) AS TP
     ,sum(CASE WHEN prediction = pathology::TEXT and prediction = 'Benign' THEN 1 ELSE 0 END) AS TN
     ,sum(CASE WHEN pathology = 'Malignant'  and prediction = 'Benign' THEN 1 ELSE 0 END) AS FN
     ,sum(CASE WHEN pathology = 'Benign'  and prediction = 'Malignant' THEN 1 ELSE 0 END) AS FP
from v_roi_image
group by ai_version, cancer_type_name
order by ai_version, cancer_type_name;

DROP VIEW IF EXISTS public.v_agg_cancer_type_all cascade;
CREATE VIEW v_agg_cancer_type_all AS
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

DROP VIEW IF EXISTS public.v_hospital cascade;
CREATE VIEW v_hospital AS  SELECT * from hospital
where status = 'active'
ORDER BY date_registered DESC;

DROP VIEW IF EXISTS public.v_ai_version cascade;
CREATE VIEW v_ai_version AS
SELECT distinct ai_version from roi_image;


/*****************************
INJECT DATE
*****************************/
TRUNCATE public.cancer_type, public.machine_type, public.hospital RESTART IDENTITY CASCADE;
insert into public.cancer_type(cancer_type_id,cancer_type_name,cancer_type_short_name,cancer_type_chinese_name,status)
values (1,'Thyroid nodules','TH','甲状腺癌','active');

insert into public.machine_type(machine_type_id, machine_type_name,machine_type_chinese_name)
values (1,'Philips','飞利浦'),
       (2,'Samsung','三星'),
       (3,'Siemens','西门子'),
       (4,'Toshiba','东芝'),
       (5,'GE','通用电气'),
       (6,'eSaote','百胜'),
       (7,'Supersonic','声科'),
       (8,'Hitachi','日立')
       ;
INSERT INTO public.hospital(hospital_id,hospital_name,hospital_chinese_name) values ('3b861d22-c856-48dc-a2af-a8faed1ad545','Signs Only','送检专用');