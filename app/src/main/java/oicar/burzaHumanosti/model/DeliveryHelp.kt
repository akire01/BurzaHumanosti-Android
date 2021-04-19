package oicar.burzaHumanosti.model

data class DeliveryHelp (
    val id: Int,
    val deliveryStatus: DeliveryStatus,
    val deliveryType: DeliveryType,
    val subCategory: SubCategory
    )