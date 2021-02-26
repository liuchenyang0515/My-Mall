package com.me.mall.model.request;

import javax.validation.constraints.NotNull;

public class CreateOrderReq {
    @NotNull
    private String receiverName;
    @NotNull
    private String receiverMobile;
    @NotNull
    private String receiverAddress;
    @NotNull
    private Integer postage = 0; // 包邮
    @NotNull
    private Integer paymentType = 1; // 在线扫码支付

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public Integer getPostage() {
        return postage;
    }

    public void setPostage(Integer postage) {
        this.postage = postage;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }
}