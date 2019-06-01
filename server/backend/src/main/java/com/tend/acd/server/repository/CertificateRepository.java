package com.tend.acd.server.repository;

import LinkFuture.DB.DBHelper.DBRepository;
import com.tend.acd.server.model.request.CertificateEntity;
import org.springframework.stereotype.Repository;

/**
 * Module Name: JwtRepository
 * Project Name: feifanuniv-search-api
 * Created by Cyokin on 5/30/2018
 */
@SuppressWarnings("unused")
@Repository
public class CertificateRepository extends DBRepository<CertificateEntity,Integer> {

    public CertificateRepository() {
        super("public.certificate");
    }
}
