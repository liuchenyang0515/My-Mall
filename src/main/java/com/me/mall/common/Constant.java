package com.me.mall.common;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;


/**
 * 描述：常量值
 */
@Component // 交给spring管理，否则注入不进入，为null
public class Constant {
    public static final String MY_MALL_USER = "my_mall_user";
    public static final String SALT = "8qkhfjlrk!@Y%^i]ws";

    // 对于static静态变量，不能简单的注入，否则为null，需要在set方法上注入
    public static String FILE_UPLOAD_DIR;

    @Value("${file.upload.dir}") // 利用set方法对静态变量赋值
    public void setFileUploadDir(String fileUploadDir) {
        FILE_UPLOAD_DIR = fileUploadDir;
    }

    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price desc", "price asc");
    }

    // 定义枚举
    public interface SaleStatus {
        int NOT_SALE = 0; // 商品下架状态
        int SALE = 1; // 商品上架状态
    }

    // 定义枚举
    public interface Cart {
        int UN_CHECKED = 0; // 购物车未选中状态
        int CHECKED = 1; // 购物车选中状态
    }
}
