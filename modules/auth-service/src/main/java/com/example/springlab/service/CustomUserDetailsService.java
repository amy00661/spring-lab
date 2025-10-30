package com.example.springlab.service;

import com.example.springlab.model.RoleEntity;
import com.example.springlab.model.CustomUserDetails;
import com.example.springlab.model.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDetailsService")
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.debug("開始登陸驗證，用戶名為: {}",s);

        // 根據用戶名驗證用戶
//        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().eq(UserInfo::getLoginAccount,s);
//        UserInfo userInfo = userService.getOne(queryWrapper);

        UserEntity userInfo = userService.findByUsername(s);
        if (userInfo == null) {
            throw new UsernameNotFoundException("用戶名不存在，登陸失敗。");
        }

        // 構建UserDetail對象
        CustomUserDetails customUserDetail = new CustomUserDetails();
        customUserDetail.setUserInfo(userInfo);
        List<RoleEntity> roleInfoList = roleService.listRoleByUserId(userInfo.getId());
        customUserDetail.setRoleInfoList(roleInfoList);
        return customUserDetail;
    }
}