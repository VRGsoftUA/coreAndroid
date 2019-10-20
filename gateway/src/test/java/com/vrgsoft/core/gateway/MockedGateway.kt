package com.vrgsoft.core.gateway

import com.vrgsoft.core.remote.error.BaseError
import com.vrgsoft.networkmanager.NetworkManager

class MockedGateway(manager: NetworkManager) : BaseGateway(manager) {
    override fun calculateMessage(error: BaseError): String {
        return ""
    }
}