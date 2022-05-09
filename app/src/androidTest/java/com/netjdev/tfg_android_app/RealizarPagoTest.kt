package com.netjdev.tfg_android_app

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.netjdev.tfg_android_app.util.EspressoIdlingResource
import com.netjdev.tfg_android_app.vistas.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// Esta anotación (@LargeTest) indica que se realizaran accesos a recursos externos, como redes
@LargeTest
@RunWith(AndroidJUnit4::class)
class RealizarPagoTest {

    @get:Rule
    val activityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp(){
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown(){
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun realizarPagoTest() {
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.btnPayments)).perform(click())
        // Comprobar que se ha cargado la activity StripeActivity
        onView(withId(R.id.activity_stripe)).check(matches(isDisplayed()))
        // Comprbar que existe el botón Pay antes de pulsarlo
        onView(withId(R.id.btnPay)).check(matches(isDisplayed()))
        // Pulsar el botón Pay para realizar el pago
        onView(withId(R.id.btnPay)).perform(click())
        // El alcance del test temina aquí, ya que el resto del proceso de pago (introducir tarjeta)
        // está delegado a la api de Stripe
    }
}