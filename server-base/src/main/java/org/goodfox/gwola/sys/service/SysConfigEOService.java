package org.goodfox.gwola.sys.service;

import org.goodfox.gwola.sys.dao.SysConfigEODao;
import org.goodfox.gwola.sys.entity.SysConfigEO;
import org.goodfox.gwola.util.service.CrudService;
import org.goodfox.gwola.util.utils.CollectionUtils;
import org.goodfox.gwola.sys.dao.SysConfigEODao;
import org.goodfox.gwola.sys.entity.SysConfigEO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysConfigEOService extends CrudService<SysConfigEODao, SysConfigEO, String> {

    public List<SysConfigEO> list(String typeCode) {
        return dao.list(typeCode);
    }

    public Map<String, String> map(String typeCode) {
        List<SysConfigEO> sysConfigEOList = list(typeCode);
        Map<String, String> configs = new HashMap<>();
        if (CollectionUtils.isNotEmpty(sysConfigEOList)) {
            for (SysConfigEO sysConfigEO : sysConfigEOList) {
                configs.put(sysConfigEO.getConfigKey(), sysConfigEO.getConfigValue());
            }
        }
        return configs;
    }

    public void update(String key, String value) {
        dao.update(key, value);
    }

    public void update(Map<String, String> configs) {
        if (CollectionUtils.isEmpty(configs)) {
            return;
        }

        for (Map.Entry<String, String> entry : configs.entrySet()) {
            dao.update(entry.getKey(), entry.getValue());
        }
    }
}
