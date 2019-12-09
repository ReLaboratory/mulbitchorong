package com.repro.waterlight.file

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

//data class ContentDTO(var imageuri: String? = null, var userId: String? = null, var time: String? = null, var name: String? = null)
//data class UserDTO(var id: String? = null, var name: String? = null, var profiluri: String? = null, var pw: String? = null)
data class SignSuccess(var uname: String? = null, var isSuccess: Boolean? = null)
data class GetName(var uname: String? = null)
data class UploadSuccess(var isSuccess: Boolean? = null)

@Parcelize
data class GetimgNames(var imgName: String? = null): Parcelable{
    constructor(parcel: Parcel): this() {
        parcel.run {
            imgName = readString()
        }
    }
    private companion object : Parceler<GetimgNames> {
        override fun GetimgNames.write(parcel: Parcel, flags: Int) {
            parcel.writeString(imgName)
        }
        override fun create(parcel: Parcel): GetimgNames {
            return GetimgNames(parcel)
        }
    }
}
