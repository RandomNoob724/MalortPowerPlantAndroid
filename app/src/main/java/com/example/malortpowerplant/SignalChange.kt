package com.example.malortpowerplant

import kotlin.properties.Delegates

object SignalChange{
    var radiationOutput = 30

    var prop: Int by Delegates.observable(radiationOutput){property, oldValue, newValue ->
        radiationOutput = newValue
    }


}