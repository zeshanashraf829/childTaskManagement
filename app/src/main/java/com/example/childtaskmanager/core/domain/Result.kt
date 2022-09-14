package com.example.childtaskmanager.core.domain

/**
 * A result class for treating errors
 */
sealed class Result<out Error, out Data> {

    data class Fail<out Error>(val error: Error) : Result<Error, Nothing>()

    data class Ok<out Data>(val data: Data) : Result<Nothing, Data>()

    fun isFailure() = this is Fail<Error>

    fun error() = (this as Fail<Error>).error

    fun isOk() = this is Ok<Data>

    fun data() = (this as Ok<Data>).data
}