package com.amitsalunke.datastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import com.amitsalunke.datastore.databinding.ActivityMainBinding
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    //preferences data store
    // they are not type safe

    private var _binding: ActivityMainBinding? = null

    private val binding get() = _binding!!

    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ui(binding)
        //outside of activity need to call context.createDataStore
        dataStore = createDataStore(name = "settings")

    }

    private fun ui(binding: ActivityMainBinding) {
        with(binding) {
            btnSave.setOnClickListener {
                lifecycleScope.launch {

                    save(etSaveKey.text.toString(), etSaveValue.text.toString())
                }
            }

            btnRead.setOnClickListener {
                lifecycleScope.launch {
                    val readVal = read(etReadkey.text.toString())
                    tvReadValue.text = readVal ?: "no value found"
                }
            }
        }

    }

    private suspend fun save(key: String, value: String) {
        val dataStoreKey = preferencesKey<String>(key) //if u want store any boolen value the set preferencesKey<Boolean>(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun read(key: String): String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /*
    val EXAMPLE_COUNTER = preferencesKey<Int>("example_counter")
val exampleCounterFlow: Flow<Int> = dataStore.data
  .map { preferences ->
    // No type safety.
    preferences[EXAMPLE_COUNTER] ?: 0
}
     */
}