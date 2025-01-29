package com.store.config

import com.store.controllers.OrderSoapController
import jakarta.xml.ws.Endpoint
import org.apache.cxf.jaxws.EndpointImpl
import org.apache.cxf.Bus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class CxfConfig(
    private val bus: Bus,
    private val orderSoapController: OrderSoapController
) {

    @Bean
    open fun orderEndpoint(): Endpoint {
        val endpoint = EndpointImpl(bus, orderSoapController)
        endpoint.publish("/OrderService")
        return endpoint
    }
}