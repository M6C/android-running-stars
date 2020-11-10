package com.runningstars.extention

import android.location.Location

/**
 * Returns the `location` object as a human readable string.
 */
fun Location?.toText():String {
    return if (this != null) {
        "($latitude, $longitude)"
    } else {
        "Unknown location"
    }
}
