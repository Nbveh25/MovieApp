package com.example.domain.exception

class ApiException(
    val code: Int,
    override val message: String
) : Exception(message)