package kaue.dev.order.controller;

import kaue.dev.order.entity.OrderEntity;
import kaue.dev.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/listAll")
    public ResponseEntity<List<OrderEntity>> listAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
