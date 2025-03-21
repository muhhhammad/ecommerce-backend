package com.practiceProject.ecommece.service;

import com.practiceProject.ecommece.exception.OrderException;
import com.practiceProject.ecommece.entity.*;
import com.practiceProject.ecommece.repository.AddressRepository;
import com.practiceProject.ecommece.repository.OrderItemRepository;
import com.practiceProject.ecommece.repository.UserRepository;
import com.practiceProject.ecommece.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImplementation implements OrderService{

    private CartService cartService;
    private AddressRepository addressRepository;
    private UserRepository userRepository;
    private OrderItemService orderItemService;
    private OrderItemRepository orderItemRepository;
    private OrderRepository orderRepository;


    @Autowired
    public OrderServiceImplementation(CartService cartService,
                                      AddressRepository addressRepository,
                                      UserRepository userRepository,
                                      OrderItemService orderItemService,
                                      OrderItemRepository orderItemRepository,
                                      OrderRepository orderRepository) {

        this.cartService = cartService;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.orderItemService = orderItemService;
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(User user, Address shippingAddress) {

        shippingAddress.setUser(user); // Associate the shipping address with the user
        Address address = addressRepository.save(shippingAddress); // Save shipping address
        user.getAddress().add(address); // Add the address to the user's list
        userRepository.save(user); // Save updated user data

        Cart cart = cartService.findUserCart(user.getUserId()); // Retrieve user's cart
        List<OrderItems> orderItem = new ArrayList<>(); // Initialize order items list

        for(CartItem item : cart.getCartItems()){ // Iterate over cart items

            OrderItems orderItems = new OrderItems(); // Create order item

            orderItems.setPrice(item.getPrice()); // Set price
            orderItems.setProduct(item.getProduct()); // Set product
            orderItems.setQuantity(item.getQuantity()); // Set quantity
            orderItems.setSize(item.getSize()); // Set size
            orderItems.setUserId(item.getUserId()); // Set user ID
            orderItems.setDiscountedPrice(item.getDiscountedPrice()); // Set discounted price

            OrderItems createdOrderItems = orderItemRepository.save(orderItems); // Save order item
            orderItems.add(createdOrderItems); // Add saved order item to list

        }

        Order createdOrder = new Order(); // Create new order
        createdOrder.setUser(user); // Associate user with order
        createdOrder.setOrderItems(orderItem); // Set order items
        createdOrder.setTotalPrice(cart.getTotalPrice()); // Set total price
        createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice()); // Set total discounted price
        createdOrder.setDiscount(cart.getDiscount()); // Set discount
        createdOrder.setTotalItem(cart.getTotalItem()); // Set total item count
        createdOrder.setShippingAddress(address); // Set shipping address
        createdOrder.setOrderDate(LocalDateTime.now()); // Set order date
        createdOrder.setOrderStatus("PENDING"); // Set initial order status
        createdOrder.getPaymentDetails().setPaymentStatus("PENDING"); // Set payment status
        createdOrder.setCreatedAt(LocalDateTime.now()); // Set creation timestamp

        Order savedOrder = orderRepository.save(createdOrder); // Save order

        for (OrderItems item : orderItem){ // Update each order item with the saved order

            item.setOrder(savedOrder);
            orderItemRepository.save(item);

        }

        return savedOrder; // Return saved order
    }

    @Override
    public Order findOrderById(Long orderId) throws OrderException {

        Optional<Order> opt = orderRepository.findById(orderId); // Find order by ID
        if(opt.isPresent()){ // If order exists, return it

            return opt.get();

        }

        throw new OrderException("Order Does Not Exist with ID: " + orderId); // Throw exception if order not found
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {

        List<Order> order = orderRepository.getUserOrder(userId); // Get user's order history
        return order; // Return order list
    }

    @Override
    public Order placedOrder(Long orderId) throws OrderException {

        Order order = findOrderById(orderId); // Find order by ID
        order.setOrderStatus("PLACED"); // Update status to PLACED
        order.getPaymentDetails().setPaymentStatus("COMPLETED"); // Mark payment as completed
        return order; // Return updated order
    }

    @Override
    public Order confirmedOrder(Long orderId) throws OrderException {

        Order order = findOrderById(orderId); // Find order by ID
        order.setOrderStatus("CONFIRMED"); // Update status to CONFIRMED

        return orderRepository.save(order); // Save and return updated order
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {

        Order order = findOrderById(orderId); // Find order by ID
        order.setOrderStatus("SHIPPED"); // Update status to SHIPPED

        return orderRepository.save(order); // Save and return updated order
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {

        Order order = findOrderById(orderId); // Find order by ID
        order.setOrderStatus("DELIVERED"); // Update status to DELIVERED

        return orderRepository.save(order); // Save and return updated order
    }

    @Override
    public Order canceledOrder(Long orderId) throws OrderException {

        Order order = findOrderById(orderId); // Find order by ID
        order.setOrderStatus("CANCELED"); // Update status to CANCELED

        return orderRepository.save(order); // Save and return updated order
    }

    @Override
    public List<Order> getAllOrder() {

        return orderRepository.findAll(); // Retrieve and return all orders
    }

    @Override
    public void deletedOrder(Long orderId) throws OrderException {

        Order order = findOrderById(orderId); // Find order by ID
        orderRepository.delete(order); // Delete order
    }

}
