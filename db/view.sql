DROP VIEW IF EXISTS public.v_roi_image cascade;
CREATE VIEW v_roi_image AS  SELECT record_external_id, roi_image.*,cancer_type_name,machine_type_name,record.cancer_type_id,record.machine_type_id
from roi_image
left join record using(record_id)
left join cancer_type using (cancer_type_id)
left join machine_type using (machine_type_id);
;
