package com.promocodes.promocodesmanager.product;

import com.promocodes.promocodesmanager.exception.ApiExceptionDto;
import com.promocodes.promocodesmanager.exception.ExceptionMapper;
import com.promocodes.promocodesmanager.exception.FailedToAddNewProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/products")
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ExceptionMapper exceptionMapper;

    @Autowired
    public ProductController(ProductService productService,
                             ProductMapper productMapper,
                             ExceptionMapper exceptionMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.exceptionMapper = exceptionMapper;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductResponseDto> productsDtoList = productMapper.toProductsDtoList(products);

        return new ResponseEntity<>(productsDtoList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addNewProduct(@RequestBody ProductDto product) {
        productService.addNewProduct(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCurrency()
        );

        return new ResponseEntity<>("Product has been successfully added", HttpStatus.CREATED);
    }
}
