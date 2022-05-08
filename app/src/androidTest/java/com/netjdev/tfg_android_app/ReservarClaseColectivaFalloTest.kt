package com.netjdev.tfg_android_app

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.netjdev.tfg_android_app.vistas.MainActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ReservarClaseColectivaFalloTest {
    /**
     * Reserva de una clase colectiva con un nombre no coincidente que provoca que no se pase el test
     */
    @Test
    fun reservarClaseColectivaFalloTest() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.btnActivities)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.activity_list_of_group_clases))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.listGroupClassesRecyclerView)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        ).check(
            ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText("nombre")))
        )
    }
}