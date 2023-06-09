package spring.rest.shop.springrestshop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import spring.rest.shop.springrestshop.aspect.SecurityContext;
import spring.rest.shop.springrestshop.dto.order.OrderDTO;
import spring.rest.shop.springrestshop.entity.*;
import spring.rest.shop.springrestshop.exception.CartEmptyException;
import spring.rest.shop.springrestshop.repository.CartProductRepository;
import spring.rest.shop.springrestshop.repository.CartRepository;
import spring.rest.shop.springrestshop.repository.OrderRepository;
import spring.rest.shop.springrestshop.repository.UserRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final UserService userService;
    private final ProductService productService;
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserRepository userRepository;


    public void createOrder() throws CartEmptyException {
        User currentUser = SecurityContext.getCurrentUser();
        Cart cart = currentUser.getCart();
        if(cart.getCartProducts().isEmpty()){
            throw new CartEmptyException("Cart is empty");
        }
        Order order = new Order();
        order.setCustomer(currentUser);
        List<CartProduct> cartProductList = new ArrayList<>(cart.getCartProducts());
        order.setProductList(cartProductList);
        order.setPrice(cart.getCostPurchase());

        for (CartProduct cartProduct : cartProductList) {
            cartProduct.setCart(null);
            cartProductRepository.save(cartProduct);
        }

        orderRepository.save(order);
        cart.setCartProducts(new ArrayList<>());
        cart.setCostPurchase(0);
        cartService.saveCart(cart);
    }

    public Order getOrderDetails(long orderId){
        User currentUser = SecurityContext.getCurrentUser();
        Order order = orderRepository.findById(orderId);
        if(order.getCustomer() == currentUser || currentUser.getRoles().contains(Role.ROLE_ADMIN)){
            return order;
        }
        return new Order();
    }


    public List<Order> getOrdersForCurrentUser() {
        User currentUser = SecurityContext.getCurrentUser();
       return orderRepository.findByCustomer_Id(currentUser.getId());
    }
}

