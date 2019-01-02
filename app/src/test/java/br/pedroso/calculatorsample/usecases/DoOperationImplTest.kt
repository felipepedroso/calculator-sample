package br.pedroso.calculatorsample.usecases

import br.pedroso.calculatorsample.data.Calculator
import br.pedroso.calculatorsample.domain.Operation
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test

class DoOperationImplTest {

    private lateinit var calculator: Calculator

    private lateinit var doOperation: DoOperationImpl

    companion object {
        private const val MOCKED_RESULT = 3f
        private val GENERIC_OPERATION = Operation.Addition
        private const val VALID_VALUE_TEXT = "2.0"
        private val VALID_VALUE = VALID_VALUE_TEXT.toFloat()
        private const val EMPTY_VALUE_TEXT = ""
        private const val BLANK_VALUE_TEXT = "      "
        private const val INVALID_VALUE_TEXT = "abc"
        private val FAKE_ERROR = RuntimeException()
    }

    @Before
    fun setup() {
        calculator = mock()
        doOperation = DoOperationImpl(calculator)
    }

    @Test
    fun `Testing if execute stream emits an EmptyFieldException when the first value is empty`() {
        val testObserver = doOperation
                .execute(GENERIC_OPERATION, EMPTY_VALUE_TEXT, VALID_VALUE_TEXT)
                .test()

        testObserver
                .assertNotComplete()
                .assertError { error ->
                    error is DoOperation.EmptyFieldException &&
                            error.fieldName == DoOperationImpl.FIRST_NUMBER_FIELD_NAME
                }
    }

    @Test
    fun `Testing if execute stream emits an EmptyFieldException when the first value is blank`() {
        val testObserver = doOperation
                .execute(GENERIC_OPERATION, BLANK_VALUE_TEXT, VALID_VALUE_TEXT)
                .test()

        testObserver
                .assertNotComplete()
                .assertError { error ->
                    error is DoOperation.EmptyFieldException &&
                            error.fieldName == DoOperationImpl.FIRST_NUMBER_FIELD_NAME
                }
    }

    @Test
    fun `Testing if execute stream emits an InvalidFieldException when the first value is invalid`() {
        val testObserver = doOperation
                .execute(GENERIC_OPERATION, INVALID_VALUE_TEXT, VALID_VALUE_TEXT)
                .test()

        testObserver
                .assertNotComplete()
                .assertError { error ->
                    error is DoOperation.InvalidFieldException &&
                            error.fieldName == DoOperationImpl.FIRST_NUMBER_FIELD_NAME
                }
    }

    @Test
    fun `Testing if execute stream emits an EmptyFieldException when the second value is empty`() {
        val testObserver = doOperation
                .execute(GENERIC_OPERATION, VALID_VALUE_TEXT, EMPTY_VALUE_TEXT)
                .test()

        testObserver
                .assertNotComplete()
                .assertError { error ->
                    error is DoOperation.EmptyFieldException &&
                            error.fieldName == DoOperationImpl.SECOND_NUMBER_FIELD_NAME
                }
    }

    @Test
    fun `Testing if execute stream emits an EmptyFieldException when the second value is blank`() {
        val testObserver = doOperation
                .execute(GENERIC_OPERATION, VALID_VALUE_TEXT, BLANK_VALUE_TEXT)
                .test()

        testObserver
                .assertNotComplete()
                .assertError { error ->
                    error is DoOperation.EmptyFieldException &&
                            error.fieldName == DoOperationImpl.SECOND_NUMBER_FIELD_NAME
                }
    }

    @Test
    fun `Testing if execute stream emits an InvalidFieldException when the second value is invalid`() {
        val testObserver = doOperation
                .execute(GENERIC_OPERATION, VALID_VALUE_TEXT, INVALID_VALUE_TEXT)
                .test()

        testObserver
                .assertNotComplete()
                .assertError { error ->
                    error is DoOperation.InvalidFieldException &&
                            error.fieldName == DoOperationImpl.SECOND_NUMBER_FIELD_NAME
                }
    }

    @Test
    fun `Testing if execute stream emits the proper value when the operation is addition`() {
        mockAllOperationsMethodsWithResult(MOCKED_RESULT)

        val testObserver = doOperation
                .execute(Operation.Addition, VALID_VALUE_TEXT, VALID_VALUE_TEXT)
                .test()
                .await()

        testObserver
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(MOCKED_RESULT)

        verify(calculator, times(1)).addition(VALID_VALUE, VALID_VALUE)
        verify(calculator, never()).subtraction(VALID_VALUE, VALID_VALUE)
        verify(calculator, never()).multiplication(VALID_VALUE, VALID_VALUE)
        verify(calculator, never()).division(VALID_VALUE, VALID_VALUE)
    }

    @Test
    fun `Testing if execute stream emits the proper value when the operation is subtraction`() {
        mockAllOperationsMethodsWithResult(MOCKED_RESULT)

        val testObserver = doOperation
                .execute(Operation.Subtraction, VALID_VALUE_TEXT, VALID_VALUE_TEXT)
                .test()
                .await()

        testObserver
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(MOCKED_RESULT)

        verify(calculator, never()).addition(VALID_VALUE, VALID_VALUE)
        verify(calculator, times(1)).subtraction(VALID_VALUE, VALID_VALUE)
        verify(calculator, never()).multiplication(VALID_VALUE, VALID_VALUE)
        verify(calculator, never()).division(VALID_VALUE, VALID_VALUE)
    }

