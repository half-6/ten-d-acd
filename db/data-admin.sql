TRUNCATE public.user RESTART IDENTITY CASCADE;
INSERT INTO public.user(username,password, display_name, roles,avatar)
values ('admin','$2a$10$2knOVmC6/.KMVf6BR0.QP.5JqaYacAZjWuLK67XwCX02QUYINyRUK','Admin','{admin}','/static/img/admin-avatar.gif');

