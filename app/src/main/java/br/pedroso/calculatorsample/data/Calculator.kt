package br.pedroso.calculatorsample.data

class Calculator {

    fun addition(firstNumber: Float, secondNumber: Float) = firstNumber + secondNumber

    fun subtraction(firstNumber: Float, secondNumber: Float) = firstNumber - secondNumber

    fun multiplication(firstNumber: Float, secondNumber: Float) = firstNumber * secondNumber

    fun division(firstNumber: Float, secondNumber: Float): Float {
        if (secondNumber == 0f) {
            throw CannotDivideByZeroException()
        }

        return firstNumber / secondNumber
    }


    class CannotDivideByZeroException : Throwable()
}