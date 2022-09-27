package com.example.laboratorio8

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var inputMail: TextInputLayout
    private lateinit var inputPW: TextInputLayout
    private lateinit var btn: MaterialButton

    private val mail = "flo21500@uvg.edu.gt"

    companion object{
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn = view.findViewById(R.id.loginBtn)
        inputMail = view.findViewById(R.id.mail)
        inputPW = view.findViewById(R.id.pw)

        userLogged()
        setListeners()
    }

    private fun userLogged() {
        val dataStoreKey = stringPreferencesKey("mail")

        CoroutineScope(Dispatchers.IO).launch {
            val preferences = requireActivity().dataStore.data.first()
            if(preferences[dataStoreKey] != null){
                CoroutineScope(Dispatchers.Main).launch {
                    requireView().findNavController().navigate(R.id.action_loginFragment_to_charactersFragment)
                }
            }
        }
    }

    private fun setListeners() {
        btn.setOnClickListener {

            var tempMail = inputMail.editText?.text.toString()
            var pw = inputPW.editText?.text.toString()

            if(tempMail == this.mail && pw == this.mail) {

                CoroutineScope(Dispatchers.IO).launch {
                    saveKeyValue(
                        key = "mail",
                        value = mail
                    )
                }

                requireView().findNavController().navigate(R.id.action_loginFragment_to_charactersFragment)

            }
            else Toast.makeText(activity, "Las credenciales ingresadas no son validas", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun deleteMail(context: Context){
        val dataStoreKey = stringPreferencesKey("mail")

        context.dataStore.edit { settings ->
            settings.remove(dataStoreKey)
        }
    }

    private suspend fun saveKeyValue(key:String, value:String) {
        val dataStoreKey = stringPreferencesKey(key)

        requireActivity().dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }
}