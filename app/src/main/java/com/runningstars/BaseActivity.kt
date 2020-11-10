package com.runningstars

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.runningstars.provider.InstanceStateProvider

// https://programmerr47.medium.com/save-state-with-delegates-d3c7d3a6a474
open class BaseActivity : AppCompatActivity() {
    private val savable = Bundle()
    override fun onCreate(savedInstanceState: Bundle?) {
        if(savedInstanceState != null) {
            savable.putAll(savedInstanceState.getBundle("_state"))
        }
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBundle("_state", savable)
        super.onSaveInstanceState(outState)
    }

    protected fun <T> instanceState() = InstanceStateProvider.Nullable<T>(savable)
    protected fun <T> instanceState(defaultValue: T) = InstanceStateProvider.NotNull(savable, defaultValue)
}