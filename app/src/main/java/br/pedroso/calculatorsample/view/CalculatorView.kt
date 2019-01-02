package br.pedroso.calculatorsample.view

interface CalculatorView {
    fun displayResult(result: Float)
    fun hideLoading()
    fun displayLoading()
    fun displayCannotDivideByZeroError()
    fun displayGenericError()
    fun displayEmptyFieldError(fieldName: String)
    fun displayInvalidFieldError(fieldName: String)
}