    @Test
    fun `Testing if execute stream emits the proper value when the operation is multiplication`() {
        mockAllOperationsMethodsWithResult(MOCKED_RESULT)

        val testObserver = doOperation
                .execute(Operation.Multiplication, VALID_VALUE_TEXT, VALID_VALUE_TEXT)
                .test()
                .await()

        testObserver
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(MOCKED_RESULT)

        verify(calculator, never()).addition(VALID_VALUE, VALID_VALUE)
        verify(calculator, never()).subtraction(VALID_VALUE, VALID_VALUE)
        verify(calculator, times(1)).multiplication(VALID_VALUE, VALID_VALUE)
        verify(calculator, never()).division(VALID_VALUE, VALID_VALUE)
    }

    @Test
    fun `Testing if execute stream emits the proper value when the operation is division`() {
        mockAllOperationsMethodsWithResult(MOCKED_RESULT)

        val testObserver = doOperation
                .execute(Operation.Division, VALID_VALUE_TEXT, VALID_VALUE_TEXT)
                .test()
                .await()

        testObserver
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(MOCKED_RESULT)

        verify(calculator, never()).addition(VALID_VALUE, VALID_VALUE)
        verify(calculator, never()).subtraction(VALID_VALUE, VALID_VALUE)
        verify(calculator, never()).multiplication(VALID_VALUE, VALID_VALUE)
        verify(calculator, times(1)).division(VALID_VALUE, VALID_VALUE)
    }

    @Test
    fun `Testing if execute stream forwards an error during the addition operation`() {
        mockAllOperationsMethodsWithError(FAKE_ERROR)

        val testObserver = doOperation
                .execute(Operation.Addition, VALID_VALUE_TEXT, VALID_VALUE_TEXT)
                .test()
                .await()

        testObserver
                .assertNotComplete()
                .assertError(FAKE_ERROR)

        verify(calculator, times(1)).addition(VALID_VALUE, VALID_VALUE)
        verify(calculator, never()).subtraction(VALID_VALUE, VALID_VALUE)
        verify(calculator, never()).multiplication(VALID_VALUE, VALID_VALUE)
        verify(calculator, never()).division(VALID_VALUE, VALID_VALUE)
    }

    @Test
    fun `Testing if execute stream forwards an error during the subtraction operation`() {
        mockAllOperationsMethodsWithError(FAKE_ERROR)

        val testObserver = doOperation
                .execute(Operation.Subtraction, VALID_VALUE_TEXT, VALID_VALUE_TEXT)
                .test()
                .await()

        testObserver
                .assertNotComplete()
                .assertError(FAKE_ERROR)

        verify(calculator, never()).addition(VALID_VALUE, VALID_VALUE)
        verify(calculator, times(1)).subtraction(VALID_VALUE, VALID_VALUE)
        verify(calculator, never()).multiplication(VALID_VALUE, VALID_VALUE)
        verify(calculator, never()).division(VALID_VALUE, VALID_VALUE)
    }

    @Test
    fun `Testing if execute stream forwards an error during the multiplication operation`() {
        mockAllOperationsMethodsWithError(FAKE_ERROR)

        val testObserver = doOperation
                .execute(Operation.Multiplication, VALID_VALUE_TEXT, VALID_VALUE_TEXT)
                .test()
                .await()

        testObserver
                .assertNotComplete()
                .assertError(FAKE_ERROR)

        verify(calculator, never()).addition(VALID_VALUE, VALID_VALUE)
        verify(calculator, never()).subtraction(VALID_VALUE, VALID_VALUE)
        verify(calculator, times(1)).multiplication(VALID_VALUE, VALID_VALUE)
        verify(calculator, never()).division(VALID_VALUE, VALID_VALUE)
    }

    @Test
    fun `Testing if execute stream forwards an error during the division operation`() {
        mockAllOperationsMethodsWithError(FAKE_ERROR)

        val testObserver = doOperation
                .execute(Operation.Division, VALID_VALUE_TEXT, VALID_VALUE_TEXT)
                .test()
                .await()

        testObserver
                .assertNotComplete()
                .assertError(FAKE_ERROR)

        verify(calculator, never()).addition(VALID_VALUE, VALID_VALUE)
        verify(calculator, never()).subtraction(VALID_VALUE, VALID_VALUE)
        verify(calculator, never()).multiplication(VALID_VALUE, VALID_VALUE)
        verify(calculator, times(1)).division(VALID_VALUE, VALID_VALUE)
    }

    private fun mockAllOperationsMethodsWithError(error: Throwable) {
        whenever(
                calculator.addition(
                        any(),
                        any()
                )
        ).thenThrow(
                error
        )

        whenever(
                calculator.subtraction(
                        any(),
                        any()
                )
        ).thenThrow(
                error
        )

        whenever(
                calculator.multiplication(
                        any(),
                        any()
                )
        ).thenThrow(
                error
        )

        whenever(
                calculator.division(
                        any(),
                        any()
                )
        ).thenThrow(
                error
        )
    }


    private fun mockAllOperationsMethodsWithResult(result: Float) {
        whenever(
                calculator.addition(
                        any(),
                        any()
                )
        ).thenReturn(
                result
        )

        whenever(
                calculator.subtraction(
                        any(),
                        any()
                )
        ).thenReturn(
                result
        )

        whenever(
                calculator.multiplication(
                        any(),
                        any()
                )
        ).thenReturn(
                result
        )

        whenever(
                calculator.division(
                        any(),
                        any()
                )
        ).thenReturn(
                result
        )
    }


}