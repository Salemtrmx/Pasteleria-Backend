package com.pasteleria.pago;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Service
@Slf4j
public class PagoService {
    
    @Value("${mercadopago.access.token}")
    private String accessToken;
    
    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken(accessToken);
        log.info("Mercado Pago SDK inicializado correctamente");
    }
    
    public PagoResponse procesarPagoTarjeta(PagoRequest request) {
        try {
            PaymentClient client = new PaymentClient();
            
            PaymentCreateRequest paymentCreateRequest = PaymentCreateRequest.builder()
                .transactionAmount(request.getTotal())
                .token(request.getPaymentData().getToken())
                .description("Pedido Pastelería - " + request.getFechaEntrega())
                .installments(request.getPaymentData().getInstallments() != null ? 
                    request.getPaymentData().getInstallments() : 1)
                .paymentMethodId(request.getPaymentData().getPayment_method_id())
                .payer(PaymentPayerRequest.builder()
                    .email(request.getPaymentData().getPayer().getEmail())
                    .build())
                .build();
            
            Payment payment = client.create(paymentCreateRequest);
            
            log.info("Pago procesado: ID={}, Status={}", payment.getId(), payment.getStatus());
            
            return PagoResponse.builder()
                .status(payment.getStatus())
                .statusDetail(payment.getStatusDetail())
                .paymentId(payment.getId())
                .message(getMessageForStatus(payment.getStatus()))
                .build();
                
        } catch (MPApiException e) {
            log.error("Error API Mercado Pago: {}", e.getApiResponse().getContent());
            return PagoResponse.builder()
                .status("error")
                .message("Error al procesar el pago: " + e.getMessage())
                .build();
        } catch (MPException e) {
            log.error("Error Mercado Pago: {}", e.getMessage());
            return PagoResponse.builder()
                .status("error")
                .message("Error de conexión con Mercado Pago")
                .build();
        }
    }
    
    public PagoResponse procesarPagoOxxo(PagoRequest request) {
        try {
            PaymentClient client = new PaymentClient();
            
            PaymentCreateRequest paymentCreateRequest = PaymentCreateRequest.builder()
                .transactionAmount(request.getTotal())
                .description("Pedido Pastelería - " + request.getFechaEntrega())
                .paymentMethodId("oxxo")
                .payer(PaymentPayerRequest.builder()
                    .email(request.getPaymentData().getPayer().getEmail())
                    .build())
                .build();
            
            Payment payment = client.create(paymentCreateRequest);
            
            log.info("Pago OXXO creado: ID={}, Status={}", payment.getId(), payment.getStatus());
            
            String ticketUrl = payment.getTransactionDetails() != null ? 
                payment.getTransactionDetails().getExternalResourceUrl() : null;
            
            return PagoResponse.builder()
                .status(payment.getStatus())
                .statusDetail(payment.getStatusDetail())
                .paymentId(payment.getId())
                .message("Se generó tu ticket de pago OXXO")
                .ticketUrl(ticketUrl)
                .build();
                
        } catch (MPApiException e) {
            log.error("Error API Mercado Pago OXXO: {}", e.getApiResponse().getContent());
            return PagoResponse.builder()
                .status("error")
                .message("Error al generar ticket OXXO: " + e.getMessage())
                .build();
        } catch (MPException e) {
            log.error("Error Mercado Pago OXXO: {}", e.getMessage());
            return PagoResponse.builder()
                .status("error")
                .message("Error de conexión con Mercado Pago")
                .build();
        }
    }
    
    public PagoResponse procesarPagoSpei(PagoRequest request) {
        try {
            PaymentClient client = new PaymentClient();
            
            PaymentCreateRequest paymentCreateRequest = PaymentCreateRequest.builder()
                .transactionAmount(request.getTotal())
                .description("Pedido Pastelería - " + request.getFechaEntrega())
                .paymentMethodId("spei")
                .payer(PaymentPayerRequest.builder()
                    .email(request.getPaymentData().getPayer().getEmail())
                    .build())
                .build();
            
            Payment payment = client.create(paymentCreateRequest);
            
            log.info("Pago SPEI creado: ID={}, Status={}", payment.getId(), payment.getStatus());
            
            String ticketUrl = payment.getTransactionDetails() != null ? 
                payment.getTransactionDetails().getExternalResourceUrl() : null;
            
            return PagoResponse.builder()
                .status(payment.getStatus())
                .statusDetail(payment.getStatusDetail())
                .paymentId(payment.getId())
                .message("Se generaron los datos para transferencia SPEI")
                .ticketUrl(ticketUrl)
                .build();
                
        } catch (MPApiException e) {
            log.error("Error API Mercado Pago SPEI: {}", e.getApiResponse().getContent());
            return PagoResponse.builder()
                .status("error")
                .message("Error al generar datos SPEI: " + e.getMessage())
                .build();
        } catch (MPException e) {
            log.error("Error Mercado Pago SPEI: {}", e.getMessage());
            return PagoResponse.builder()
                .status("error")
                .message("Error de conexión con Mercado Pago")
                .build();
        }
    }
    
    private String getMessageForStatus(String status) {
        return switch (status) {
            case "approved" -> "¡Pago aprobado exitosamente!";
            case "pending" -> "El pago está pendiente de confirmación";
            case "in_process" -> "El pago está siendo procesado";
            case "rejected" -> "El pago fue rechazado";
            default -> "Estado del pago: " + status;
        };
    }
}
