package br.pedroso.calculatorsample.usecases

import br.pedroso.calculatorsample.data.Calculator
import br.pedroso.calculatorsample.domain.Operation
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.concurrent.TimeUnit

class DoOperationImpl(
        private val calculator: Calculator,
        private val scheduler: Scheduler = Schedulers.trampoline(),
        private val delayInSeconds: Long = TEST_DELAY
) : DoOperation {
    companion object {
        private const val TEST_DELAY = 0L
        const val FIRST_NUMBER_FIELD_NAME = "first"
        const val SECOND_NUMBER_FIELD_NAME = "second"
    }

    override fun execute(operation: Operation, firstNumberText: String, secondNumberText: String): Single<Float> {
        val operationSingle = Single.create<Float> { emitter ->

            val firstNumber = try {
                parseNumberText(firstNumberText, FIRST_NUMBER_FIELD_NAME)
            } catch (exception: Exception) {
                emitter.onError(exception)
                return@create
            }

            val secondNumber = try {
                parseNumberText(secondNumberText, SECOND_NUMBER_FIELD_NAME)
            } catch (exception: Exception) {
                emitter.onError(exception)
                return@create
            }

            with(calculator) {
                val result = try {
                    when (operation) {
                        Operation.Addition -> addition(firstNumber, secondNumber)
                        Operation.Subtraction -> subtraction(firstNumber, secondNumber)
                        Operation.Division -> division(firstNumber, secondNumber)
                        Operation.Multiplication -> multiplication(firstNumber, secondNumber)
                    }
                } catch (exception: Exception) {
                    emitter.onError(exception)
                    return@create
                }

                emitter.onSuccess(result)
            }
        }

        return operationSingle
                .delay(delayInSeconds, TimeUnit.SECONDS)
                .subscribeOn(scheduler)

    }

    private fun parseNumberText(numberText: String, fieldName: String): Float {
        if (numberText.isEmpty() || numberText.isBlank()) {
            throw DoOperation.EmptyFieldException(fieldName)
        }

        return try {
            numberText.toFloat()
        } catch (exception: Exception) {
            throw DoOperation.InvalidFieldException(fieldName)
        }
    }
}