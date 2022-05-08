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
class EscribirMensajeChatTest {
    @Test
    fun escribirMensajeChatTest() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        // Comprobar que el botón chat existe
        Espresso.onView(ViewMatchers.withId(R.id.btnChat))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.btnChat)).perform(ViewActions.click())
        // Comprobar que se ha cargado la activity Chat
        Espresso.onView(ViewMatchers.withId(R.id.activity_chat))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        // Comprobar que existe la caja de texto para escribir el mensaje
        Espresso.onView(ViewMatchers.withId(R.id.tvMsg))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        // Escribir el mensaje en la caja de texto
        Espresso.onView(ViewMatchers.withId(R.id.tvMsg))
            .perform(ViewActions.typeText("Mensaje Espresso"))
        // Comprbar que existe el botón para enviar el mensaje
        Espresso.onView(ViewMatchers.withId(R.id.btnSendMsg))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        // Pulsar el botón para enviar el mensaje
        Espresso.onView(ViewMatchers.withId(R.id.btnSendMsg)).perform(ViewActions.click())
    }
}