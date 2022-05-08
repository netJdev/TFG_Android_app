package com.netjdev.tfg_android_app

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.netjdev.tfg_android_app.vistas.MainActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ReservarClaseColectivaTest {
    @Test
    fun reservarClaseColectivaTest() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        // Comprobar que el botón Activities existe
        Espresso.onView(ViewMatchers.withId(R.id.btnActivities))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        // Pulsar botón Activities
        Espresso.onView(ViewMatchers.withId(R.id.btnActivities)).perform(ViewActions.click())
        // Comprobar que la actividad se ha cargado
        Espresso.onView(ViewMatchers.withId(R.id.activity_list_of_group_clases))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        // Comprobar que el elemento del recyclerview coincide con el que se busca
        Espresso.onView(ViewMatchers.withId(R.id.listGroupClassesRecyclerView)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        ).check(
            ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText("Aikido")))
        )
        // Pulsar sobre el elemento del recyclerview
        Espresso.onView(ViewMatchers.withId(R.id.listGroupClassesRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    ViewActions.click()
                )
            )
        // Comprobar que el boton del día (btnFriday) se ha cargado
        Espresso.onView(ViewMatchers.withId(R.id.btnFriday))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        // Pulsar el boton del día
        Espresso.onView(ViewMatchers.withId(R.id.btnFriday)).perform(ViewActions.click())
        // Comprobar que se ha cargado la hora buscada
        Espresso.onView(ViewMatchers.withId(R.id.listTimeRecyclerView)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                1
            )
        ).check(
            ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText("19:00 - 20:00")))
        )
        // Pulsar el botón de la hora seleccionada para realizar la reserva
        Espresso.onView(ViewMatchers.withId(R.id.listTimeRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    clickOnRecyclerViewChild(R.id.btnAddReserve)
                )
            )
        // Comprobar que la activity de confirmación de reserva se ha cargado
        /*Espresso.onView(ViewMatchers.withId(R.id.activity_confirm_reservation))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        // Comprobar que el botón de cerrar existe
        Espresso.onView(ViewMatchers.withId(R.id.btnCloseConfirm))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        // Pulsar botón de cerrar
        Espresso.onView(ViewMatchers.withId(R.id.btnCloseConfirm)).perform(ViewActions.click())*/
    }

    // Función que implementa el click de una vista dentro de un RecyclerView
    fun clickOnRecyclerViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null
        override fun getDescription() = "Click en una vista hija con un id especifico."
        override fun perform(uiController: UiController, view: View) = click().perform(uiController, view.findViewById<View>(viewId))
    }
}