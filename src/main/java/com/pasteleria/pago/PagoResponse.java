package com.pasteleria.pago;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PagoResponse {
    private String status;
    private String statusDetail;
    private Long paymentId;
    private String message;
    private String ticketUrl;
}
