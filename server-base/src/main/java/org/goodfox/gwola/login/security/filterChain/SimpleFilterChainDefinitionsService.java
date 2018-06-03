package org.goodfox.gwola.login.security.filterChain;

import org.goodfox.gwola.util.constant.GlobalConfig;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleFilterChainDefinitionsService extends AbstractFilterChainDefinitionsService {

    @Override
    public Map<String, String> initOtherPermission() {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/poserver.zz", "anon");
        filterChainDefinitionMap.put("/sealsetup.exe", "anon");
        filterChainDefinitionMap.put("/posetup.exe", "anon");
        filterChainDefinitionMap.put("/pageoffice.js", "anon");
        filterChainDefinitionMap.put("/jquery.min.js", "anon");
        filterChainDefinitionMap.put("/pobstyle.css", "anon");
        filterChainDefinitionMap.put(GlobalConfig.getRestApiPath() + "/bid/project/*/supplier/*/apply", "anon");
        return filterChainDefinitionMap;
    }

}