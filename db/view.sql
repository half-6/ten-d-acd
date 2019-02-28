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

