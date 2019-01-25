TRUNCATE public.cancer_type, public.machine_type RESTART IDENTITY;
insert into public.cancer_type(cancer_type_name)
values ('Thyroid nodules'),('Breast tumours'),('Prostate lesions'),('Lung lesions'),('Kidney lesions'),('Lymphoma');
insert into public.machine_type(machine_type_name)
values ('Philips'),('Samsung');