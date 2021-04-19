package oicar.burzaHumanosti.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Condition(
    val id : Int,
    val name: String
) : Parcelable
