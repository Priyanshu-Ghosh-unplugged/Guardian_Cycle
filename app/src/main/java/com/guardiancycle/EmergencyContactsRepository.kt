package com.guardiancycle

import android.content.Context


data class EmergencyContact(val name: String, val phoneNumber: String)

class EmergencyContactsRepository(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("EmergencyContacts", Context.MODE_PRIVATE)

    /**
     * Saves an emergency contact.
     * @param contact The EmergencyContact object to save.
     */
    fun saveContact(contact: EmergencyContact) {
        val editor = sharedPreferences.edit()
        editor.putString(contact.name, contact.phoneNumber)
        editor.apply()
    }

    /**
     * Retrieves all saved emergency contacts as a list of EmergencyContact objects.
     * @return A list of EmergencyContact.
     */
    fun getEmergencyContacts(): List<EmergencyContact> {
        return sharedPreferences.all.mapNotNull {
            val name = it.key
            val phoneNumber = it.value as? String
            if (phoneNumber != null) EmergencyContact(name, phoneNumber) else null
        }
    }

    /**
     * Deletes an emergency contact by name.
     * @param name Name of the contact to delete.
     */
    fun deleteContact(name: String) {
        val editor = sharedPreferences.edit()
        editor.remove(name)
        editor.apply()
    }

    /**
     * Clears all saved emergency contacts.
     */
    fun clearAllContacts() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
