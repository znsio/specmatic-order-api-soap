package com.store.model

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.util.concurrent.atomic.AtomicInteger

@XmlRootElement(name = "Product")
@XmlAccessorType(XmlAccessType.FIELD)
data class Product(
    @field:NotNull
    @XmlElement(name = "name", required = true)
    val name: String = "",

    @field:NotNull
    @XmlElement(name = "type", required = true)
    val type: String = "gadget",

    @field:NotNull
    @field:Positive
    @XmlElement(name = "inventory", required = true)
    val inventory: Int = 0,

    @XmlElement(name = "id", required = true)
    var id: Int = 0
) {
    init {
        if (id == 0) {
            this.id = idGenerator.getAndIncrement()
        }
    }

    companion object {
        val idGenerator: AtomicInteger = AtomicInteger()
    }
}