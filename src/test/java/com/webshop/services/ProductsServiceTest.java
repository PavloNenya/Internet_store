package com.webshop.services;

import com.webshop.dto.ProductDTO;
import com.webshop.exceptions.ProductNotFoundException;
import com.webshop.models.Account;
import com.webshop.models.Cart;
import com.webshop.models.Product;
import com.webshop.repositories.ProductsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProductsServiceTest {
    @Autowired
    private ProductsService productsService;
    @MockBean
    private ProductsRepository productsRepository;

    @BeforeEach
    void init() {
        var seller1 = new Account(
                321L,
                "Ivan",
                "qwerty123",
                "ivan.email@gmail.com",
                null
        );
        var seller2 = new Account(
                123L,
                "Pavlo",
                "qwerty123",
                "pavel.nenya228@gmail.com",
                null
        );

        List<Product> products = List.of(
                new Product(
                        "Title",
                        "Description",
                        25.90,
                        seller1
                ),
                new Product(
                        "Title2",
                        "Description2",
                        35.90,
                        seller2
                )
        );
        products.get(0).setId(1L);
        products.get(0).setActive(false);
        products.get(1).setId(2L);
        seller1.setProducts(List.of(products.get(0)));
        seller2.setProducts(List.of(products.get(1)));
        when(productsRepository.findProductsBySeller(any(Account.class)))
                .thenAnswer((i) -> products
                        .stream()
                        .filter(product -> product.getSeller().getId().equals(((Account) i.getArguments()[0]).getId()))
                        .toList());
        when(productsRepository.findAll()).thenAnswer(i -> {
            products.get(0).setActive(true);
            products.get(1).setActive(true);
            return products;
        });
        when(productsRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);
        when(productsRepository.findById(1L)).thenAnswer(invocation -> Optional.of(products.get(0)));
        when(productsRepository.findById(2L)).thenAnswer(invocation -> Optional.of(products.get(1)));
        doAnswer(invocation -> {
            Product product = productsRepository.findById((Long)invocation.getArguments()[0])
                    .orElseThrow(ProductNotFoundException::new);
            product.setActive(invocation.getArgument(1, Boolean.class));
            return product;
        }).when(productsRepository).updateProductActivity(any(Long.class), any(Boolean.class));

    }

    @Test
    void getAllProducts() {
        //Arrange
        var seller1 = new Account(
                321L,
                "Ivan",
                "qwerty123",
                "ivan.email@gmail.com",
                null
        );
        var seller2 = new Account(
                123L,
                "Pavlo",
                "qwerty123",
                "pavel.nenya228@gmail.com",
                null
        );
        List<Product> products = List.of(
                new Product(
                        "Title",
                        "Description",
                        25.90,
                        seller1
                ),
                new Product(
                        "Title2",
                        "Description2",
                        35.90,
                        seller2
                )
        );
        products.get(0).setId(1L);
        products.get(1).setId(2L);
        seller1.setProducts(List.of(products.get(0)));
        seller2.setProducts(List.of(products.get(1)));

        //Act
        List<ProductDTO> res = productsService.getAllProducts();

        //Assert
        Assertions.assertEquals(products.get(0).getTitle(), res.get(0).getTitle());
        Assertions.assertEquals(products.get(0).getDescription(), res.get(0).getDescription());
        Assertions.assertEquals(products.get(0).getPrice(), res.get(0).getPrice());
        Assertions.assertEquals(products.get(0).getSeller().getId(), res.get(0).getSellerId());

        Assertions.assertEquals(products.get(1).getTitle(), res.get(1).getTitle());
        Assertions.assertEquals(products.get(1).getDescription(), res.get(1).getDescription());
        Assertions.assertEquals(products.get(1).getPrice(), res.get(1).getPrice());
        Assertions.assertEquals(products.get(1).getSeller().getId(), res.get(1).getSellerId());
    }

    @Test
    void addProduct() {
        //Arrange
        Account seller = new Account(
                123L,
                "Pavlo",
                "qwerty123",
                "pavel.nenya228@gmail.com",
                null
        );
        Product product = new Product(
                "Title2",
                "Description2",
                35.90,
                seller
        );
        seller.setProducts(List.of(product));
        //Act
        var res = productsService.addProduct(product);

        //Assert
        assert res == product;
    }

    @Test
    void buyProduct() {
        //Arrange
        var seller1 = new Account(
                321L,
                "Ivan",
                "qwerty123",
                "ivan.email@gmail.com",
                null
        );
        Product product = new Product(
                "Title",
                "Description",
                25.90,
                seller1
        );
        product.setId(1L);
        seller1.setProducts(List.of(product));

        //Act
        Product res = productsService.buyProduct(product.getId());

        //Assert
        Assertions.assertFalse(res.isActive());
        Assertions.assertThrows(ProductNotFoundException.class, () -> productsService.buyProduct(-120L));
    }

    @Test
    void getProductById() {
        //Arrange
        var seller1 = new Account(
                321L,
                "Ivan",
                "qwerty123",
                "ivan.email@gmail.com",
                null
        );
        Product product = new Product(
                "Title",
                "Description",
                25.90,
                seller1
        );
        product.setId(1L);
        seller1.setProducts(List.of(product));

        //Act
        Product res = productsService.getProductById(product.getId());

        //Assert
        Assertions.assertEquals(res.getTitle(), product.getTitle());
        Assertions.assertEquals(res.getDescription(), product.getDescription());
        Assertions.assertEquals(res.getPrice(), product.getPrice());
    }

    @Test
    void findProductByName() {
        //Arrange
        List<ProductDTO> productDTOS = List.of(
                new ProductDTO(
                        "Title",
                        "Description",
                        25.90,
                        321L,
                        1L
                ),
                new ProductDTO(
                        "Title2",
                        "Description2",
                        35.90,
                        123L,
                        2L
                )
        );

        //Act
        List<ProductDTO> res = productsService.findProductsByName("Title");

        //Assert
        Assertions.assertEquals(productDTOS.get(0), res.get(0));
        Assertions.assertEquals(productDTOS.get(1), res.get(1));
    }

    @Test
    void findProductsBySeller() {
        //Arrange
        var seller = new Account(
                321L,
                "Ivan",
                "qwerty123",
                "ivan.email@gmail.com",
                null
        );
        var product = new Product(
                "Title",
                "Description",
                25.90,
                seller
        );
        product.setId(1L);
        product.setActive(false);
        seller.setProducts(List.of(product));
        //Act
        List<Product> foundProducts = productsService.findProductsBySeller(seller);

        //Assert
        Assertions.assertEquals(1, foundProducts.size());
        Assertions.assertEquals(product, foundProducts.get(0));
    }

    @Test
    void findProductById() {
        //Arrange
        Long id = 1L;
        var seller1 = new Account(
                321L,
                "Ivan",
                "qwerty123",
                "ivan.email@gmail.com",
                null
        );
        var product = new Product(
                "Title",
                "Description",
                25.90,
                seller1
        );
        product.setId(1L);
        seller1.setProducts(List.of(product));
        //Act
        Product foundedProduct = productsService.findProductById(id);

        //Assert
        Assertions.assertEquals(0, Double.compare(product.getPrice(), foundedProduct.getPrice()));
        Assertions.assertFalse(foundedProduct.isActive());
        Assertions.assertEquals(product.getId(), foundedProduct.getId());
        Assertions.assertEquals(product.getTitle(), foundedProduct.getTitle());
        Assertions.assertEquals(product.getSeller().getId(), foundedProduct.getSeller().getId());
        Assertions.assertEquals(product.getDescription(), foundedProduct.getDescription());
        Assertions.assertThrows(ProductNotFoundException.class, () -> productsService.findProductById(12333L));
    }

    @Test
    void getAllActiveProducts() {
        //Arrange
        var seller2 = new Account(
                123L,
                "Pavlo",
                "qwerty123",
                "pavel.nenya228@gmail.com",
                null
        );
        var seller1 = new Account(
                321L,
                "Ivan",
                "qwerty123",
                "ivan.email@gmail.com",
                null
        );
        var product1 = new ProductDTO(
                "Title",
                "Description",
                25.90,
                seller1.getId(),
                1L
        );
        var product2 = new ProductDTO(
                "Title2",
                "Description2",
                35.90,
                seller2.getId(),
                2L
        );
        product1.setId(1L);
        product2.setId(2L);
        seller1.setProducts(List.of(new Product(
                "Title",
                "Description",
                25.90,
                seller1
        )));
        seller2.setProducts(List.of(new Product(
                "Title2",
                "Description2",
                35.90,
                seller2
        )));

        //Act
        List<ProductDTO> res = productsService.getAllActiveProducts();

        //Assert
        Assertions.assertEquals(2, res.size());
        Assertions.assertEquals(product1, res.get(0));
        Assertions.assertEquals(product2, res.get(1));
    }


    @Test
    void deleteProduct() {
        //Arrange
        var seller = new Account(
                123L,
                "Pavlo",
                "qwerty123",
                "pavel.nenya228@gmail.com",
                null
        );
        var product = new Product(
                "Title2",
                "Description2",
                35.90,
                seller
        );
        product.setId(2L);
        seller.setProducts(List.of(product));

        //Act
        productsService.deleteProduct(2L);
        var res = productsService.findProductById(2L);
        product.setActive(false);
        //Assert
        Assertions.assertFalse(res.isActive());
        Assertions.assertEquals(res, product);
    }

    @Test
    void getProductsFromCart() {
        //Arrange
        var seller2 = new Account(
                123L,
                "Pavlo",
                "qwerty123",
                "pavel.nenya228@gmail.com",
                null
        );
        var seller1 = new Account(
                321L,
                "Ivan",
                "qwerty123",
                "ivan.email@gmail.com",
                null
        );
        var product1 = new Product(
                "Title",
                "Description",
                25.90,
                seller1
        );
        var product2 = new Product(
                "Title2",
                "Description2",
                35.90,
                seller2
        );
        product1.setId(1L);
        product2.setId(2L);
        List<Cart> carts = List.of(
                new Cart(
                        product1,
                        seller2
                ), new Cart(
                        product2,
                        seller1
                )
        );

        //Act
        var res = productsService.getProductsFromCart(carts);

        //Assert
        Assertions.assertEquals(product1, res.get(0));
        Assertions.assertEquals(product2, res.get(1));
    }

    @Test
    void buyProducts() {
        //Arrange
        var seller1 = new Account(
                321L,
                "Ivan",
                "qwerty123",
                "ivan.email@gmail.com",
                null
        );
        List<Product> products = List.of(
                new Product(
                        "Title",
                        "Description",
                        25.90,
                        seller1
                )
        );
        products.get(0).setId(1L);

        //Act
        productsService.buyProducts(products);
        var res = productsService.findProductById(1L);

        //Assert
        Assertions.assertFalse(res.isActive());
    }
}