package com.netjdev.tfg_android_app

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
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
class ReservarClaseColectivaTest {

    @get:Rule
    val activityTestRule = ActivityScenarioRule(MainActivity::class.java)
    //val activityTestRule = ActivityScenarioRule(MenuPrincipal::class.java)

    @Before
    fun setUp(){
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown(){
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun reservaClaseColectiva(){
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()))
        // Comprobar que el botón Activities existe
        onView(withId(R.id.btnActivities)).check(matches(isDisplayed()))
        // Pulsar botón Activities
        onView(withId(R.id.btnActivities)).perform(click())
        // Comprobar que la actividad se ha cargado
        onView(withId(R.id.activity_list_of_group_clases)).check(matches(isDisplayed()))
        // Comprobar que el elemento del recyclerview coincide con el que se busca
        onView(withId(R.id.listGroupClassesRecyclerView)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        ).check(
            matches(ViewMatchers.hasDescendant(ViewMatchers.withText("Aikido")))
        )
        // Pulsar sobre el elemento del recyclerview
        onView(withId(R.id.listGroupClassesRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )
        // Comprobar que el boton del día (btnFriday) se ha cargado
        onView(withId(R.id.btnFriday))
            .check(matches(isDisplayed()))
        // Pulsar el boton del día
        onView(withId(R.id.btnFriday)).perform(click())
        // Comprobar que se ha cargado la hora buscada
        onView(withId(R.id.listTimeRecyclerView)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                1
            )
        ).check(
            matches(ViewMatchers.hasDescendant(ViewMatchers.withText("19:00 - 20:00")))
        )
        // Pulsar el botón de la hora seleccionada para realizar la reserva
        onView(withId(R.id.listTimeRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    clickOnRecyclerViewChild(R.id.btnAddReserve)
                )
            )
        // Comprobar que la activity de confirmación de reserva se ha cargado
        onView(withId(R.id.activity_confirm_reservation)).check(matches(isDisplayed()))
        // Comprobar que el botón de cerrar existe
        onView(withId(R.id.btnCloseConfirm)).check(matches(isDisplayed()))
        // Pulsar botón de cerrar
        onView(withId(R.id.btnCloseConfirm)).perform(click())
    }

    // Función que implementa el click en una vista dentro de un RecyclerView
    fun clickOnRecyclerViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null
        override fun getDescription() = "Click en una vista hija con un id especifico."
        override fun perform(uiController: UiController, view: View) = ViewActions.click()
            .perform(uiController, view.findViewById<View>(viewId))
    }
}