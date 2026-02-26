package com.salesmanager.shop.store.api.v1.customer;

import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.customer.wishlist.WishlistService;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.customer.wishlist.Wishlist;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.shop.mapper.customer.wishlist.WishlistMapper;
import com.salesmanager.shop.model.customer.wishlist.WishlistItem;
import com.salesmanager.shop.store.api.exception.ResourceNotFoundException;
import com.salesmanager.shop.store.api.exception.ServiceRuntimeException;
import com.salesmanager.shop.store.api.exception.UnauthorizedException;
import io.swagger.annotations.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/auth/customer/wishlist")
@Api(tags = {"Customer Wishlist"})
@CrossOrigin(allowedHeaders = "*", exposedHeaders = "*")
public class WishlistApi {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(WishlistApi.class);
    
    @Autowired
    private WishlistService wishlistService;
    
    @Inject
    private CustomerService customerService;
    
    @Autowired
    private WishlistMapper wishlistMapper;
    
    @GetMapping
    @ApiOperation(value = "Get customer wishlist", response = WishlistItem.class, responseContainer = "List")
    @ApiImplicitParams({@ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT")})
    public ResponseEntity<List<WishlistItem>> getWishlist(@ApiIgnore MerchantStore store, @ApiIgnore HttpServletRequest request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                throw new UnauthorizedException("Not authenticated");
            }
            String username = auth.getName();
            Customer customer = customerService.getByNick(username, store.getCode());
            if (customer == null) {
                throw new ResourceNotFoundException("Customer not found");
            }
            List<Wishlist> wishlists = wishlistService.getCustomerWishlist(customer.getId());
            return ResponseEntity.ok(wishlistMapper.toWishlistItems(wishlists, store));
        } catch (Exception e) {
            LOGGER.error("Error getting wishlist", e);
            throw new ServiceRuntimeException(e);
        }
    }
    
    @PostMapping("/product/{productId}")
    @ApiOperation(value = "Add product to wishlist")
    @ApiImplicitParams({@ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT")})
    public ResponseEntity<WishlistItem> addToWishlist(
            @PathVariable Long productId,
            @ApiIgnore MerchantStore store,
            @ApiIgnore HttpServletRequest request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) throw new UnauthorizedException("Not authenticated");
            String username = auth.getName();
            Customer customer = customerService.getByNick(username, store.getCode());
            if (customer == null) {
                throw new ResourceNotFoundException("Customer not found");
            }
            Wishlist wishlist = wishlistService.addToWishlist(customer.getId(), productId);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(wishlistMapper.toWishlistItem(wishlist, store));
        } catch (Exception e) {
            LOGGER.error("Error adding to wishlist", e);
            throw new ServiceRuntimeException(e);
        }
    }
    
    @DeleteMapping("/product/{productId}")
    @ApiOperation(value = "Remove product from wishlist")
    @ApiImplicitParams({@ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT")})
    public ResponseEntity<Void> removeFromWishlist(
            @PathVariable Long productId,
            @ApiIgnore MerchantStore store,
            @ApiIgnore HttpServletRequest request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) throw new UnauthorizedException("Not authenticated");
            String username = auth.getName();
            Customer customer = customerService.getByNick(username, store.getCode());
            if (customer == null) {
                throw new ResourceNotFoundException("Customer not found");
            }
            wishlistService.removeFromWishlist(customer.getId(), productId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            LOGGER.error("Error removing from wishlist", e);
            throw new ServiceRuntimeException(e);
        }
    }
    
    @GetMapping("/product/{productId}/check")
    @ApiOperation(value = "Check if product is in wishlist")
    @ApiImplicitParams({@ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT")})
    public ResponseEntity<Boolean> checkWishlist(
            @PathVariable Long productId,
            @ApiIgnore MerchantStore store,
            @ApiIgnore HttpServletRequest request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) throw new UnauthorizedException("Not authenticated");
            String username = auth.getName();
            Customer customer = customerService.getByNick(username, store.getCode());
            if (customer == null) {
                throw new ResourceNotFoundException("Customer not found");
            }
            boolean exists = wishlistService.isInWishlist(customer.getId(), productId);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            LOGGER.error("Error checking wishlist", e);
            throw new ServiceRuntimeException(e);
        }
    }
}
