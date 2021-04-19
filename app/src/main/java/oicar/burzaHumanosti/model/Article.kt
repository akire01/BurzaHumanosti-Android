package oicar.burzaHumanosti.model

import android.text.BoringLayout

data class Article (
    val id: Int,
    val name: String,
    val description: String,
    val image: String?,
    val subCategoryId: Int,
    val articleConditionId: Int,
    val articleCondition: Condition?,
    val accountId: Int?,
    val active: Boolean?,
    val account: Account?,
    val delivery: Delivery
)