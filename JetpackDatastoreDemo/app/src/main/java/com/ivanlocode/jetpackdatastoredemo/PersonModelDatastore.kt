package com.ivanlocode.jetpackdatastoredemo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PersonModelDatastore(val context:Context) {

    companion object{
        private val Context.personModelDatastore: DataStore<Preferences> by preferencesDataStore("PersonModelDatastore")
        private val PERSON_MODEL_FIRSTNAME = stringPreferencesKey("PERSON_MODEL_FIRSTNAME")
        private val PERSON_MODEL_SURNAME = stringPreferencesKey("PERSON_MODEL_SURNAME")
        private val PERSON_MODEL_AGE = intPreferencesKey("PERSON_MODEL_AGE")
    }

    val personModel : Flow<PersonModel> =
        context.personModelDatastore.data.map { preferences ->
            PersonModel(
                preferences[PERSON_MODEL_FIRSTNAME] ?: "",
                preferences[PERSON_MODEL_SURNAME] ?: "",
                preferences[PERSON_MODEL_AGE] ?: 0
            )
        }

    suspend fun savePersonModel(personModel: PersonModel){
        context.personModelDatastore.edit { preferences ->
            preferences[PERSON_MODEL_FIRSTNAME] = personModel.firstname
            preferences[PERSON_MODEL_SURNAME] = personModel.surname
            preferences[PERSON_MODEL_AGE] = personModel.age
        }
    }
}