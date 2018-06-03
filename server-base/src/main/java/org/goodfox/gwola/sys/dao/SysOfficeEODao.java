package org.goodfox.gwola.sys.dao;

import org.goodfox.gwola.sys.entity.SysOfficeEO;
import org.goodfox.gwola.util.persistence.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public class SysOfficeEODao extends BaseRepositoryImpl<SysOfficeEO, String> {

    @Autowired
    public SysOfficeEODao(EntityManager entityManager) {
        super(JpaEntityInformationSupport.getEntityInformation(SysOfficeEO.class, entityManager), entityManager);
    }

}