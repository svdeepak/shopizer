package com.salesmanager.shop.mapper.customer.wishlist;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.salesmanager.core.model.customer.wishlist.Wishlist;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.shop.model.customer.wishlist.WishlistItem;
import com.salesmanager.core.business.utils.ProductPriceUtils;
import com.salesmanager.core.model.merchant.MerchantStore;

@Component
public class WishlistMapper {
    
    @Autowired
    private ProductPriceUtils priceUtil;
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public WishlistItem toWishlistItem(Wishlist wishlist, MerchantStore store) {
        WishlistItem item = new WishlistItem();
        item.setId(wishlist.getId());
        
        Product product = wishlist.getProduct();
        item.setProductId(product.getId());
        item.setProductSku(product.getSku());
        item.setAvailable(product.isAvailable());
        
        if (product.getProductDescription() != null) {
            item.setProductName(product.getProductDescription().getName());
        }
        
        ProductImage image = product.getProductImage();
        if (image != null) {
            item.setImageUrl("/static/products/" + store.getCode() + "/" + product.getSku() + "/SMALL/" + image.getProductImage());
        }
        
        try {
            item.setPrice(priceUtil.getStoreFormatedAmountWithCurrency(store, priceUtil.getFinalPrice(product).getFinalPrice()));
        } catch (Exception e) {
            item.setPrice("N/A");
        }
        
        item.setCreatedDate(DATE_FORMAT.format(wishlist.getCreatedDate()));
        
        return item;
    }
    
    public List<WishlistItem> toWishlistItems(List<Wishlist> wishlists, MerchantStore store) {
        return wishlists.stream()
            .map(w -> toWishlistItem(w, store))
            .collect(Collectors.toList());
    }
}
