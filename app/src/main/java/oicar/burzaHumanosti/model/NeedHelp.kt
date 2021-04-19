package oicar.burzaHumanosti.model

data class NeedHelp (
    val id : Int,
    val article: HelpArticle,
    val account: Account
    )