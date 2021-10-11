package ru.otus.otuskotlin.marketplace.backend.repo.dynamo

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue

abstract class DynamoAttrBase<T, V: AttributeValue>(
    open val attrName: String,
    open val attrValue: V?,
    open val value: T?
)
