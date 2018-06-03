package org.goodfox.gwola.login.security;

import org.goodfox.gwola.user.constant.VerifyCodeTypeEnum;
import org.goodfox.gwola.login.security.exception.CaptchaException;
import org.goodfox.gwola.login.util.UserUtils;
import org.goodfox.gwola.user.entity.UserInfoEO;
import org.goodfox.gwola.user.service.UserInfoEOService;
import org.goodfox.gwola.util.exception.GwolaBaseException;
import org.goodfox.gwola.util.http.CookieUtils;
import org.goodfox.gwola.util.utils.RedisUtil;
import org.goodfox.gwola.util.utils.SpringContextHolder;
import org.goodfox.gwola.util.utils.StringUtils;
import org.goodfox.gwola.util.utils.cipher.Encodes;
import org.goodfox.gwola.util.utils.cipher.password.PasswordUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.util.WebUtils;
import org.goodfox.gwola.util.utils.SpringContextHolder;
import org.goodfox.gwola.util.utils.StringUtils;
import org.goodfox.gwola.util.utils.cipher.Encodes;
import org.goodfox.gwola.util.utils.cipher.password.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统安全认证实现类
 * @author ThinkGem
 * @version 2013-5-29
 */
@Service("systemAuthorizingRealm")
public class SystemAuthorizingRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(SystemAuthorizingRealm.class);

    private UserInfoEOService userService;

    private RedisUtil redisUtil;

    private UserInfoEOService getUserService() {
    	if(userService == null) {
    		userService = SpringContextHolder.getBean(UserInfoEOService.class);
    	}
    	return userService;
    }

    private RedisUtil getRedisUtil() {
        if(redisUtil == null) {
            redisUtil = SpringContextHolder.getBean(RedisUtil.class);
        }
        return redisUtil;
    }

    /**
     * 认证回调函数, 登录时调用
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        OneUsernamePasswordToken token = (OneUsernamePasswordToken) authcToken;

        // 如果登录失败超过3次需要验证码
        if (UserUtils.isNeedValidCode(token.getUsername())) {
            HttpServletRequest request = WebUtils.getHttpRequest(SecurityUtils.getSubject());
            String cookieValue = CookieUtils.getCookieValue(request);
            String validCode = getRedisUtil().get(cookieValue + "_" + VerifyCodeTypeEnum.LOGIN.getCode());
            if (token.getCaptcha() == null || !token.getCaptcha().toUpperCase().equals(validCode)) {
                throw new CaptchaException("验证码错误.");
            }
            getRedisUtil().remove(cookieValue + "_" + VerifyCodeTypeEnum.LOGIN.getCode());
        }

        UserInfoEO user = getUserService().getUserByLoginName(token.getUsername());
        if (user == null) {
            return null;
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            throw new GwolaBaseException("用户密码为空");
        }

        byte[] salt = Encodes.decodeHex(user.getPassword().substring(0, 16));
        return new SimpleAuthenticationInfo(new Principal(user), user.getPassword().substring(16), ByteSource.Util.bytes(salt), getName());
    }


    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Principal principal = (Principal) getAvailablePrincipal(principals);
        UserInfoEO user = userService.getUserByLoginName(principal.getLoginName());
        if (user != null) {
            UserUtils.flushUserLoginTimeAndIp();
            return UserUtils.getAuthInfo();
        } else {
            return null;
        }
    }

    /**
     * 设定密码校验的Hash算法与迭代次数
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(PasswordUtils.HASH_ALGORITHM);
        matcher.setHashIterations(PasswordUtils.HASH_INTERATIONS);
        setCredentialsMatcher(matcher);
    }

    /**
     * 清空用户关联权限认证，待下次使用时重新加载
     */
    public void clearCachedAuthorizationInfo(String principal) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
        clearCachedAuthorizationInfo(principals);
    }

    /**
     * 清空所有关联认证
     */
    public void clearAllCachedAuthorizationInfo() {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null) {
            for (Object key : cache.keys()) {
                cache.remove(key);
            }
        }
    }


    /**
     * 授权用户信息
     */
    public static class Principal implements Serializable {

        private static final long serialVersionUID = 1L;

        private String id;
        private String loginName;
        private String name;
        private Map<String, Object> cacheMap;

        public Principal(UserInfoEO user) {
            this.id = user.getUserId();
            this.loginName = user.getName();
            this.name = user.getName();
        }

        public String getId() {
            return id;
        }

        public String getLoginName() {
            return loginName;
        }

        public String getName() {
            return name;
        }

        public Map<String, Object> getCacheMap() {
            if (cacheMap == null) {
                cacheMap = new HashMap<String, Object>();
            }
            return cacheMap;
        }

    }
}
