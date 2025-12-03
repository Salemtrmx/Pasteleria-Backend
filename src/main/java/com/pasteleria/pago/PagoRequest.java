package com.pasteleria.pago;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PagoRequest {
    private List<ItemPedido> items;
    private BigDecimal total;
    private String fechaEntrega;
    private PaymentData paymentData;
    
    @Data
    public static class ItemPedido {
        private String nombre;
        private Integer cantidad;
        private BigDecimal precio;
        private String tipo;
        private String sabor;
        private String tamanio;
        private String decoracion;
        private String mensaje;
    }
    
    @Data
    public static class PaymentData {
        private String token;
        private String payment_method_id;
        private Integer installments;
        private String issuer_id;
        private Payer payer;
        
        @Data
        public static class Payer {
            private String email;
            private Identification identification;
            
            @Data
            public static class Identification {
                private String type;
                private String number;
            }
        }
    }
}
