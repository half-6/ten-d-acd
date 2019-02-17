DROP VIEW IF EXISTS public.v_roi_image cascade;
CREATE VIEW v_roi_image AS  SELECT record_external_id, roi_image.*,cancer_type_name,machine_type_name,record.cancer_type_id,record.machine_type_id
from roi_image
left join record using(record_id)
left join cancer_type using (cancer_type_id)
left join machine_type using (machine_type_id)
where roi_image.status = 'active';


DROP VIEW IF EXISTS public.v_agg_cancer_type cascade;
CREATE VIEW v_agg_cancer_type AS  SELECT cancer_type_name,count(1) as number_diagnostics,max(processing_time),min(processing_time),avg(processing_time) from v_roi_image
group by cancer_type_name
order by cancer_type_name;

