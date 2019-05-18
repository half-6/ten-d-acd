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
where roi_image.status = 'active';

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
where status = 'active';

DROP VIEW IF EXISTS public.v_ai_version cascade;
CREATE VIEW v_ai_version AS
SELECT distinct ai_version from roi_image;


/*****************************
ADMIN ONLY
*****************************/
DROP FUNCTION IF EXISTS psp_ai_aggregation ;
CREATE OR REPLACE FUNCTION psp_ai_aggregation(hospitalId UUID,machineTypeId INT,aiVersion VARCHAR(200))
  RETURNS refcursor
AS $$
DECLARE
  ref1 refcursor;
BEGIN
  OPEN ref1 FOR
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
    WHERE
      ( machineTypeId is null OR v_roi_image.machine_type_id = machineTypeId)
      AND ( aiVersion is null OR v_roi_image.ai_version = aiVersion)
      AND ( hospitalId is null OR v_roi_image.hospital_id = hospitalId)
    group by cancer_type_name
    order by cancer_type_name;
  RETURN ref1;
END; $$
  LANGUAGE 'plpgsql';