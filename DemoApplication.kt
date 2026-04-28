package com.gfk.s2s.demo.s2s

import android.app.Application
import com.gfk.s2s.demo.s2s.constants.DemoConstants.demoConfigPreproduction

class DemoApplication : Application() {
    companion object {
        var configURL = demoConfigPreproduction
    }
}