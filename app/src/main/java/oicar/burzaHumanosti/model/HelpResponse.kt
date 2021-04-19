package oicar.burzaHumanosti.model

data class HelpResponse (
    val id: Int,
    val article: Article,
    val account: Account,
    val stories: List<StoryResponse>?
)