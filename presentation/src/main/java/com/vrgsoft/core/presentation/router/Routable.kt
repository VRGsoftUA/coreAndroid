package com.vrgsoft.core.presentation.router

interface Routable {
    val router: IBaseRouter
    fun backPressed(): Boolean = router.clickBack()
}