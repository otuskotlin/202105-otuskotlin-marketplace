package ru.otus.otuskotlin.marketplace.backend.repo.dynamo

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue

data class DynamoAttrString(
    val attrName: String,
    val attrValue: AttributeValue.S?,
    val value: String?
) {
    constructor(name: String, value: AttributeValue): this(
        attrName = name,
        attrValue = value as? AttributeValue.S,
        value = getFlatValue(value),
    )
    constructor(name: String, value: String): this(
        attrName = name,
        attrValue = getAttributeValue(value),
        value = value,
    )

    companion object {
        fun getFlatValue(attrValue: AttributeValue) =
            (attrValue as? AttributeValue.S)?.value

        fun getAttributeValue(value: String) =
            AttributeValue.S(value)
    }
}
