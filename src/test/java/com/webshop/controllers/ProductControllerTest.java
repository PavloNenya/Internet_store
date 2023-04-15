package com.webshop.controllers;

import com.webshop.dto.ProductDTO;
import org.junit.Before;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@Order(3)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(value = "new.email@gmail.com", userDetailsServiceBeanName = "userDetailsService")
    @WithMockUser
    @Order(1)
    @Before
    public void postAddProduct() throws Exception {
        mockMvc.perform(post("/add-product")
                        .param("title", "testtitle")
                        .param("description", "testdescription")
                        .param("price", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(content().string(containsString("Product added successfully!")));
    }

    @Test
    @WithUserDetails(value = "new.email@gmail.com", userDetailsServiceBeanName = "userDetailsService")
    @WithMockUser
    @Order(2)
    void productDeleteGet() throws Exception {
        mockMvc.perform(get("/products/1/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("product"))
                .andExpect(model().attributeExists("delete"))
                .andExpect(content().string(containsString("You sure you want to delete this product?")));
    }

    @Test
    @Order(3)
    void searchPage() throws Exception {

        mockMvc.perform(get("/search")
                        .param("search", "test"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Title")))
                .andExpect(content().string(containsString("Price")))
                .andExpect(content().string(containsString("Description")))
                .andExpect(content().string(containsString("Found")));

        mockMvc.perform(get("/search")
                        .param("search", "THIS PRODUCT DOES NOT EXIST"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("No products found")));
    }

    @Test
    @Order(4)
    void getAllProducts() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Title")))
                .andExpect(content().string(containsString("Price")))
                .andExpect(content().string(containsString("Description")))
                .andExpect(content().string(containsString("/products/")));
    }

    @Test
    @WithUserDetails(value = "new.email@gmail.com", userDetailsServiceBeanName = "userDetailsService")
    @WithMockUser()
    @Order(5)
    void productPage() throws Exception {
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Title")))
                .andExpect(content().string(containsString("Description")))
                .andExpect(content().string(containsString("testtitle")))
                .andExpect(content().string(containsString("testdescription")))
                .andExpect(content().string(containsString("123")))
                .andExpect(content().string(containsString("Price")));

        mockMvc.perform(get("/products/-12"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Product not found!")));
    }

    @Test
    @WithUserDetails(value = "new.email@gmail.com", userDetailsServiceBeanName = "userDetailsService")
    @WithMockUser
    @Order(6)
    void productDeletePost() throws Exception {
        mockMvc.perform(post("/products/1/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(content().string(containsString("Product deleted successfully!")));
    }

    @Test
    @WithUserDetails(value = "new.email@gmail.com", userDetailsServiceBeanName = "userDetailsService")
    @WithMockUser
    @Order(7)
    void getAddProduct() throws Exception {

        mockMvc.perform(get("/add-product")
                        .flashAttr("product", new ProductDTO()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("product"))
                .andExpect(view().name("add-product"))
                .andExpect(content().string(containsString("Title")))
                .andExpect(content().string(containsString("Description")))
                .andExpect(content().string(containsString("Price")));
    }
}