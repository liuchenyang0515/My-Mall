package com.me.mall.controller;

import com.github.pagehelper.PageInfo;
import com.me.mall.common.ApiRestResponse;
import com.me.mall.model.pojo.Product;
import com.me.mall.model.request.AddProductReq;
import com.me.mall.model.request.UpdateProductReq;
import com.me.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 描述：后台商品管理Controller
 */
@RestController // 相当于类里面的方法都加了@ResponseBody
public class ProductAdminController {
    @Resource
    private ProductService productService;

    /**
     * 比如127.0.0.1:8083/admin/product/add的url中的Body添加
     * {"name":"新鲜猕猴桃","categoryId":5,"price":1000,"stock":10,"status":1,"detail":"新西兰黄心，黄金奇异果",
     * "image":"http://127.0.0.1:8083/images/6037baf8-5251-4560-be5a-32b8ee3823cf.png"}
     * 那么就会对应添加到AddProductReq对象中，各个属性自动注入。
     * 最后数据insert成功，me_mall_product表会多出这条数据
     * @param addProductReq
     * @return
     */
    @PostMapping("admin/product/add")
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq) {
        productService.add(addProductReq);
        return ApiRestResponse.success();
    }

    @PostMapping("admin/upload/file")
    public ApiRestResponse upload(HttpServletRequest request, MultipartFile file) {
        return productService.upload(request, file);
    }

    @ApiOperation("后台更新商品")
    @PostMapping("admin/product/update")
    public ApiRestResponse updateProduct(@Valid @RequestBody UpdateProductReq updateProductReq) {
        Product product = new Product();
        BeanUtils.copyProperties(updateProductReq, product);
        productService.update(product);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台删除商品")
    @PostMapping("admin/product/delete")
    public ApiRestResponse deleteProduct(@RequestParam Integer id) {
        productService.delete(id);
        return ApiRestResponse.success();
    }

    /**
     * 比如让id为2、3的商品下架，就是让他们的status由1变为0
     * ids=2,3&sellStatus=0
     *
     * @param ids        需要下架的商品
     * @param sellStatus 0-下架  1-上架
     * @return
     */
    @ApiOperation("后台批量上下架接口")
    @PostMapping("admin/product/batchUpdateSellStatus")
    public ApiRestResponse batchUpdateSellStatus(@RequestParam Integer[] ids, @RequestParam Integer sellStatus) {
        productService.batchUpdateSellStatus(ids, sellStatus);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台商品列表接口")
    @GetMapping("admin/product/list")
    public ApiRestResponse list(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        PageInfo pageInfo = productService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }
}
