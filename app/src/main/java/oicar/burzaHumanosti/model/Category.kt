package oicar.burzaHumanosti.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category (
    val id: Int,
    val name: String,
    val subCategories: List<SubCategory>
): Parcelable