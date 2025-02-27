import java.util.Map;

abstract class Handler {
    protected Handler nextHandler;

    public void setNext(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract String handle(Map<String, Object> request);
}

class StockValidationHandler extends Handler {
    @Override
    public String handle(Map<String, Object> request) {
        Boolean inStock = (Boolean) request.getOrDefault("in_stock", false);
        if (!inStock) {
            return "El producto no está en stock. Solicitud rechazada.";
        }
        System.out.println("Validación de stock exitosa.");
        if (nextHandler != null) {
            return nextHandler.handle(request);
        }
        return null;
    }
}

class PaymentValidationHandler extends Handler {
    @Override
    public String handle(Map<String, Object> request) {
        String paymentMethod = (String) request.get("payment_method");
        if (!"credit_card".equals(paymentMethod) && !"paypal".equals(paymentMethod) && !"bank_transfer".equals(paymentMethod)) {
            return "Método de pago '" + paymentMethod + "' no válido. Solicitud rechazada.";
        }
        System.out.println("Validación de pago (" + paymentMethod + ") exitosa.");
        if (nextHandler != null) {
            return nextHandler.handle(request);
        }
        return null;
    }
}

class ShippingValidationHandler extends Handler {
    @Override
    public String handle(Map<String, Object> request) {
        String shippingAddress = (String) request.get("shipping_address");
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            return "Dirección de envío no válida. Solicitud rechazada.";
        }
        System.out.println("Validación de dirección de envío exitosa.");
        if (nextHandler != null) {
            return nextHandler.handle(request);
        }
        return null;
    }
}

class DiscountValidationHandler extends Handler {
    @Override
    public String handle(Map<String, Object> request) {
        Boolean isPremiumCustomer = (Boolean) request.getOrDefault("is_premium_customer", false);
        Double totalPrice = (Double) request.get("total_price");
        if (isPremiumCustomer) {
            System.out.println("Descuento aplicado (10%) para cliente premium.");
            totalPrice *= 0.9; 
            request.put("total_price", totalPrice);
        } else {
            System.out.println("Cliente no elegible para descuentos.");
        }
        if (nextHandler != null) {
            return nextHandler.handle(request);
        }
        return null;
    }
}

class ApprovalHandler extends Handler {
    @Override
    public String handle(Map<String, Object> request) {
        System.out.println("Todas las validaciones pasaron. Solicitud aprobada.");
        Double totalPrice = (Double) request.get("total_price");
        return String.format("Solicitud aprobada. Precio final: $%.2f", totalPrice);
    }
}

public class Main {
    public static void main(String[] args) {
        Handler stockHandler = new StockValidationHandler();
        Handler paymentHandler = new PaymentValidationHandler();
        Handler shippingHandler = new ShippingValidationHandler();
        Handler discountHandler = new DiscountValidationHandler();
        Handler approvalHandler = new ApprovalHandler();

        stockHandler.setNext(paymentHandler)
                    .setNext(shippingHandler)
                    .setNext(discountHandler)
                    .setNext(approvalHandler);

        Map<String, Object> request1 = Map.of(
            "in_stock", true,
            "payment_method", "credit_card",
            "shipping_address", "123 Main St",
            "is_premium_customer", true,
            "total_price", 100.0
        );

        Map<String, Object> request2 = Map.of(
            "in_stock", true,
            "payment_method", "debit_card", 
            "shipping_address", "456 Elm St",
            "is_premium_customer", false,
            "total_price", 200.0
        );

        Map<String, Object> request3 = Map.of(
            "in_stock", false, 
            "payment_method", "paypal",
            "shipping_address", "789 Oak St",
            "is_premium_customer", true,
            "total_price", 150.0
        );

        System.out.println("\n=== Procesando Solicitud 1 ===");
        String result1 = stockHandler.handle(request1);
        System.out.println(result1);

        System.out.println("\n=== Procesando Solicitud 2 ===");
        String result2 = stockHandler.handle(request2);
        System.out.println(result2);

        System.out.println("\n=== Procesando Solicitud 3 ===");
        String result3 = stockHandler.handle(request3);
        System.out.println(result3);
    }
}
