package com.salesmanager.core.business.services.customer.wishlist;

import java.util.List;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.model.customer.wishlist.Wishlist;

public interface WishlistService {
    
    Wishlist addToWishlist(Long customerId, Long productId) throws ServiceException;
    
    void removeFromWishlist(Long customerId, Long productId) throws ServiceException;
    
    List<Wishlist> getCustomerWishlist(Long customerId) throws ServiceException;
    
    boolean isInWishlist(Long customerId, Long productId) throws ServiceException;
}
