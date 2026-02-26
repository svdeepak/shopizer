package com.salesmanager.core.business.services.customer.wishlist;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.customer.CustomerRepository;
import com.salesmanager.core.business.repositories.customer.wishlist.WishlistRepository;
import com.salesmanager.core.business.repositories.catalog.product.ProductRepository;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.customer.wishlist.Wishlist;
import com.salesmanager.core.model.catalog.product.Product;

@Service("wishlistService")
public class WishlistServiceImpl implements WishlistService {
    
    @Autowired
    private WishlistRepository wishlistRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Override
    @Transactional
    public Wishlist addToWishlist(Long customerId, Long productId) throws ServiceException {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new ServiceException("Customer not found: " + customerId));
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ServiceException("Product not found: " + productId));
        
        if (wishlistRepository.existsByCustomerIdAndProductId(customerId, productId)) {
            return wishlistRepository.findByCustomerIdAndProductId(customerId, productId);
        }
        
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomer(customer);
        wishlist.setProduct(product);
        
        return wishlistRepository.save(wishlist);
    }
    
    @Override
    @Transactional
    public void removeFromWishlist(Long customerId, Long productId) throws ServiceException {
        if (!customerRepository.existsById(customerId)) {
            throw new ServiceException("Customer not found: " + customerId);
        }
        
        wishlistRepository.deleteByCustomerIdAndProductId(customerId, productId);
    }
    
    @Override
    public List<Wishlist> getCustomerWishlist(Long customerId) throws ServiceException {
        if (!customerRepository.existsById(customerId)) {
            throw new ServiceException("Customer not found: " + customerId);
        }
        
        return wishlistRepository.findByCustomerId(customerId);
    }
    
    @Override
    public boolean isInWishlist(Long customerId, Long productId) throws ServiceException {
        return wishlistRepository.existsByCustomerIdAndProductId(customerId, productId);
    }
}
