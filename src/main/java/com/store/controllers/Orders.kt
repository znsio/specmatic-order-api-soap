package com.store.controllers

import com.store.model.Id
import com.store.model.Order
import com.store.model.OrderStatus
import com.store.services.OrderService
import jakarta.jws.WebMethod
import jakarta.jws.WebParam
import jakarta.jws.WebService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

@WebService(targetNamespace = "http://www.example.com/store")
@Controller
class OrderSoapController {
    @Autowired
    lateinit var orderService: OrderService


    @WebMethod
    fun createOrder(@WebParam(name = "order") order: Order): Id {
        return orderService.createOrder(order)
    }

    @WebMethod
    fun getOrder(@WebParam(name = "orderId") orderId: Int): Order {
        return orderService.getOrder(orderId)
    }

    @WebMethod
    fun updateOrder(@WebParam(name = "order") order: Order) {
        orderService.updateOrder(order)
    }

    @WebMethod
    fun deleteOrder(@WebParam(name = "orderId") orderId: Int): String {
        orderService.deleteOrder(orderId)
        return "Order deleted successfully"
    }

    @WebMethod
    fun searchOrders(
        @WebParam(name = "status") status: OrderStatus?,
        @WebParam(name = "productId") productId: Int
    ): List<Order> {
        return orderService.findOrders(status, productId)
    }
}
