package com.me.mall.common;

import org.springframework.beans.factory.annotation.Value;

/**
 * 描述：常量值
 */
public class Constant {
    public static final String MY_MALL_USER = "my_mall_user";
    public static final String SALT = "8qkhfjlrk!@Y%^i]ws";
    @Value("${file.upload.dir}")
    public static String FILE_UPLOAD_DIR;
}
