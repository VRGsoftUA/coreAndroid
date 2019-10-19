package com.vrgsoft.retrofit.common

abstract class Auth {
    abstract fun createAuthHeaderString(): String
}