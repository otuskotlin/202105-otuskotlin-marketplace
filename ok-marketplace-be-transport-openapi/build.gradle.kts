plugins {
    kotlin("jvm")
    id("org.openapi.generator")
}

/**
 * Настраиваем генерацию здесь
 */
openApiGenerate {
    val openapiGroup = "${rootProject.group}.openapi"
    generatorName.set("kotlin") // Это и есть активный генератор
    packageName.set(openapiGroup)
    apiPackage.set("$openapiGroup.api")
    modelPackage.set("$openapiGroup.models")
    invokerPackage.set("$openapiGroup.invoker")
    inputSpec.set("$rootDir/specs/marketplace.api-spec.yaml")

    /**
     * Здесь указываем, что нам нужны только модели, все остальное не нужно
     */
    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }

    /**
     * Настройка дополнительных параметров из документации по генератору
     * https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/kotlin.md
     */
    configOptions.set(mapOf(
        "dateLibrary" to "string",
        "enumPropertyNaming" to "UPPERCASE",
        "serializationLibrary" to "jackson",
        "collectionType" to "list"
    ))

    /**
     * Так генерируется KMP версия
     */
//    library.set("multiplatform")
}

/**
 * Здесь подключаем сгенерированные исходники к списку директорий компилятора, чтоб он их видел при компиляции
 */
sourceSets {
    main {
        java.srcDir("$buildDir/generate-resources/main/src/main/kotlin")
    }
}

dependencies {
    val jacksonVersion: String by project


    implementation(kotlin("stdlib"))

    /**
     * Зависимости ниже мы забрали из сгенерированного build.gradle. Они нужны для компиляции подпроекта
     */
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

}

tasks {
    /**
     * Устанавливаем зависимость компиляции от генерации исходников. Компиляция начнется только после генерации
     */
    compileKotlin {
        dependsOn(openApiGenerate)
    }
}
