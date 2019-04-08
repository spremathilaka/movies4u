package com.zotikos.m4u.util

import androidx.annotation.StringRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.R
import com.google.gson.Gson
import org.hamcrest.Matchers

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object TestUtils {

    @Throws(IOException::class)
    fun readJsonFile(filename: String): String {
        val br = BufferedReader(InputStreamReader(TestUtils::class.java.classLoader!!.getResourceAsStream(filename)))
        val sb = StringBuilder()
        var line: String? = br.readLine()
        while (line != null) {
            sb.append(line)
            line = br.readLine()
        }

        return sb.toString()
    }

    fun <T> loadData(fileName: String, kClass: Class<T>): T? {
        return try {
            val stringFormattedData = readJsonFile(fileName)
            val gson = Gson()
            gson.fromJson(stringFormattedData, kClass)
        } catch (e: Exception) {
            null
        }

    }

    fun checkSnackBarDisplayedByMessage(@StringRes message: Int) {
        Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.snackbar_text), ViewMatchers.withText(message)))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE
                    )
                )
            )
    }
}
