package com.pasteleria.pago;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PagoController {
    
    private final PagoService pagoService;
    
    @PostMapping("/procesar")
    public ResponseEntity<PagoResponse> procesarPago(@RequestBody PagoRequest request) {
        log.info("Procesando pago - Total: {}, Método: {}", 
            request.getTotal(), 
            request.getPaymentData().getPayment_method_id());
        
        String paymentMethod = request.getPaymentData().getPayment_method_id();
        
        PagoResponse response;
        
        if ("oxxo".equals(paymentMethod)) {
            response = pagoService.procesarPagoOxxo(request);
        } else if ("spei".equals(paymentMethod)) {
            response = pagoService.procesarPagoSpei(request);
        } else {
            // Pago con tarjeta
            response = pagoService.procesarPagoTarjeta(request);
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(@RequestBody String payload) {
        log.info("Webhook recibido: {}", payload);
        // Aquí se procesarían las notificaciones de Mercado Pago
        // para actualizar el estado de los pedidos
        return ResponseEntity.ok("OK");
    }
}
