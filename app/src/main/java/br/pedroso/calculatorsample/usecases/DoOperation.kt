package br.pedroso.calculatorsample.usecases

import br.pedroso.calculatorsample.domain.Operation
import io.reactivex.Single

interface DoOperation {
    fun execute(operation: Operation, firstNumberText: String, secondNumberText: String): Single<Float>

    class EmptyFieldException(val fieldName: String) : Throwable()

    class InvalidFieldException(val fieldName: String) : Throwable()
}