package com.qnkj.core.webconfigs.authentication;


import com.github.restapi.models.Profile;
import com.qnkj.core.base.services.IProfileService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义实现 ShiroRealm，包含认证和授权两大模块
 *
 * @author Oldhand
 */
@Component
public class ShiroRealm extends AuthorizingRealm {

    private IProfileService profileService;


    @Autowired
    public void setProfileService(IProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * 授权模块，获取用户角色和权限
     *
     * @param principal principal
     * @return AuthorizationInfo 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        Profile user = (Profile) SecurityUtils.getSubject().getPrincipal();

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

//      获取用户角色集
        Set<String> roleSet = new HashSet<String>();
        roleSet.add("注册账户");
        simpleAuthorizationInfo.setRoles(roleSet);

//        获取用户权限集
        Set<String> permissionSet = new HashSet<String>();
        permissionSet.add("user:view");
        permissionSet.add("user:add");
        permissionSet.add("user:export");
        simpleAuthorizationInfo.setStringPermissions(permissionSet);

        return simpleAuthorizationInfo;
    }

    /**
     * 用户认证
     *
     * @param token AuthenticationToken 身份认证 token
     * @return AuthenticationInfo 身份认证信息
     * @throws AuthenticationException 认证相关异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取用户输入的用户名和密码
        String profileid = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        try {
            Profile profileInfo = this.profileService.load(profileid);
            if (this.profileService.sign(profileid,password)) {
                return new SimpleAuthenticationInfo(profileInfo, password, getName());
            }
            throw new Exception("用户名或密码错误！");
        } catch (Exception e) {
            throw new IncorrectCredentialsException("用户名或密码错误！");
        }
    }

    /**
     * 清除当前用户权限缓存
     * 使用方法：在需要清除用户权限的地方注入 ShiroRealm,
     * 然后调用其 clearCache方法。
     */
    public void clearCache() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }
}
