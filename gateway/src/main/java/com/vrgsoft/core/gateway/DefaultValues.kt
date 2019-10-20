package com.vrgsoft.core.gateway

fun Long?.orMinusOne(): Long = this ?: -1L

fun Long?.orZero(): Long = this ?: 0L

fun Int?.orMinusOne(): Int = this ?: -1

fun Int?.orZero(): Int = this ?: 0