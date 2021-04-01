package oicar.burzaHumanosti.model

data class Category (
    val id: Int,
    val name: String,
    val subCategories: List<SubCategory>
)