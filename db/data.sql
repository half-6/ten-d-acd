TRUNCATE public.cancer_type, public.machine_type RESTART IDENTITY;
insert into public.cancer_type(cancer_type_name,cancer_type_short_name,status)
values ('Thyroid nodules','TH','active'),('Breast tumours','BR','active'),('Prostate lesions','PR','deleted'),('Kidney lesions','KD','deleted'),('Lymphoma','LY','deleted');
insert into public.machine_type(machine_type_name)
values ('Philips'),('Samsung');