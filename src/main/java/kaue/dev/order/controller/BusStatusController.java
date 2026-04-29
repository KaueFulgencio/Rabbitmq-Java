package kaue.dev.order.controller;

import kaue.dev.order.entity.BusStatusEntity;
import kaue.dev.order.listener.BusStatusCreatedListener;
import kaue.dev.order.listener.dto.BusStatusCreatedEvent;
import kaue.dev.order.service.BusStatusProducer;
import kaue.dev.order.service.BusStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/bus-status")
public class BusStatusController {

    @Autowired
    private BusStatusService busStatusService;

    @Autowired
    private BusStatusProducer busStatusProducer;

    @GetMapping("/listAll")
    public ResponseEntity<List<BusStatusEntity>> listAll() {
        return ResponseEntity.ok(busStatusService.findAll());
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishEvent(@RequestBody BusStatusCreatedEvent event) {
        try {
            busStatusProducer.sendBusStatusCreatedEvent(event);
            return ResponseEntity.ok("Evento publicado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao publicar evento: " + e.getMessage());
        }
    }

    @PostMapping("/publish-with-error")
    public ResponseEntity<String> publishEventWithError(@RequestBody BusStatusCreatedEvent event) {
        try {
            // Ativa modo de erro para simular falha
            BusStatusCreatedListener.SIMULATE_ERROR = true;
            busStatusProducer.sendBusStatusCreatedEvent(event);
            // Desativa após um curto tempo
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    BusStatusCreatedListener.SIMULATE_ERROR = false;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
            return ResponseEntity.ok("Evento publicado com ERRO SIMULADO - observe a DLQ!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("/error-mode")
    public ResponseEntity<String> getErrorMode() {
        return ResponseEntity.ok("Modo erro: " + (BusStatusCreatedListener.SIMULATE_ERROR ? "ATIVO ✅" : "INATIVO ❌"));
    }

    @PostMapping("/error-mode/toggle")
    public ResponseEntity<String> toggleErrorMode() {
        BusStatusCreatedListener.SIMULATE_ERROR = !BusStatusCreatedListener.SIMULATE_ERROR;
        return ResponseEntity.ok("Modo erro alterado para: " + (BusStatusCreatedListener.SIMULATE_ERROR ? "ATIVO ✅" : "INATIVO ❌"));
    }
}
