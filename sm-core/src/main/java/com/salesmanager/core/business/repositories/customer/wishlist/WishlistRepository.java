package com.salesmanager.core.business.repositories.customer.wishlist;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.salesmanager.core.model.customer.wishlist.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    
    @Query("SELECT w FROM Wishlist w WHERE w.customer.id = :customerId ORDER BY w.createdDate DESC")
    List<Wishlist> findByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM Wishlist w WHERE w.customer.id = :customerId AND w.product.id = :productId")
    boolean existsByCustomerIdAndProductId(@Param("customerId") Long customerId, @Param("productId") Long productId);
    
    @Query("SELECT w FROM Wishlist w WHERE w.customer.id = :customerId AND w.product.id = :productId")
    Wishlist findByCustomerIdAndProductId(@Param("customerId") Long customerId, @Param("productId") Long productId);
    
    void deleteByCustomerIdAndProductId(Long customerId, Long productId);
}
