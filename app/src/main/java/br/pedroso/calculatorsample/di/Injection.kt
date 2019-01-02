package br.pedroso.calculatorsample.di

import br.pedroso.calculatorsample.data.Calculator
import br.pedroso.calculatorsample.di.ServiceLocator.getOrRegisterService
import br.pedroso.calculatorsample.presenter.CalculatorPresenter
import br.pedroso.calculatorsample.usecases.DoOperation
import br.pedroso.calculatorsample.usecases.DoOperationImpl
import br.pedroso.calculatorsample.view.CalculatorView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun provideCalculatorPresenter(view: CalculatorView) =
        getOrRegisterService(CalculatorPresenter::class.java) {
            CalculatorPresenter(
                    uiScheduler = provideUiScheduler(),
                    view = view,
                    doOperationUsecase = provideDoOperationUsecase()
            )
        }

private fun provideDoOperationUsecase() = getOrRegisterService(DoOperation::class.java) {
    DoOperationImpl(
            scheduler = provideWorkerScheduler(),
            calculator = provideCalculator(),
            delayInSeconds = 1L
    )
}

private fun provideCalculator() = getOrRegisterService(Calculator::class.java) {
    Calculator()
}

const val UI_SCHEDULER_TAG = "UI_SCHEDULER_TAG"
const val WORKER_SCHEDULER_TAG = "UI_SCHEDULER_TAG"

private fun provideUiScheduler() = getOrRegisterService(UI_SCHEDULER_TAG) {
    AndroidSchedulers.mainThread()
}

private fun provideWorkerScheduler() = getOrRegisterService(WORKER_SCHEDULER_TAG) {
    Schedulers.io()
}
