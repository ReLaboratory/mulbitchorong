package com.repro.waterlight.file

import java.io.File

data class ContentDTO(var imageuri: String? = null, var userId: String? = null, var time: String? = null, var name: String? = null)
data class UserDTO(var id: String? = null, var name: String? = null, var profiluri: String? = null, var pw: String? = null)
data class SignSuccess(var uname: String? = null, var isSuccess: Boolean? = null)
data class GetName(var uname: String? = null)
data class UploadSuccess(var isSuccess: Boolean? = null)
data class GetimgNames(var imgName: String? = null)
data class Getimgs(var img: File? = null)