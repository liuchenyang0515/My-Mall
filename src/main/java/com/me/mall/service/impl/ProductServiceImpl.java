package com.me.mall.service.impl;

import com.me.mall.common.ApiRestResponse;
import com.me.mall.common.Constant;
import com.me.mall.exception.MyMallException;
import com.me.mall.exception.MyMallExceptionEnum;
import com.me.mall.model.dao.ProductMapper;
import com.me.mall.model.pojo.Product;
import com.me.mall.model.request.AddProductReq;
import com.me.mall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * 描述：商品服务实现类
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductMapper productMapper;

    @Override
    public void add(AddProductReq addProductReq) {
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq, product);
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if (productOld != null) {
            throw new MyMallException(MyMallExceptionEnum.NAME_EXISTED);
        }
        int count = productMapper.insertSelective(product);
        if (count == 0) {
            throw new MyMallException(MyMallExceptionEnum.CREATE_FAILED);
        }
    }

    @Override
    public ApiRestResponse upload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        // 获取文件的原始名字mihoutao2.jpg
        String originalFilename = file.getOriginalFilename();
        String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 生成文件名称UUID
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + suffixName;
        // 创建文件
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        // 如果文件夹不存在，那么就去创建
        if (!fileDirectory.exists()) {
            // 如果创建文件夹失败，抛出异常
            if (!fileDirectory.mkdir()) {
                throw new MyMallException(MyMallExceptionEnum.MKDIR_FAILED);
            }
        }
        try {
            // 把文件写到最后生成好的路径
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return ApiRestResponse.success(getHost(new URI(request.getRequestURL() + ""))
                    + "/images/" + newFileName);
        } catch (URISyntaxException e) {
            return ApiRestResponse.error(MyMallExceptionEnum.UPLOAD_FAILED);
        }
    }

    // uri: "http://127.0.0.1:8083/admin/upload/file"
    private URI getHost(URI uri) {
        URI effectiveURI;
        try {
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(),
                    uri.getHost(), uri.getPort(),
                    null, null, null);
        } catch (URISyntaxException e) {
            effectiveURI = null;
        }
        return effectiveURI;
    }

    @Override
    public void update(Product updateProduct) {
        Product productOld = productMapper.selectByName(updateProduct.getName());
        // 同名且不同id，不能继续修改
        if (productOld != null && !productOld.getId().equals(updateProduct.getId())) {
            throw new MyMallException(MyMallExceptionEnum.NAME_EXISTED);
        }
        int count = productMapper.updateByPrimaryKeySelective(updateProduct);
        if (count == 0) {
            throw new MyMallException(MyMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void delete(Integer id) {
        Product productOld = productMapper.selectByPrimaryKey(id);
        // 查不到该记录，无法删除
        if (productOld == null) {
            throw new MyMallException(MyMallExceptionEnum.DELETE_FAILED);
        }
        int count = productMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new MyMallException(MyMallExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus) {
        productMapper.batchUpdateSellStatus(ids, sellStatus);
    }
}
