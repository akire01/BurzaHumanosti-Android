package oicar.burzaHumanosti.model

data class UpdateArticle (
    val name: String,
    val description: String,
    val subCategoryId: Int,
    val articleConditionId: Int
)