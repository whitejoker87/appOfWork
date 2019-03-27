package ru.jobni.jobni.model.menu

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class NavigationChild(
        val name: String,
        val iconResId: Int
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NavigationChild

        if (name != other.name) return false
        return true
    }

    override fun hashCode() = name.hashCode()
}

