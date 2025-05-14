
package com.youtube.invidious

import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.Plugin

@Plugin
class YouTubePlugin: Plugin() {
    override fun load(context: android.content.Context) {
        registerMainAPI(YouTubeInvidious())
    }
}
