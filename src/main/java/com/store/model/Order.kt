package com.store.model

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.util.concurrent.atomic.AtomicInteger

@XmlRootElement(name = "Order")
@XmlAccessorType(XmlAccessType.FIELD)
class Order(
    @field:Positive
    @XmlElement(name = "productid", required = true)
    val productid: Int = 0,

    @field:Positive
    @XmlElement(name = "count", required = true)
    val count: Int = 0,

    @field:NotNull
    @XmlElement(name = "status", required = true)
    var status: OrderStatus = OrderStatus.pending,

    @XmlElement(name = "id", required = true)
    val id: Int = idGenerator.getAndIncrement()
) {
    companion object {
        val idGenerator: AtomicInteger = AtomicInteger()
    }
}

enum class OrderStatus {
    pending,
    fulfilled,
    cancelled
}