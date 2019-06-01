/*****************************
ADMIN ONLY
*****************************/
DROP TABLE IF EXISTS public.user cascade;
CREATE TABLE public.user(
  user_id SERIAL PRIMARY KEY,
  username VARCHAR(200) NOT NULL unique,
  password VARCHAR(200) NOT NULL,
  display_name VARCHAR(200),
  roles VARCHAR(50)[],
  avatar VARCHAR(50),
  status tp_status default 'active',
  date_registered TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_updated TIMESTAMP WITH TIME ZONE
) WITH (
    OIDS = FALSE
  );

DROP TABLE IF EXISTS public.certificate cascade;
CREATE TABLE public.certificate(
  certificate_id  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  hospital_id UUID references hospital NOT NULL,
  user_id BIGINT references public.user NOT NULL,
  start_date TIMESTAMP WITH TIME ZONE NOT NULL,
  expire_date TIMESTAMP WITH TIME ZONE NOT NULL,
  date_registered TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_updated TIMESTAMP WITH TIME ZONE
) WITH (
    OIDS = FALSE
  );