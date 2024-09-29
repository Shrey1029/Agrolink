import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import java.util.Locale

object LocaleManager {

    private const val PREFS_NAME = "prefs"
    private const val LANGUAGE_KEY = "language"

    // Retrieve saved language from shared preferences
    fun getSavedLanguage(context: Context): String {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(LANGUAGE_KEY, "English") ?: "English"
    }

    // Set the locale based on the selected language
    fun setLocale(context: Context, language: String) {
        val locale = when (language) {
            "Hindi" -> Locale("hi")
            "Spanish" -> Locale("es")
            else -> Locale("en") // Default to English
        }

        // Save the selected language to shared preferences
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(LANGUAGE_KEY, language)
            apply()
        }

        // Update the locale in the configuration
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)
        context.createConfigurationContext(configuration) // This line may not affect all use cases

        // Optional: You may need to restart the activity to apply changes
        // Example: (context as? Activity)?.recreate()
    }
}
