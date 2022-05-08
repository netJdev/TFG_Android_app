package com.netjdev.tfg_android_app

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.netjdev.tfg_android_app.vistas.MainActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class RealizarPagoTest {
    @Test
    fun realizarPagoTest() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.btnPayments)).perform(ViewActions.click())
        // Comprobar que se ha cargado la activity StripeActivity
        Espresso.onView(ViewMatchers.withId(R.id.activity_stripe))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        // Comprbar que existe el botón Pay antes de pulsarlo
        Espresso.onView(ViewMatchers.withId(R.id.btnPay))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        // Pulsar el botón Pay para realizar el pago
        Espresso.onView(ViewMatchers.withId(R.id.btnPay)).perform(ViewActions.click())
        // El alcance del test temina aquí, ya que el resto del proceso de pago (introducir tarjet)
        // está delegado a la api de Stripe
    }
}