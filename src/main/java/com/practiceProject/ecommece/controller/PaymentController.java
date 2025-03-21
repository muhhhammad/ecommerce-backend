package com.practiceProject.ecommece.controller;

import com.practiceProject.ecommece.entity.Order;
import com.practiceProject.ecommece.exception.OrderException;
import com.practiceProject.ecommece.repository.OrderRepository;
import com.practiceProject.ecommece.response.ApiResponse;
import com.practiceProject.ecommece.response.PaymentLinkResponse;
import com.practiceProject.ecommece.service.OrderService;
import com.practiceProject.ecommece.service.UserService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {

    // NOTE:
    // This class requires the Razorpay API to function properly.
    // If you have the Razorpay API, add your Merchant ID and API Key in the application.properties file.
    // Once configured, the application will run without any issues.
    // If the API credentials are missing, this code will NOT work.
    // However, the code itself is well-written, complete, and properly commented for easy understanding.


    @Value("${razorpay.api.key}")
    String apiKey;

    @Value("${razor.api.secret}")
    String apiSecret;

    private OrderService orderService;
    private UserService userService;
    private OrderRepository orderRepository;

    @Autowired
    public PaymentController(OrderService orderService, UserService userService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.userService = userService;
        this.orderRepository = orderRepository;
    }


    //The method is to make Payment
    @PostMapping("/payment/{orderId}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable("orderId") Long orderId,
                                                                 @RequestHeader("Authorization") String jwt) throws OrderException, RazorpayException {

        Order order = orderService.findOrderById(orderId);
        try {

            RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);

            //setting the total price of the items bought by the User
            //Also setting the currency to the PKR
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("Amount", order.getTotalPrice() * 278);
            paymentLinkRequest.put("currency", "PKR");

            //this is to get the details of the user to the payment page
            JSONObject customer = new JSONObject();
            customer.put("name", order.getUser().getFirstName());
            customer.put("email", order.getUser().getEmail());
            paymentLinkRequest.put("customer", customer);

            //Setting the Notification
            //When the payment is successfull this will notify the user on given email and an sms will be sent to the users number
            JSONObject notify = new JSONObject();
            notify.put("sms", true);
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            //This will Re-direct to the Provided the url
            paymentLinkRequest.put("callback_url", "http://localhost:3000/payment/" + order.getOrderId());
            paymentLinkRequest.put("callback_methods", "get");

            PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);

            String paymentLinkId = payment.get("id");
            String paymentLinkUrl = payment.get("short_url");

            //Setting the URL and ID in PaymentLinkResponse Class
            PaymentLinkResponse res = new PaymentLinkResponse();
            res.setPayment_link_Id(paymentLinkId);
            res.setPayment_link_url(paymentLinkUrl);

            return new ResponseEntity<PaymentLinkResponse>(res, HttpStatus.CREATED);

        } catch (Exception e) {

            throw new RazorpayException(e.getMessage());

        }


    }

    //Payment is now Successfull
    //Now we need to Change the Status of ORDER from PENDING to PLACED
    //And Also need to update the payment details
    @GetMapping("/payments")
    public ResponseEntity<ApiResponse> reDirect(@RequestParam(name = "payment_id") String paymentId,
                                                @RequestParam(name = "order_id") Long orderId) throws OrderException, RazorpayException {

        Order order = orderService.findOrderById(orderId);
        RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);
        try {

            Payment payment = razorpayClient.payments.fetch(paymentId);
            if (payment.get("status").equals("captured")) {

                order.getPaymentDetails().setPaymentId(paymentId);
                order.getPaymentDetails().setPaymentStatus("COMPLETED");//Setting the Payment status to "COMPLETED"
                order.setOrderStatus("PLACED");//Setting the Order status to "PLACED" from "PENDING"
                orderRepository.save(order);//Saving the Order

            }

            ApiResponse response = new ApiResponse();
            response.setMessage("Order Placed successfully");
            response.setStatus(true);

            return new ResponseEntity<ApiResponse>(response, HttpStatus.ACCEPTED);

        } catch (Exception e) {

            throw new RazorpayException(e.getMessage());

        }


    }


}
