package com.woniu.zerenliandemo;


public class AuthHandler extends Handler {
    @Override
    public void doHandler(User user) {
        if (!"管理员".equals(user.getRoleName())) {
            System.out.println("您不是管理员，没有操作权限");
            return;
        }
        if (null != next) {
            next.doHandler(user);
        }
    }
}

