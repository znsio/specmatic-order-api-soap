package com.store.model

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "Id")
@XmlAccessorType(XmlAccessType.FIELD)
data class Id(
    @XmlElement(name = "id", required = true)
    val id: Int
) {
    constructor() : this(0)
}