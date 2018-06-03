package org.goodfox.gwola.sys.dao;

import org.goodfox.gwola.sys.entity.SysUserRoleEO;
import org.goodfox.gwola.util.persistence.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

@Component
public class SysUserRoleEODao extends BaseRepositoryImpl<SysUserRoleEO, String> {

    @Autowired
    public SysUserRoleEODao(EntityManager entityManager) {
        super(JpaEntityInformationSupport.getEntityInformation(SysUserRoleEO.class, entityManager), entityManager);
    }

    public void deleteRoleWhereNotIn(String userId, List<String> roleIdList) {
        executeUpdate("delete from SysUserRoleEO where userId = ?1 and roleId not in ?2", userId, roleIdList);
    }
}