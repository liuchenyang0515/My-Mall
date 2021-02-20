package com.me.mall.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 描述：AddCategoryReq请求参数封装类
 */
public class AddCategoryReq {
    /**
     * 添加@Valid参数校验之后，用对象接收请求体参数时，这个对象就不用调用一个个get方法去判断参数合法性了，bean对象直接使用注解
     * 正确示范是这样
     * {
     *     "name":"鸭货",
     *     "type":2,
     *     "parentId":6,
     *     "orderNum":10
     * }
     * 结果发送到后端成了这样
     * {
     *     "name":"鸭货伴手零食",
     *     "type":4
     * }
     * 当我们在统一异常处理类GlobalExceptionHandler处理之后
     * 前端接收到的返回值能把参数校验的错误信息一个个打印返回
     * {
     *     "status": 10012,
     *     "msg": "[orderNum不能为null, name长度必须在2~5之间, parentId不能为null, type最大只能为3]",
     *     "data": null
     * }
     */
    @Size(min = 2, max = 5, message = "name长度必须在2~5之间")
    @NotNull(message = "name不能为null")
    private String name;
    @NotNull(message = "type不能为null")
    @Max(value = 3, message = "type最大只能为3")
    private Integer type;
    @NotNull(message = "parentId不能为null")
    private Integer parentId;
    @NotNull(message = "orderNum不能为null")
    private Integer orderNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
