package ru.jobni.jobni.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AutoDetailVacancy {

    @SerializedName("year_of_manufacture")
    @Expose
    var yearOfManufacture: String? = null
    @SerializedName("fuel_consumption_city")
    @Expose
    var fuelConsumptionCity: String? = null
    @SerializedName("fuel_consumption_highway")
    @Expose
    var fuelConsumptionHighway: String? = null
    @SerializedName("cargo_capacity")
    @Expose
    var cargoCapacity: String? = null
    @SerializedName("capacity_of_people")
    @Expose
    var capacityOfPeople: String? = null

}
