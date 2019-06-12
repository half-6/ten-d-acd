/*****************************
INJECT DATE
*****************************/
TRUNCATE public.cancer_type, public.machine_type, public.hospital RESTART IDENTITY CASCADE;
insert into public.cancer_type(cancer_type_id,cancer_type_name,cancer_type_short_name,cancer_type_chinese_name,status)
values (1,'Thyroid nodules','TH','甲状腺癌','active'),
       (2,'Breast tumours','BR','乳腺癌','active'),
       (3,'Prostate lesions','PR','前列腺癌','deleted'),
       (4,'Kidney lesions','KD','肾脏癌','deleted'),
       (5,'Lymphoma','LY','淋巴癌','deleted'),
       (6,'Liver','LR','肝癌','deleted');
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
INSERT INTO public.hospital(hospital_name,hospital_chinese_name) values ('Renji Hospital','仁济医院');
