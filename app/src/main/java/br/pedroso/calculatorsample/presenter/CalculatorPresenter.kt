package br.pedroso.calculatorsample.presenter

import android.support.annotation.VisibleForTesting
import br.pedroso.calculatorsample.data.Calculator
import br.pedroso.calculatorsample.domain.Operation
import br.pedroso.calculatorsample.usecases.DoOperation
import br.pedroso.calculatorsample.view.CalculatorView
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CalculatorPresenter(
        private val view: CalculatorView,
        private val doOperationUsecase: DoOperation,
        private val uiScheduler: Scheduler = Schedulers.trampoline()
) {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val subscriptions by lazy { CompositeDisposable() }

    fun clickedOnCalculate(selectedOperation: Operation, firstNumberText: String, secondNumberText: String) {
        val subscription = doOperationUsecase
                .execute(selectedOperation, firstNumberText, secondNumberText)
                .observeOn(uiScheduler)
                .doOnSubscribe { view.displayLoading() }
                .doOnSuccess { view.hideLoading() }
                .doOnError { view.hideLoading() }
                .subscribe(::handleCalculationSuccess, ::handleCalculationError)

        registerSubscription(subscription)
    }

    private fun registerSubscription(subscription: Disposable) {
        subscriptions.add(subscription)
    }

    fun clearSubscriptions() = subscriptions.clear()

    private fun handleCalculationSuccess(result: Float) = view.displayResult(result)

    private fun handleCalculationError(error: Throwable) {
        when (error) {
            is Calculator.CannotDivideByZeroException -> view.displayCannotDivideByZeroError()
            is DoOperation.EmptyFieldException -> view.displayEmptyFieldError(error.fieldName)
            is DoOperation.InvalidFieldException -> view.displayInvalidFieldError(error.fieldName)
            else -> view.displayGenericError()
        }
    }
}