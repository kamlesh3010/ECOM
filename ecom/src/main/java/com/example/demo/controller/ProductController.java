package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/product")
    public ResponseEntity< List<Product>> getAllProduct() {
        return new ResponseEntity<> (service.getAllProduct(), HttpStatus.OK);
    }
    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProd(@PathVariable int id){

        Product prod = service.getProdId(id);
        if (prod != null) {
            return new ResponseEntity<>(service.getProdId(id), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile) {
        try {
            Product product1 = service.addProduct(product, imageFile);
            return new ResponseEntity<>(product1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);


        }
    }
    @GetMapping("/product/{prodID}/image")
    public ResponseEntity<byte[]> getImageByProd(@PathVariable int prodID) {

        Product product = service.getProdId(prodID);
        byte[] imageFile = product.getImageData();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product.getImageType()))
                .body(imageFile);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProd(@PathVariable int id,
                                             @RequestPart Product product,

                                             @RequestPart MultipartFile imageFile) {
        Product product1= null;
        try {
            product1 = service.updateProd(id,product,imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (product1 != null){
              return new  ResponseEntity<>("Updated",HttpStatus.OK);
           }
           else {
               return  new ResponseEntity<>("Failed To Update",HttpStatus.BAD_REQUEST);
           }
        }

        @DeleteMapping("/product/{id}")
        public ResponseEntity<String> deleteProd(@PathVariable int id){


            Product product =service.getProdId(id);
            if (product !=null){
                service.deleteProd(id);
                return new  ResponseEntity<>("Delete Sucsessfully ",HttpStatus.OK);
            }else{
                return  new ResponseEntity<>("Failed To Delete",HttpStatus.NOT_FOUND);
            }

        }

        @GetMapping("/product/search")
        public ResponseEntity<List<Product>> searchProd(@RequestParam String keyword){
            System.out.println("Searching with"+keyword);
            List<Product> products= service.searchProduct(keyword);
            return  new ResponseEntity<>(products,HttpStatus.OK);

        }
}