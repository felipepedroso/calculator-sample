package br.pedroso.calculatorsample.view

import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import br.pedroso.calculatorsample.R
import br.pedroso.calculatorsample.data.Calculator
import br.pedroso.calculatorsample.di.ServiceLocator
import br.pedroso.calculatorsample.domain.Operation
import br.pedroso.calculatorsample.usecases.DoOperation
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController

@RunWith(RobolectricTestRunner::class)
class CalculatorActivityTest {

    private lateinit var activityController: ActivityController<CalculatorActivity>

    private lateinit var calculatorActivity: CalculatorActivity

    private lateinit var spinnerOperations: Spinner

    private lateinit var buttonCalculate: Button

    private lateinit var textInputEditTextFirstNumber: TextInputEditText

    private lateinit var textInputEditTextSecondNumber: TextInputEditText

    private lateinit var textViewResult: TextView

    private lateinit var progressBarLoading: ProgressBar

    private lateinit var textInputLayoutFirstNumber: TextInputLayout

    private lateinit var textInputLayoutSecondNumber: TextInputLayout

    private lateinit var doOperation: DoOperation

    companion object {
        private const val VALID_TEXT = "2.0"
        private val VALID_TEXT_VALUE = VALID_TEXT.toFloat()
        private const val EMPTY_TEXT = ""
        private const val FAKE_FIELD_NAME = "fieldName"
    }

    @Before
    fun setup() {
        mockUseCase()
        setupActivity()
        bindViews()
    }

    private fun mockUseCase() {
        doOperation = mock()
        ServiceLocator.registerService(doOperation)
    }

    private fun setupActivity() {
        activityController = Robolectric.buildActivity(CalculatorActivity::class.java).create().start()
        calculatorActivity = activityController.get()
    }

    private fun bindViews() {
        spinnerOperations = calculatorActivity.findViewById(R.id.spinnerOperations)
        buttonCalculate = calculatorActivity.findViewById(R.id.buttonCalculate)
        textInputLayoutFirstNumber = calculatorActivity.findViewById(R.id.textInputLayoutFirstNumber)
        textInputLayoutSecondNumber = calculatorActivity.findViewById(R.id.textInputLayoutSecondNumber)
        textInputEditTextFirstNumber = calculatorActivity.findViewById(R.id.textInputEditTextFirstNumber)
        textInputEditTextSecondNumber = calculatorActivity.findViewById(R.id.textInputEditTextSecondNumber)
        textViewResult = calculatorActivity.findViewById(R.id.textViewResult)
        progressBarLoading = calculatorActivity.findViewById(R.id.progressBarLoading)
    }

    @After
    fun tearDown() {
        ServiceLocator.clearRegisteredServices()
    }

    @Test
    fun `Test if spinnerOperations is loading the operations properly`() {
        val availableOperations = Operation.values()

        assertEquals(availableOperations.size, spinnerOperations.count)

        availableOperations.forEachIndexed { index, operation ->
            val spinnerOperationItem = spinnerOperations.getItemAtPosition(index) as? Operation
            assertEquals(operation, spinnerOperationItem)
        }
    }

    @Test
    fun `Assert that UI is initialized with proper state`() {
        assertEquals(View.VISIBLE, textViewResult.visibility)

        assertEquals(EMPTY_TEXT, textViewResult.text)

        assertEquals(EMPTY_TEXT, textInputEditTextFirstNumber.text.toString())

        assertEquals(true, textInputLayoutFirstNumber.isEnabled)

        assertEquals(EMPTY_TEXT, textInputEditTextSecondNumber.text.toString())

        assertEquals(true, textInputLayoutSecondNumber.isEnabled)

        assertEquals(View.INVISIBLE, progressBarLoading.visibility)

        assertEquals(spinnerOperations.selectedItem, Operation.Addition)

        assertEquals(true, spinnerOperations.isEnabled)

        assertEquals(true, buttonCalculate.isEnabled)
    }

    @Test
    fun `Test if the result is displayed properly when a operation succeeds`() {
        mockDoOperationResultWithValue(VALID_TEXT_VALUE)

        textInputEditTextFirstNumber.setText(VALID_TEXT, TextView.BufferType.EDITABLE)

        spinnerOperations.setSelection(0)

        textInputEditTextSecondNumber.setText(VALID_TEXT, TextView.BufferType.EDITABLE)

        buttonCalculate.performClick()

        val expectedResultText = calculatorActivity.getString(R.string.result_string, VALID_TEXT_VALUE)

        assertEquals(expectedResultText, textViewResult.text)
    }

    @Test
    fun `Test if ui displays a proper message  when DoOperation throws a CannotDivideByZeroException`() {
        mockDoOperationResultWithError(Calculator.CannotDivideByZeroException())

        textInputEditTextFirstNumber.setText(VALID_TEXT, TextView.BufferType.EDITABLE)

        spinnerOperations.setSelection(0)

        textInputEditTextSecondNumber.setText(VALID_TEXT, TextView.BufferType.EDITABLE)

        buttonCalculate.performClick()

        val expectedResultText = calculatorActivity.getString(R.string.error_cannot_divide_by_zero)

        assertEquals(expectedResultText, textViewResult.text)
    }

    @Test
    fun `Test if ui displays a proper message  when DoOperation throws a EmptyFieldException`() {
        mockDoOperationResultWithError(DoOperation.EmptyFieldException(FAKE_FIELD_NAME))

        textInputEditTextFirstNumber.setText(VALID_TEXT, TextView.BufferType.EDITABLE)

        spinnerOperations.setSelection(0)

        textInputEditTextSecondNumber.setText(VALID_TEXT, TextView.BufferType.EDITABLE)

        buttonCalculate.performClick()

        val expectedResultText = calculatorActivity.getString(R.string.error_empty_field, FAKE_FIELD_NAME)

        assertEquals(expectedResultText, textViewResult.text)
    }

    @Test
    fun `Test if ui displays a proper message  when DoOperation throws a InvalidFieldException`() {
        mockDoOperationResultWithError(DoOperation.InvalidFieldException(FAKE_FIELD_NAME))

        textInputEditTextFirstNumber.setText(VALID_TEXT, TextView.BufferType.EDITABLE)

        spinnerOperations.setSelection(0)

        textInputEditTextSecondNumber.setText(VALID_TEXT, TextView.BufferType.EDITABLE)

        buttonCalculate.performClick()

        val expectedResultText = calculatorActivity.getString(R.string.error_invalid_field, FAKE_FIELD_NAME)

        assertEquals(expectedResultText, textViewResult.text)
    }

    @Test
    fun `Test if ui displays a proper message  when DoOperation throws a generic error`() {
        mockDoOperationResultWithError(Throwable())

        textInputEditTextFirstNumber.setText(VALID_TEXT, TextView.BufferType.EDITABLE)

        spinnerOperations.setSelection(0)

        textInputEditTextSecondNumber.setText(VALID_TEXT, TextView.BufferType.EDITABLE)

        buttonCalculate.performClick()

        val expectedResultText = calculatorActivity.getString(R.string.error_generic)

        assertEquals(expectedResultText, textViewResult.text)
    }

    @Test
    fun `Testing if presenter will clean its subscriptions during Activity onDestroy`() {
        val presenter = calculatorActivity.presenter

        activityController.destroy()

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
                resultSingle.observeOn(Schedulers.trampoline())
        )
    }
}