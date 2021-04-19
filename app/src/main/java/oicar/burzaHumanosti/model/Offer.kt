package oicar.burzaHumanosti.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Offer(
    val id: Int,
    val name: String,
    val description: String,
    val image: String,
    val articleCondition: Condition,
    val subCategory: OfferSubCategory
 ): Parcelable