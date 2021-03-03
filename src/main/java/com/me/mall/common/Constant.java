package com.me.mall.common;

import com.google.common.collect.Sets;
import com.me.mall.exception.MyMallException;
import com.me.mall.exception.MyMallExceptionEnum;
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

    public enum OrderStatusEnum {
        CANCELED(0, "用户已取消"),
        NOT_PAID(10, "未付款"),
        PAID(20, "已付款"),
        DELIVERED(30, "已发货"),
        FINISHED(40, "交易完成");

        private String value;
        private int code;

        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public static OrderStatusEnum codeOf(int code) {
            // 这里values()是枚举列表
            for (OrderStatusEnum orderStatusEnum : values()) {
                if (orderStatusEnum.getCode() == code) {
                    return orderStatusEnum;
                }
            }
            throw new MyMallException(MyMallExceptionEnum.NO_ENUM);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
