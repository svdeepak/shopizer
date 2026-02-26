package com.salesmanager.core.business.services.customer.wishlist;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.customer.CustomerRepository;
import com.salesmanager.core.business.repositories.customer.wishlist.WishlistRepository;
import com.salesmanager.core.business.repositories.catalog.product.ProductRepository;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.customer.wishlist.Wishlist;
import com.salesmanager.core.model.catalog.product.Product;

@RunWith(MockitoJUnitRunner.class)
public class WishlistServiceImplTest {
    
    @Mock
    private WishlistRepository wishlistRepository;
    
    @Mock
    private CustomerRepository customerRepository;
    
    @Mock
    private ProductRepository productRepository;
    
    @InjectMocks
    private WishlistServiceImpl wishlistService;
    
    private Customer customer;
    private Product product;
    private Wishlist wishlist;
    
    @Before
    public void setUp() {
        customer = new Customer();
        customer.setId(1L);
        
        product = new Product();
        product.setId(100L);
        
        wishlist = new Wishlist();
        wishlist.setId(1L);
        wishlist.setCustomer(customer);
        wishlist.setProduct(product);
    }
    
    @Test
    public void testAddToWishlist_Success() throws ServiceException {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));
        when(wishlistRepository.existsByCustomerIdAndProductId(1L, 100L)).thenReturn(false);
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(wishlist);
        
        Wishlist result = wishlistService.addToWishlist(1L, 100L);
        
        assertNotNull(result);
        verify(wishlistRepository).save(any(Wishlist.class));
    }
    
    @Test
    public void testAddToWishlist_Idempotent() throws ServiceException {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));
        when(wishlistRepository.existsByCustomerIdAndProductId(1L, 100L)).thenReturn(true);
        when(wishlistRepository.findByCustomerIdAndProductId(1L, 100L)).thenReturn(wishlist);
        
        Wishlist result = wishlistService.addToWishlist(1L, 100L);
        
        assertNotNull(result);
        verify(wishlistRepository, never()).save(any(Wishlist.class));
    }
    
    @Test(expected = ServiceException.class)
    public void testAddToWishlist_CustomerNotFound() throws ServiceException {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        
        wishlistService.addToWishlist(1L, 100L);
    }
    
    @Test(expected = ServiceException.class)
    public void testAddToWishlist_ProductNotFound() throws ServiceException {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(100L)).thenReturn(Optional.empty());
        
        wishlistService.addToWishlist(1L, 100L);
    }
    
    @Test
    public void testRemoveFromWishlist_Success() throws ServiceException {
        when(customerRepository.existsById(1L)).thenReturn(true);
        
        wishlistService.removeFromWishlist(1L, 100L);
        
        verify(wishlistRepository).deleteByCustomerIdAndProductId(1L, 100L);
    }
    
    @Test(expected = ServiceException.class)
    public void testRemoveFromWishlist_CustomerNotFound() throws ServiceException {
        when(customerRepository.existsById(1L)).thenReturn(false);
        
        wishlistService.removeFromWishlist(1L, 100L);
    }
    
    @Test
    public void testGetCustomerWishlist_Success() throws ServiceException {
        List<Wishlist> wishlists = Arrays.asList(wishlist);
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(wishlistRepository.findByCustomerId(1L)).thenReturn(wishlists);
        
        List<Wishlist> result = wishlistService.getCustomerWishlist(1L);
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test(expected = ServiceException.class)
    public void testGetCustomerWishlist_CustomerNotFound() throws ServiceException {
        when(customerRepository.existsById(1L)).thenReturn(false);
        
        wishlistService.getCustomerWishlist(1L);
    }
    
    @Test
    public void testIsInWishlist_True() throws ServiceException {
        when(wishlistRepository.existsByCustomerIdAndProductId(1L, 100L)).thenReturn(true);
        
        boolean result = wishlistService.isInWishlist(1L, 100L);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsInWishlist_False() throws ServiceException {
        when(wishlistRepository.existsByCustomerIdAndProductId(1L, 100L)).thenReturn(false);
        
        boolean result = wishlistService.isInWishlist(1L, 100L);
        
        assertFalse(result);
    }
}
