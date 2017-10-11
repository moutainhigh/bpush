package com.beyond.bpush.core.service;

import com.beyond.bpush.core.TxMain;
import com.beyond.bpush.core.entity.Product;

import java.util.List;

/**
 * Created by user on 1/28/15.
 */
public interface ProductService {

    /**
     *
     * @param key
     * @return Integer
     */
    Integer getProductId(String key);

    /**
     *
     * @param key
     * @return Product
     */
    Product findByKey(String key);

    @TxMain
    void add(Product product);

    List<Product> findAll();

    List<Product> findNewest(int startId);
}
