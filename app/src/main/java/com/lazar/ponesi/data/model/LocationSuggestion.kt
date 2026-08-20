package com.lazar.ponesi.data.model

data class LocationSuggestion(
    val name: String,
    val administrativeArea: String?,
    val country: String?,
    val latitude: Double,
    val longitude: Double
) {
    val displayName: String
        get() = listOfNotNull(
            name,
            administrativeArea
                ?.takeIf { it.isNotBlank() && it != name },
            country
                ?.takeIf {
                    it.isNotBlank() &&
                            it != name &&
                            it != administrativeArea
                }
        ).joinToString(", ")

    fun toTravelLocation(): TravelLocation {
        return TravelLocation(
            name = displayName,
            latitude = latitude,
            longitude = longitude
        )
    }
}