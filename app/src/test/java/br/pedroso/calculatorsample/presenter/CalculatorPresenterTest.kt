package br.pedroso.calculatorsample.presenter

import br.pedroso.calculatorsample.data.Calculator
import br.pedroso.calculatorsample.domain.Operation
import br.pedroso.calculatorsample.usecases.DoOperation
import br.pedroso.calculatorsample.view.CalculatorView
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CalculatorPresenterTest {

    private lateinit var view: CalculatorView

    private lateinit var doOperation: DoOperation

    private lateinit var presenter: CalculatorPresenter

    companion object {
        private val OPERATION = Operation.Addition
        private const val FAKE_VALUE_TEXT = "2.0f"
        private const val FAKE_FIELD_NAME = "fieldName"
        private const val MOCKED_RETURN = 2f
    }

    @Before
    fun setup() {
        view = mock()

        doOperation = mock()

        presenter = CalculatorPresenter(
                view,
                doOperation
        )
    }

    @Test
    fun `Testing if clickedOnCalculate works properly when DoOperation returns a proper value`() {
        mockDoOperationResultWithValue(MOCKED_RETURN)

        presenter.clickedOnCalculate(OPERATION, FAKE_VALUE_TEXT, FAKE_VALUE_TEXT)

        inOrder(doOperation, view).run {
            verify(view, times(1)).displayLoading()
            verify(view, times(1)).hideLoading()
            verify(view, times(1)).displayResult(MOCKED_RETURN)
        }
    }

    @Test
    fun `Testing if clickedOnCalculate works properly when DoOperation throws a CannotDivideByZeroException`() {
        mockDoOperationResultWithError(Calculator.CannotDivideByZeroException())

        presenter.clickedOnCalculate(OPERATION, FAKE_VALUE_TEXT, FAKE_VALUE_TEXT)

        inOrder(doOperation, view).run {
            verify(view, times(1)).displayLoading()
            verify(view, times(1)).hideLoading()
            verify(view, times(1)).displayCannotDivideByZeroError()
        }
    }

    @Test
    fun `Testing if clickedOnCalculate works properly when DoOperation throws a EmptyFieldException`() {
        mockDoOperationResultWithError(DoOperation.EmptyFieldException(FAKE_FIELD_NAME))

        presenter.clickedOnCalculate(OPERATION, FAKE_VALUE_TEXT, FAKE_VALUE_TEXT)

        inOrder(doOperation, view).run {
            verify(view, times(1)).displayLoading()
            verify(view, times(1)).hideLoading()
            verify(view, times(1)).displayEmptyFieldError(FAKE_FIELD_NAME)
        }
    }


    @Test
    fun `Testing if clickedOnCalculate works properly when DoOperation throws a InvalidFieldException`() {
        mockDoOperationResultWithError(DoOperation.InvalidFieldException(FAKE_FIELD_NAME))

        presenter.clickedOnCalculate(OPERATION, FAKE_VALUE_TEXT, FAKE_VALUE_TEXT)

        inOrder(doOperation, view).run {
            verify(view, times(1)).displayLoading()
            verify(view, times(1)).hideLoading()
            verify(view, times(1)).displayInvalidFieldError(FAKE_FIELD_NAME)
        }
    }

    @Test
    fun `Testing if clickedOnCalculate works properly when DoOperation throws a generic exception`() {
        mockDoOperationResultWithError(Throwable())

        presenter.clickedOnCalculate(OPERATION, FAKE_VALUE_TEXT, FAKE_VALUE_TEXT)

        inOrder(doOperation, view).run {
            verify(view, times(1)).displayLoading()
            verify(view, times(1)).hideLoading()
            verify(view, times(1)).displayGenericError()
        }
    }

    @Test
    fun `Testing if clickedOnCalculate register a proper subscription`() {
        mockDoOperationResultWithValue(MOCKED_RETURN)

        assertEquals(0, presenter.subscriptions.size())

        presenter.clickedOnCalculate(OPERATION, FAKE_VALUE_TEXT, FAKE_VALUE_TEXT)

        assertEquals(1, presenter.subscriptions.size())
    }

    @Test
    fun `Testing if clearSubscriptions clean the subscriptions list`() {
        mockDoOperationResultWithValue(MOCKED_RETURN)

        assertEquals(0, presenter.subscriptions.size())

        presenter.clickedOnCalculate(OPERATION, FAKE_VALUE_TEXT, FAKE_VALUE_TEXT)

        assertEquals(1, presenter.subscriptions.size())

        presenter.clearSubscriptions()

        assertEquals(0, presenter.subscriptions.size())
    }

    private fun mockDoOperationResultWithValue(value: Float) =
            mockDoOperationResult(Single.just(value))

    private fun mockDoOperationResultWithError(error: Throwable) =
            mockDoOperationResult(Single.error(error))

    private fun mockDoOperationResult(resultSingle: Single<Float>) {
        whenever(
                doOperation.execute(
                        any(),
                        any(),
                        any()
                )
        ).thenReturn(
                resultSingle
        )
    }

}