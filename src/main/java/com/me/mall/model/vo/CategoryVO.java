package com.me.mall.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Cannot serialize; nested exception is org.springframework.core.serializer.support.SerializationFailedException:
// Failed to serialize object using DefaultSerializer;
// nested exception is java.io.NotSerializableException: com.me.mall.model.vo.CategoryVO
// 如果没有实现Serializable接口，那么就不能存到序列化后存到缓存里，直接报错
public class CategoryVO implements Serializable {
    private Integer id;

    private String name;

    private Integer type;

    private Integer parentId;

    private Integer orderNum;

    private Date createTime;

    private Date updateTime;

    private List<CategoryVO> childCategory = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<CategoryVO> getChildCategory() {
        return childCategory;
    }

    public void setChildCategory(List<CategoryVO> childCategory) {
        this.childCategory = childCategory;
    }
}