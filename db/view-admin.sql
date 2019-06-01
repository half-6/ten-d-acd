/*****************************
ADMIN ONLY
*****************************/
DROP VIEW IF EXISTS public.v_hospital cascade;
CREATE VIEW public.v_hospital AS SELECT hospital.*,c.start_date,c.expire_date
from hospital
LEFT JOIN (SELECT hospital_id, min(start_date) start_date,max(expire_date) expire_date from certificate group by hospital_id) c using(hospital_id)
where status = 'active'
ORDER BY hospital.date_registered DESC;


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

DROP VIEW IF EXISTS public.v_certificate cascade;
CREATE VIEW v_certificate AS SELECT
   certificate.*,
   hospital.hospital_name,
   hospital.hospital_chinese_name,
   public.user.display_name
 from certificate
        left join public.user using(user_id)
        left join hospital using (hospital_id)
 order by certificate.certificate_id DESC
;