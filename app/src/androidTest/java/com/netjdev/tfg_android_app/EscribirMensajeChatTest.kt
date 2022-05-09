package com.netjdev.tfg_android_app

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
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

class EscribirMensajeChatTest {
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
    fun escribirMensajeChatTest(){
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()))
        // Comprobar que el botón chat existe
        onView(withId(R.id.btnChat)).check(matches(isDisplayed()))
        onView(withId(R.id.btnChat)).perform(click())
        // Comprobar que se ha cargado la activity Chat
        onView(withId(R.id.activity_chat)).check(matches(isDisplayed()))
        // Comprobar que existe la caja de texto para escribir el mensaje
        onView(withId(R.id.tvMsg)).check(matches(isDisplayed()))
        // Escribir el mensaje en la caja de texto
        onView(withId(R.id.tvMsg)).perform(ViewActions.typeText("Mensaje Espresso"))
        // Comprbar que existe el botón para enviar el mensaje
        onView(withId(R.id.btnSendMsg)).check(matches(isDisplayed()))
        // Pulsar el botón para enviar el mensaje
        onView(withId(R.id.btnSendMsg)).perform(click())
    }
}