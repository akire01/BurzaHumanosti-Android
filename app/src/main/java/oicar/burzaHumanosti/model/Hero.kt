package oicar.burzaHumanosti.model

data class Hero (
    val id: Int,
    val firstName: String,
    val lastName: String,
    val image: String,
    val organization: String,
    val helped: Int
)