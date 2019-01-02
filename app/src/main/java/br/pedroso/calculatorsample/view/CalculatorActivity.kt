package br.pedroso.calculatorsample.view

import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.annotation.VisibleForTesting
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import br.pedroso.calculatorsample.R
import br.pedroso.calculatorsample.di.provideCalculatorPresenter
import br.pedroso.calculatorsample.domain.Operation
import kotlinx.android.synthetic.main.activity_main.*

class CalculatorActivity : AppCompatActivity(), CalculatorView {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val presenter by lazy { provideCalculatorPresenter(this) }

    private val components by lazy {
        arrayListOf(
                textInputLayoutFirstNumber,
                textInputLayoutSecondNumber,
                spinnerOperations,
                buttonCalculate
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setContentView(R.layout.activity_main)

        spinnerOperations.adapter =
                ArrayAdapter<Operation>(this, android.R.layout.simple_spinner_item, Operation.values())

        buttonCalculate.setOnClickListener {
            clickedOnCalculateButton()
        }
    }

    private fun clickedOnCalculateButton() {
        val operation = spinnerOperations.selectedItem as Operation

        val firstNumberText = textInputEditTextFirstNumber.text.toString()

        val secondNumberText = textInputEditTextSecondNumber.text.toString()

        presenter.clickedOnCalculate(operation, firstNumberText, secondNumberText)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.clearSubscriptions()
    }

    override fun displayResult(result: Float) {
        setResultColor(R.color.success_color)
        textViewResult.text = getString(R.string.result_string, result)
    }

    override fun hideLoading() {
        progressBarLoading.visibility = View.INVISIBLE
        textViewResult.visibility = View.VISIBLE
        enableComponents()
    }

    override fun displayLoading() {
        textViewResult.visibility = View.GONE
        progressBarLoading.visibility = View.VISIBLE
        disableComponents()
    }

    override fun displayCannotDivideByZeroError() =
            displayErrorMessage(R.string.error_cannot_divide_by_zero)

    override fun displayEmptyFieldError(fieldName: String) {
        val errorMessage = getString(R.string.error_empty_field, fieldName)
        displayErrorMessage(errorMessage)
    }

    private fun displayErrorMessage(@StringRes errorMessageRes: Int) {
        val errorMessage = getString(errorMessageRes)
        displayErrorMessage(errorMessage)
    }

    private fun displayErrorMessage(errorMessage: String) {
        setResultColor(R.color.error_color)
        textViewResult.text = errorMessage
    }

    private fun setResultColor(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        textViewResult.setTextColor(color)
    }

    override fun displayInvalidFieldError(fieldName: String) {
        val errorMessage = getString(R.string.error_invalid_field, fieldName)
        displayErrorMessage(errorMessage)
    }

    override fun displayGenericError() =
            displayErrorMessage(R.string.error_generic)

    private fun disableComponents() =
            setComponentsEnabledState(false)

    private fun enableComponents() =
            setComponentsEnabledState(true)

    private fun setComponentsEnabledState(newState: Boolean) {
        components.forEach { component ->
            component?.isEnabled = newState
        }
    }

}
