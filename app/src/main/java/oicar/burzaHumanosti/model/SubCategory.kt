package oicar.burzaHumanosti.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubCategory (
    val id: Int,
    val name: String
):Parcelable