package com.store.model

import jakarta.validation.constraints.NotNull
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "User")
@XmlAccessorType(XmlAccessType.FIELD)
data class User(
    @field:NotNull
    @XmlElement(name = "name", required = true)
    val name: String = ""
)