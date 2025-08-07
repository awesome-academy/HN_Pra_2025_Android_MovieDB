package com.sun.moviedb.data.repository.source.remote.model

data class MovieDetail(
    val tmdb: TmdbInfo?,
    val imdb: ImdbInfo?,
    val created: CreatedInfo?,
    val modified: ModifiedInfo?,
    val _id: String?,
    val name: String?,
    val slug: String?,
    val origin_name: String?,
    val content: String?,
    val type: String?,
    val status: String?,
    val poster_url: String?,
    val thumb_url: String?,
    val is_copyright: Boolean?,
    val sub_docquyen: Boolean?,
    val chieurap: Boolean?,
    val trailer_url: String?,
    val time: String?,
    val episode_current: String?,
    val episode_total: String?,
    val quality: String?,
    val lang: String?,
    val notify: String?,
    val showtimes: String?,
    val year: Int?,
    val view: Int?,
    val actor: List<String>?,
    val director: List<String>?,
    val category: List<Category>?,
    val country: List<Country>?
)

data class EpisodeItem(
    val name: String?,
    val slug: String?,
    val filename: String?,
    val link_embed: String?,
    val link_m3u8: String?
)

data class EpisodeGroup(
    val server_name: String?,
    val server_data: List<EpisodeItem>?
)

data class MovieDetailResponse(
    val status: Boolean?,
    val msg: String?,
    val movie: MovieDetail?,
    val episodes: List<EpisodeGroup>?
)

