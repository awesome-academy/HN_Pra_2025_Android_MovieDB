package com.sun.moviedb.data.repository.source.remote.exception

/* *
* The purpose: Catch logical errors
* <code: 200; method: GET>
* {
*   "status":false,
*   "msg":"Movie not found",
*   "movie":"",
*   "episodes":""
* }
* */
class ApiLogicalException(message: String) : Exception(message)

