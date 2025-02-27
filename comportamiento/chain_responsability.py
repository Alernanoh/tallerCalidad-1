from abc import ABC, abstractmethod

class Handler(ABC):
    def __init__(self, next_handler=None):
        self._next_handler = next_handler

    def set_next(self, next_handler):
        self._next_handler = next_handler
        return next_handler

    @abstractmethod
    def handle(self, request):
        if self._next_handler:
            return self._next_handler.handle(request)
        return None

class StockValidationHandler(Handler):
    def handle(self, request):
        if not request.get("in_stock", False):
            return "El producto no está en stock. Solicitud rechazada."
        print("Validación de stock exitosa.")
        return super().handle(request)

class PaymentValidationHandler(Handler):
    def handle(self, request):
        payment_method = request.get("payment_method")
        if payment_method not in ["credit_card", "paypal", "bank_transfer"]:
            return f"Método de pago '{payment_method}' no válido. Solicitud rechazada."
        print(f"Validación de pago ({payment_method}) exitosa.")
        return super().handle(request)

class ShippingValidationHandler(Handler):
    def handle(self, request):
        shipping_address = request.get("shipping_address")
        if not shipping_address or len(shipping_address.strip()) == 0:
            return "Dirección de envío no válida. Solicitud rechazada."
        print("Validación de dirección de envío exitosa.")
        return super().handle(request)

class DiscountValidationHandler(Handler):
    def handle(self, request):
        if request.get("is_premium_customer", False):
            print("Descuento aplicado (10%) para cliente premium.")
            request["total_price"] *= 0.9  # Aplica un 10% de descuento
        else:
            print("Cliente no elegible para descuentos.")
        return super().handle(request)

class ApprovalHandler(Handler):
    def handle(self, request):
        print("Todas las validaciones pasaron. Solicitud aprobada.")
        return f"Solicitud aprobada. Precio final: ${request.get('total_price', 0):.2f}"

stock_handler = StockValidationHandler()
payment_handler = PaymentValidationHandler()
shipping_handler = ShippingValidationHandler()
discount_handler = DiscountValidationHandler()
approval_handler = ApprovalHandler()

stock_handler.set_next(payment_handler).set_next(shipping_handler).set_next(discount_handler).set_next(approval_handler)

request_1 = {
    "in_stock": True,
    "payment_method": "credit_card",
    "shipping_address": "123 Main St",
    "is_premium_customer": True,
    "total_price": 100.0
}

request_2 = {
    "in_stock": True,
    "payment_method": "debit_card",  # Método de pago no válido
    "shipping_address": "456 Elm St",
    "is_premium_customer": False,
    "total_price": 200.0
}

request_3 = {
    "in_stock": False,  # Producto no en stock
    "payment_method": "paypal",
    "shipping_address": "789 Oak St",
    "is_premium_customer": True,
    "total_price": 150.0
}

print("\n=== Procesando Solicitud 1 ===")
result_1 = stock_handler.handle(request_1)
print(result_1)

print("\n=== Procesando Solicitud 2 ===")
result_2 = stock_handler.handle(request_2)
print(result_2)

print("\n=== Procesando Solicitud 3 ===")
result_3 = stock_handler.handle(request_3)
print(result_3)
