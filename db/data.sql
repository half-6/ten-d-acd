TRUNCATE public.cancer_type, public.machine_type RESTART IDENTITY;
insert into public.cancer_type(cancer_type_name,cancer_type_short_name,cancer_type_chinese_name,status)
values ('Thyroid nodules','TH','结节性甲状腺肿','active'),
       ('Breast tumours','BR','乳腺肿块','active'),
       ('Prostate lesions','PR','前列腺病变','deleted'),
       ('Kidney lesions','KD','肾脏病变','deleted'),
       ('Lymphoma','LY','淋巴瘤','deleted');
insert into public.machine_type(machine_type_name,machine_type_chinese_name)
values ('Philips','飞利浦'),
       ('Samsung','三星'),
       ('Siemens','西门子'),
       ('Toshiba','东芝'),
       ('GE','通用电气'),
       ('eSaote','百胜');