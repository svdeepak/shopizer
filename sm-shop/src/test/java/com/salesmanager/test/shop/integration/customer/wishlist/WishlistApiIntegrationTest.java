package com.salesmanager.test.shop.integration.customer.wishlist;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.merchant.MerchantStore;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class WishlistApiIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private MerchantStoreService merchantStoreService;
    
    private Customer customer;
    private Product product;
    private MerchantStore store;
    
    @Before
    public void setUp() throws Exception {
        store = merchantStoreService.getByCode("DEFAULT");
        
        customer = new Customer();
        customer.setEmailAddress("test@wishlist.com");
        customer.setNick("testwishlist");
        customer.setMerchantStore(store);
        customerService.create(customer);
        
        product = new Product();
        product.setSku("TEST-WISHLIST-001");
        product.setMerchantStore(store);
        productService.create(product);
    }
    
    @Test
    @WithMockUser(username = "testwishlist")
    public void testAddToWishlist() throws Exception {
        mockMvc.perform(post("/api/v1/customer/wishlist/product/" + product.getId())
                .header("store", "DEFAULT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId", is(product.getId().intValue())));
    }
    
    @Test
    @WithMockUser(username = "testwishlist")
    public void testAddToWishlist_Idempotent() throws Exception {
        mockMvc.perform(post("/api/v1/customer/wishlist/product/" + product.getId())
                .header("store", "DEFAULT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        
        mockMvc.perform(post("/api/v1/customer/wishlist/product/" + product.getId())
                .header("store", "DEFAULT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
    
    @Test
    @WithMockUser(username = "testwishlist")
    public void testGetWishlist() throws Exception {
        mockMvc.perform(post("/api/v1/customer/wishlist/product/" + product.getId())
                .header("store", "DEFAULT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        
        mockMvc.perform(get("/api/v1/customer/wishlist")
                .header("store", "DEFAULT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].productId", is(product.getId().intValue())));
    }
    
    @Test
    @WithMockUser(username = "testwishlist")
    public void testRemoveFromWishlist() throws Exception {
        mockMvc.perform(post("/api/v1/customer/wishlist/product/" + product.getId())
                .header("store", "DEFAULT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        
        mockMvc.perform(delete("/api/v1/customer/wishlist/product/" + product.getId())
                .header("store", "DEFAULT"))
                .andExpect(status().isNoContent());
        
        mockMvc.perform(get("/api/v1/customer/wishlist")
                .header("store", "DEFAULT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
    
    @Test
    @WithMockUser(username = "testwishlist")
    public void testCheckWishlist() throws Exception {
        mockMvc.perform(get("/api/v1/customer/wishlist/product/" + product.getId() + "/check")
                .header("store", "DEFAULT"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
        
        mockMvc.perform(post("/api/v1/customer/wishlist/product/" + product.getId())
                .header("store", "DEFAULT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        
        mockMvc.perform(get("/api/v1/customer/wishlist/product/" + product.getId() + "/check")
                .header("store", "DEFAULT"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
    
    @Test
    public void testAddToWishlist_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/customer/wishlist/product/" + product.getId())
                .header("store", "DEFAULT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
