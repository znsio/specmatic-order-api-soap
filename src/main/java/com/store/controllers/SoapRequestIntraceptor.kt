package com.store.controllers

import com.store.model.SoapRequest
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.Unmarshaller
import jakarta.xml.soap.MessageFactory
import jakarta.xml.soap.Node
import jakarta.xml.soap.SOAPMessage
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor


@Component
class SoapRequestInterceptor : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod) {
            val requestType = handler.method.getAnnotation(SoapRequest::class.java)?.value?.java
                ?: throw IllegalArgumentException("Missing @SoapRequest annotation on controller method")
            val extractedObject = unmarshallRequest(request, requestType)
            request.setAttribute("unmarshalledObject", extractedObject)
        }
        return true
    }

    private fun <T> unmarshallRequest(request: HttpServletRequest, clazz: Class<T>): T {
        val message: SOAPMessage = MessageFactory.newInstance().createMessage(
            null,
            request.inputStream
        )
        val soapBody = message.soapBody
        val firstElement = getFirstElement(soapBody) ?: throw IllegalArgumentException("Invalid SOAP structure")
        val unmarshaller: Unmarshaller = JAXBContext.newInstance(clazz).createUnmarshaller()
        return unmarshaller.unmarshal(firstElement) as T
    }

    private fun getFirstElement(node: Node): org.w3c.dom.Element? {
        var child = node.firstChild
        while (child != null) {
            if (child.nodeType == Node.ELEMENT_NODE) {
                return child as org.w3c.dom.Element
            }
            child = child.nextSibling
        }
        return null
    }
}
