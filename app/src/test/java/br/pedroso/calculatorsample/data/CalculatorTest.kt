package br.pedroso.calculatorsample.data

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CalculatorTest {

    private lateinit var calculator: Calculator

    @Before
    fun setup() {
        calculator = Calculator()
    }

    @Test
    fun `Testing if addition works properly with positive numbers`() {
        val firstNumber = 2f

        val secondNumber = 2f

        val expectedResult = firstNumber + secondNumber

        val calculatorResult = calculator.addition(firstNumber, secondNumber)

        assertEquals(expectedResult, calculatorResult)
    }

    @Test
    fun `Testing if addition works properly with negative numbers`() {
        val firstNumber = -2f

        val secondNumber = -2f

        val expectedResult = firstNumber + secondNumber

        val calculatorResult = calculator.addition(firstNumber, secondNumber)

        assertEquals(expectedResult, calculatorResult)
    }

    @Test
    fun `Testing if addition works properly when the first number is zero`() {
        val firstNumber = 0f

        val secondNumber = 2f

        val calculatorResult = calculator.addition(firstNumber, secondNumber)

        assertEquals(secondNumber, calculatorResult)
    }

    @Test
    fun `Testing if addition works properly when the second number is zero`() {
        val firstNumber = 2f

        val secondNumber = 0f

        val calculatorResult = calculator.addition(firstNumber, secondNumber)

        assertEquals(firstNumber, calculatorResult)
    }

    @Test
    fun `Testing if addition works properly when both numbers are zero`() {
        val firstNumber = 0f

        val secondNumber = 0f

        val calculatorResult = calculator.addition(firstNumber, secondNumber)

        assertEquals(0f, calculatorResult)
    }

    @Test
    fun `Testing if subtraction works properly with positive numbers`() {
        val firstNumber = 2f

        val secondNumber = 2f

        val expectedResult = firstNumber - secondNumber

        val calculatorResult = calculator.subtraction(firstNumber, secondNumber)

        assertEquals(expectedResult, calculatorResult)
    }

    @Test
    fun `Testing if subtraction works properly with negative numbers`() {
        val firstNumber = -2f

        val secondNumber = -2f

        val expectedResult = firstNumber - secondNumber

        val calculatorResult = calculator.subtraction(firstNumber, secondNumber)

        assertEquals(expectedResult, calculatorResult)
    }

    @Test
    fun `Testing if subtraction works properly when the first number is zero`() {
        val firstNumber = 0f

        val secondNumber = 2f

        val calculatorResult = calculator.subtraction(firstNumber, secondNumber)

        assertEquals(-secondNumber, calculatorResult)
    }

    @Test
    fun `Testing if subtraction works properly when the second number is zero`() {
        val firstNumber = 2f

        val secondNumber = 0f

        val calculatorResult = calculator.addition(firstNumber, secondNumber)

        assertEquals(firstNumber, calculatorResult)
    }

    @Test
    fun `Testing if subtraction works properly when both numbers are zero`() {
        val firstNumber = 0f

        val secondNumber = 0f

        val calculatorResult = calculator.subtraction(firstNumber, secondNumber)

        assertEquals(0f, calculatorResult)
    }

    @Test
    fun `Testing if multiplication works properly with positive numbers`() {
        val firstNumber = 2f

        val secondNumber = 2f

        val expectedResult = firstNumber * secondNumber

        val calculatorResult = calculator.multiplication(firstNumber, secondNumber)

        assertEquals(expectedResult, calculatorResult)
    }

    @Test
    fun `Testing if multiplication works properly with negative numbers`() {
        val firstNumber = -2f

        val secondNumber = -2f

        val expectedResult = firstNumber * secondNumber

        val calculatorResult = calculator.multiplication(firstNumber, secondNumber)

        assertEquals(expectedResult, calculatorResult)
    }

    @Test
    fun `Testing if multiplication works properly when the first number is zero`() {
        val firstNumber = 0f

        val secondNumber = 2f

        val calculatorResult = calculator.multiplication(firstNumber, secondNumber)

        assertEquals(0f, calculatorResult)
    }

    @Test
    fun `Testing if multiplication works properly when the second number is zero`() {
        val firstNumber = 2f

        val secondNumber = 0f

        val calculatorResult = calculator.multiplication(firstNumber, secondNumber)

        assertEquals(0f, calculatorResult)
    }

    @Test
    fun `Testing if multiplication works properly when both numbers are zero`() {
        val firstNumber = 0f

        val secondNumber = 0f

        val calculatorResult = calculator.multiplication(firstNumber, secondNumber)

        assertEquals(0f, calculatorResult)
    }

    @Test
    fun `Testing if division works properly with positive numbers`() {
        val firstNumber = 2f

        val secondNumber = 2f

        val expectedResult = firstNumber / secondNumber

        val calculatorResult = calculator.division(firstNumber, secondNumber)

        assertEquals(expectedResult, calculatorResult)
    }

    @Test
    fun `Testing if division works properly with negative numbers`() {
        val firstNumber = -2f

        val secondNumber = -2f

        val expectedResult = firstNumber / secondNumber

        val calculatorResult = calculator.division(firstNumber, secondNumber)

        assertEquals(expectedResult, calculatorResult)
    }

    @Test
    fun `Testing if division works properly when the first number is zero`() {
        val firstNumber = 0f

        val secondNumber = 2f

        val calculatorResult = calculator.division(firstNumber, secondNumber)

        assertEquals(0f, calculatorResult)
    }

    @Test(expected = Calculator.CannotDivideByZeroException::class)
    fun `Testing if division works properly when the second number is zero`() {
        val firstNumber = 2f

        val secondNumber = 0f

        calculator.division(firstNumber, secondNumber)
    }

    @Test(expected = Calculator.CannotDivideByZeroException::class)
    fun `Testing if division works properly when both numbers are zero`() {
        val firstNumber = 0f

        val secondNumber = 0f

        calculator.division(firstNumber, secondNumber)
    }

}