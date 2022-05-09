package com.netjdev.tfg_android_app

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
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
class ConsultarDocumentoTest {

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
    fun consultarDocumentoTest(){
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()))
        // Comprobar que el botón existe y pulsarlo
        onView(withId(R.id.btnDocs)).check(matches(isDisplayed()))
        onView(withId(R.id.btnDocs)).perform(click())
        // Comprobar que se ha cargado la activity list_of_doc_category
        onView(withId(R.id.activity_list_of_doc_category)).check(matches(isDisplayed()))
        // Comprobar que las categorias de documentos son correctas
        onView(withId(R.id.listDocTypeRecyclerView)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        ).check(matches(ViewMatchers.hasDescendant(ViewMatchers.withText("Guías de entrenamiento"))))

        onView(withId(R.id.listDocTypeRecyclerView)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                1
            )
        ).check(
            matches(ViewMatchers.hasDescendant(ViewMatchers.withText("Guías de nutrición")))
        )
        // Aceder a una categoría
        onView(withId(R.id.listDocTypeRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )
        // Acceder a un documento
        onView(withId(R.id.listDocumentsRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )
    }
}