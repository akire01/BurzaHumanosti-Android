package oicar.burzaHumanosti.model

import android.provider.ContactsContract

data class Account (
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val image: String?,
    val organization: String?,
    val familyMembers: Int
  )