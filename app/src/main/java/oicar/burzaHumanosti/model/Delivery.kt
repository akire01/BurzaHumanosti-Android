package oicar.burzaHumanosti.model

data class Delivery (
    val id: Int,
    val comment: String?,
    val days: Int?,
    val deliveryStatus: DeliveryStatus,
    val deliveryType: DeliveryType
)