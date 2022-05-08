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
class ConsultarDocumentoTest {
    @Test
    fun consultarDocumentoTest() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        // Comprobar que el botón existe y pulsarlo
        Espresso.onView(ViewMatchers.withId(R.id.btnDocs))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.btnDocs)).perform(ViewActions.click())
        // Comprobar que se ha cargado la activity list_of_doc_category
        Espresso.onView(ViewMatchers.withId(R.id.activity_list_of_doc_category))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        // Comprobar que las categorias de documentos son correctas
        Espresso.onView(ViewMatchers.withId(R.id.listDocTypeRecyclerView)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        ).check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText("Guías de entrenamiento"))))

        Espresso.onView(ViewMatchers.withId(R.id.listDocTypeRecyclerView)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                1
            )
        ).check(
            ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText("Guías de nutrición")))
        )
        // Aceder a una categoría
        Espresso.onView(ViewMatchers.withId(R.id.listDocTypeRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    ViewActions.click()
                )
            )
        // Acceder a un documento
        Espresso.onView(ViewMatchers.withId(R.id.listDocumentsRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    ViewActions.click()
                )
            )
    }
}