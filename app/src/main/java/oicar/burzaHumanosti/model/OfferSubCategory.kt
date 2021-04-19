package oicar.burzaHumanosti.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OfferSubCategory (
    val id: Int,
    val name: String,
    val category: Category
) : Parcelable