package com.tend.acd.server.repository;

import LinkFuture.DB.DBHelper.GenericDBRepository;
import org.springframework.stereotype.Repository;

/**
 * Module Name: JwtRepository
 * Project Name: feifanuniv-search-api
 * Created by Cyokin on 5/30/2018
 */
@SuppressWarnings("unused")
@Repository
public class RoiImageRepository extends GenericDBRepository {

    public RoiImageRepository() {
        super("v_roi_image");
    }
}
