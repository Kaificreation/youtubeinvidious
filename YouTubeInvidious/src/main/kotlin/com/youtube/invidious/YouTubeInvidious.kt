
package com.youtube.invidious

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.api.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.Jsoup

class YouTubeInvidious : MainAPI() {
    override var mainUrl = "https://invidious.snopyta.org"
    override var name = "YouTube (Invidious)"
    override val hasMainPage = true
    override val hasQuickSearch = true
    override val hasDownloadSupport = true

    override val supportedTypes = setOf(TvType.Movie)

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/search?q=" + query.replace(" ", "+")
        val doc = app.get(url).document
        return doc.select(".video-card").mapNotNull {
            val title = it.selectFirst(".video-card-title")?.text() ?: return@mapNotNull null
            val href = it.selectFirst("a")?.attr("href") ?: return@mapNotNull null
            val poster = it.selectFirst("img")?.attr("src") ?: ""

            MovieSearchResponse(
                title,
                mainUrl + href,
                this.name,
                TvType.Movie,
                poster,
                null,
                null
            )
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val doc = app.get(url).document
        val title = doc.selectFirst("h1")?.text() ?: "Video"
        val videoUrl = doc.select("video > source").attr("src")
        return MovieLoadResponse(
            title,
            url,
            this.name,
            listOf(
                ExtractorLink(this.name, "Stream", videoUrl, mainUrl, Qualities.P720.value)
            ),
            null,
            null
        )
    }
}
