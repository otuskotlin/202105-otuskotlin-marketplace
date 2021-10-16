package ru.otus.otuskotlin.marketplace.backend.repo.dynamo

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue

data class DynamoAttrString(
    override val attrName: String,
    override val attrValue: AttributeValue.S?,
    override val value: String?
): DynamoAttrBase<String, AttributeValue.S>(attrName, attrValue, value) {
    constructor(name: String, value: AttributeValue): this(
        attrName = name,
        attrValue = value as? AttributeValue.S,
        value = (value as? AttributeValue.S)?.value,
    )
    constructor(name: String, value: String): this(
        attrName = name,
        attrValue = AttributeValue.S(value),
        value = value,
    )
}
