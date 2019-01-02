package br.pedroso.calculatorsample.view

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import br.pedroso.calculatorsample.di.ServiceLocator
import br.pedroso.calculatorsample.domain.Operation
import br.pedroso.calculatorsample.usecases.DoOperation
import io.reactivex.Single
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class CalculatorActivityUiTest {

    @get:Rule
    var activityRule: ActivityTestRule<CalculatorActivity> =
            ActivityTestRule(CalculatorActivity::class.java, true, false)

    companion object {
        private const val VALID_TEXT = "2.0"
        private val VALID_TEXT_VALUE = VALID_TEXT.toFloat()
        private const val EMPTY_TEXT = ""
        private const val FAKE_FIELD_NAME = "fieldName"
    }

    @Before
    fun setup() {
        ServiceLocator.clearRegisteredServices()
    }

    @Test
    fun test() {
        startActivity()

        mockDoOperationResultWithValue(VALID_TEXT_VALUE)

        onView(withHint(R.string.hint_first_number)).perform(typeText(VALID_TEXT))

        onView(withHint(R.string.hint_second_number)).perform(typeText(VALID_TEXT))

        onView(withText(R.string.calculate)).perform(click())

        val expectedResultText = context.getString(R.string.result_string, VALID_TEXT_VALUE)

        onView(withId(R.id.textViewResult)).check(matches(allOf(withText(expectedResultText), isDisplayed())))
    }

    private val context = InstrumentationRegistry.getTargetContext()

    private fun startActivity() = activityRule.launchActivity(Intent())

    private fun mockDoOperationResultWithValue(value: Float) =
            mockDoOperation(AlwaysSuccessDoOperation(value))

    private fun mockDoOperationResultWithError(error: Throwable) =
            mockDoOperation(AlwaysFailDoOperation(error))

    private fun mockDoOperation(doOperation: DoOperation) {
        ServiceLocator.registerService(DoOperation::class.java, doOperation)
    }

    class AlwaysSuccessDoOperation(private val result: Float) : DoOperation {
        override fun execute(operation: Operation, firstNumberText: String, secondNumberText: String): Single<Float> {
            return Single.just(result)
        }
    }

    class AlwaysFailDoOperation(private val error: Throwable) : DoOperation {
        override fun execute(operation: Operation, firstNumberText: String, secondNumberText: String): Single<Float> {
            return Single.error(error)
        }
    }
}