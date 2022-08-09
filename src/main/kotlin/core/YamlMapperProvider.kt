package core

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

private val MAPPER = YAMLMapper.builder()
    .propertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE)
    .addModule(KotlinModule.Builder().build())
    .build()

class YamlMapperProvider {

    companion object {

        fun mapper(): YAMLMapper = MAPPER
    }
}
