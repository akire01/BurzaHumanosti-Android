package oicar.burzaHumanosti.model

data class Article (
    val id: Int,
    val name: String,
    val description: String,
    val image: String,
    val subCategoryId: Int,
    val articleConditionId: Int,
    val accountId: Int

)