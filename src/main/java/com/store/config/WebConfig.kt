package com.store.config

import com.store.controllers.SoapRequestInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
open class WebConfig : WebMvcConfigurer {

    @Autowired
    lateinit var soapRequestInterceptor: SoapRequestInterceptor

     override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(soapRequestInterceptor)
            .addPathPatterns("/**")
    }
}
