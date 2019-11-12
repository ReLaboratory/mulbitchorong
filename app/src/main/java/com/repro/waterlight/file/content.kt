package com.repro.waterlight.file

data class ContentDTO(var imageuri: String? = null, var userId: String? = null, var time: String? = null, var name: String? = null)
data class UserDTO(var id: String? = null, var name: String? = null, var profiluri: String? = null, var pw: String? = null)
data class SignupSuccess( var uname: String? = null, var isSuccess: Boolean? = null)