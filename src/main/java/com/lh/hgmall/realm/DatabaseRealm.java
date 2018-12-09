package com.lh.hgmall.realm;
import com.lh.hgmall.bean.Manager;
import com.lh.hgmall.bean.RolePermission;
import com.lh.hgmall.bean.User;
import com.lh.hgmall.service.ManagerService;
import com.lh.hgmall.service.RolePermissionService;
import com.lh.hgmall.service.RoleService;
import com.lh.hgmall.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Set;
public class DatabaseRealm extends AuthorizingRealm {
    @Autowired
    @Lazy
    ManagerService managerService;
    @Autowired
    @Lazy
    RoleService roleService;
    @Autowired
    @Lazy
    RolePermissionService rolePermissionService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String name = (String) principalCollection.getPrimaryPrincipal();
        //通过DAO获取角色和权限
        Set<String> permissions = rolePermissionService.listByManager(name);
        Set<String> roles = roleService.listRoleNamesByManager(name);

        //授权对象
        SimpleAuthorizationInfo s = new SimpleAuthorizationInfo();
        //把通过DAO获取到的角色和权限放进去
        s.setStringPermissions(permissions);
        s.setRoles(roles);
        return s;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken t = (UsernamePasswordToken) authenticationToken;
        String name= t.getPrincipal().toString();
        //获取数据库中的密码
        Manager manager = managerService.getByName(name);
        String passwordInDB = manager.getPassword();
        String salt = manager.getSalt();

        //认证信息里存放账号密码, getName() 是当前Realm的继承方法,通常返回当前类名 :databaseRealm
        //盐也放进去
        //这样通过applicationContext-shiro.xml里配置的 HashedCredentialsMatcher 进行自动校验
//            SimpleAuthenticationInfo a = new SimpleAuthenticationInfo(userName,pass,getName());
        SimpleAuthenticationInfo a = new SimpleAuthenticationInfo(name,passwordInDB, ByteSource.Util.bytes(salt),getName());
        return a;
    }
}
