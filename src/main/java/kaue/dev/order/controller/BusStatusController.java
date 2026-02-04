package kaue.dev.order.controller;

import kaue.dev.order.entity.BusStatusEntity;
import kaue.dev.order.service.BusStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/bus-status")
public class BusStatusController {

    @Autowired
    private BusStatusService busStatusService;

    @GetMapping("/listAll")
    public ResponseEntity<List<BusStatusEntity>> listAll() {
        return ResponseEntity.ok(busStatusService.findAll());
    }
}
