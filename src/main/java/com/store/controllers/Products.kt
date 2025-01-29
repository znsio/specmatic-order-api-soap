package com.store.controllers

import com.store.exceptions.NotFoundException
import com.store.exceptions.ValidationException
import com.store.model.*
import com.store.services.ProductService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import jakarta.xml.soap.MessageFactory
import jakarta.xml.soap.SOAPMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayOutputStream

private val typesOfProducts = listOf("gadget", "book", "food", "other")

@RestController
open class Products {

    @Autowired
    lateinit var productService: ProductService

    @PostMapping("/products/{id}")
    @SoapRequest(Product::class)
    fun update(
        @PathVariable id: Int,
        request: HttpServletRequest,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<String> {
        val unmarshalledProduct = request.getAttribute("unmarshalledObject") as Product

        val productId = productService.addProduct(unmarshalledProduct.also {
            if(unmarshalledProduct.type !in typesOfProducts)
                throw ValidationException("type must be one of ${typesOfProducts.joinToString(", ")}")
        })

        productService.updateProduct(unmarshalledProduct)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/products/{id}")
    @SoapRequest(Product::class)
    fun get(@PathVariable("id") id: Int): Product {
        try {
            return productService.getProduct(id)
        } catch (e: NoSuchElementException) {
            throw NotFoundException(e.message!!)
        }
    }

    @PostMapping("/products")
    @SoapRequest(Product::class)
    fun create(request: HttpServletRequest, @AuthenticationPrincipal user: User): ResponseEntity<ByteArray> {
        val unmarshalledProduct = request.getAttribute("unmarshalledObject") as Product
        val productId = productService.addProduct(unmarshalledProduct.also {
            if(unmarshalledProduct.type !in typesOfProducts)
                throw ValidationException("type must be one of ${typesOfProducts.joinToString(", ")}")
        })
        val soapResponse = SoapResponse(id = productId)
        val soapMessage = createSoapResponse(soapResponse)
        val byteArrayOutputStream = ByteArrayOutputStream()
        soapMessage.writeTo(byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_XML)
            .body(byteArray)
    }

    @DeleteMapping("/products/{id}")
    fun delete(@PathVariable("id") id: Int, @AuthenticationPrincipal user: User): ResponseEntity<String> {
        productService.deleteProduct(id)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/products")
    fun search(
        @RequestParam(name = "name", required = false) name: String?,
        @RequestParam(name = "type", required = false) type: String?,
        @RequestParam(name = "status", required = false) status: String?
    ): ResponseEntity<List<Product>> {
        // An exception thrown by some internal bug...
        if (name == "unknown")
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        val products = productService.findProducts(name, type, status)
        return ResponseEntity(products, HttpStatus.OK)
    }

    @PutMapping("/products/{id}/image", consumes = ["multipart/form-data"])
    fun uploadImage(@PathVariable("id") id: Int, @RequestPart("image") image: MultipartFile): ResponseEntity<Map<String, Any>> {
        productService.addImage(id, image.originalFilename, image.bytes)
        val response = mapOf("message" to "Product image updated successfully", "productId" to id)
        return ResponseEntity(response, HttpStatus.OK)
    }

    private fun createSoapResponse(soapResponse: SoapResponse): SOAPMessage {
        val soapMessage = MessageFactory.newInstance().createMessage()
        val soapPart = soapMessage.soapPart
        val soapEnvelope = soapPart.envelope

        val soapBody = soapEnvelope.body
        soapBody.addChildElement("id").value = soapResponse.id.id.toString()
        soapMessage.saveChanges()

        return soapMessage
    }
}
