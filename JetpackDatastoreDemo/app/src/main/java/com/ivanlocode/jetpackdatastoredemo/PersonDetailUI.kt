package com.ivanlocode.jetpackdatastoredemo

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PersonDetailUI(
     modifier: Modifier = Modifier
){

    val context = LocalContext.current
    val personDataStore = PersonModelDatastore(context)
    val savedPersonModel = personDataStore.personModel.collectAsState(PersonModel("","",0))
    val firstnameInput = remember { mutableStateOf("") }
    val surnameInput = remember { mutableStateOf("") }
    val ageInput = remember { mutableStateOf(0) }
    val isLoadDataFirstTime = remember { mutableStateOf(true) }

    //Once the savedPersonModel is loaded from Coroutine the block of code will execute and refresh the UI compose
    if (isLoadDataFirstTime.value && savedPersonModel.value.firstname.isNotEmpty()) {
        Toast.makeText(context, "Loaded from datastore", Toast.LENGTH_SHORT).show()
        firstnameInput.value = savedPersonModel.value.firstname
        surnameInput.value = savedPersonModel.value.surname
        ageInput.value = savedPersonModel.value.age
        isLoadDataFirstTime.value = false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
    ) {

        Text(
            modifier = modifier.padding(10.dp),
            text = "DataStore Stored Data: \n\nFirstname: ${savedPersonModel.value.firstname}"
                   + "\nSurname: ${savedPersonModel.value.surname}"
                   + "\nAge: ${savedPersonModel.value.age}"
            ,
            fontSize = 20.sp,
            color = Color.Blue
        )

        OutlinedTextField(
            value = firstnameInput.value,
            onValueChange = {str ->
                firstnameInput.value = str
            },
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            label = {Text(text = "Firstname")}
        )

        OutlinedTextField(
            value = surnameInput.value,
            onValueChange = {str ->
                surnameInput.value = str
            },
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            label = {Text(text = "Surname")}
        )

        OutlinedTextField(
            value = ageInput.value.toString(),
            onValueChange = {str ->
                try{
                    ageInput.value = str.toInt()
                }catch (e: Exception){
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            label = {Text(text = "Age")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedButton(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    personDataStore.savePersonModel(PersonModel(firstname = firstnameInput.value, surname = surnameInput.value, age = ageInput.value))
                }
                Toast.makeText(context, "Data saved to Datastore", Toast.LENGTH_SHORT).show()

            }
        ) {
            Text(
                text = "Save",
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = Color.Red
            )
        }


    }

}