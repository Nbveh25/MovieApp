package com.example.homework.data.exception

class ApiException(
    val code: Int,
    override val message: String
) : Exception(message) 