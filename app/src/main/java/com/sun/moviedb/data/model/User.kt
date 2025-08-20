package com.sun.moviedb.data.model

import com.google.firebase.firestore.PropertyName

data class User(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("username") @set:PropertyName("username") var username: String? = null,
    @get:PropertyName("email") @set:PropertyName("email") var email: String? = null,
    @get:PropertyName("profileImageUrl") @set:PropertyName("profileImageUrl") var profileImageUrl: String? = null
) {
    constructor() : this("", null, null, null)
}
