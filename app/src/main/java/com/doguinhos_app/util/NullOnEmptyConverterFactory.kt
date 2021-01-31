package com.doguinhos_app.util

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Created by Filipi Andrade Rocha on 06/11/2019
 */
val nullOnEmptyConverterFactory = object : Converter.Factory() {
    fun converterFactory() = this
    override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any>(converterFactory(), type, annotations)
        return Converter<ResponseBody, Any> {
            if (it.contentLength() != 0L) {
                nextResponseBodyConverter.convert(it)
            } else {
                null
            }
        }
    }
}