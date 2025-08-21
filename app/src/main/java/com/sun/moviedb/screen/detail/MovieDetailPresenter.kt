package com.sun.moviedb.screen.detail

import com.sun.moviedb.data.model.Member
import com.sun.moviedb.data.model.Movie
import com.sun.moviedb.data.model.Room
import com.sun.moviedb.data.repository.rtdb.MemberRepository
import com.sun.moviedb.data.repository.rtdb.RoomRepository
import com.sun.moviedb.data.repository.source.MovieRepository
import com.sun.moviedb.data.repository.source.remote.NetworkResult
import com.sun.moviedb.data.repository.source.remote.dto.MovieDetailResponse
import com.sun.moviedb.utils.session.RoomSession
import com.sun.moviedb.utils.session.UserSession

class MovieDetailPresenter
    internal constructor(
        private val mMovieRepository: MovieRepository?,
        private val roomRepository: RoomRepository,
        private val memberRepository: MemberRepository
    ) : MovieDetailContract.Presenter{
        private var mView: MovieDetailContract.View? = null

    override fun attachView(view: MovieDetailContract.View) {
        this.mView = view
    }

    override fun detachView() {
        mView = null
    }

    override fun getDetail(slug: String) {
        mMovieRepository?.getDetailMovie(slug){result ->
            when(result){
                is NetworkResult.OnSuccess<MovieDetailResponse> ->{
                    val movie = result.data.movie
                    val episodes = result.data.episodes

                    mView?.onGetDetailSuccess(movie, episodes)
                }
                is NetworkResult.OnError -> {
                    val errorMessage = result.message
                    mView?.showError(errorMessage)
                }
            }
        }
    }

    override fun onFavClicked(movie: Movie, isFavourite: Boolean) {
        if (isFavourite){
            //add to favourite list
        }else {
            //remove from favourite list
        }
    }

    override fun createRoom(movie: Movie) {
        val userID = UserSession.userId.run {
            if (this.isNullOrEmpty()) {
                throw IllegalStateException("Current user not found")
            } else this
        }
        val room = Room(
            roomName = movie.name,
            roomCode = movie.slug,
            createAt = System.currentTimeMillis(),
            createBy = userID
        )

        mView?.showLoading2(true)
        roomRepository.addRoom(room){ result ->
            when(result){
                is NetworkResult.OnSuccess -> {
                    // Room created successfully
                    mView?.onAddSuccess("Room created successfully")
                }
                is NetworkResult.OnError -> {
                    // Failed to create room
                    mView?.showError(result.message)
                }
            }
            mView?.showLoading2(false)
        }


    }

    override fun addMember() {
        mView?.showLoading2(true)

        val userID = UserSession.userId.run {
            if (this.isNullOrEmpty()) {
                throw IllegalStateException("Current user not found")
            } else this
        }

        val roomId = RoomSession.roomId.run {
            if (this.isNullOrEmpty()) {
                throw IllegalStateException("Room ID is not set")
            } else this
        }

        val member = Member(
            memberId = userID,
            memberName = UserSession.userName ?: "Unknown",
            linkAvatar = UserSession.linkAvatar ?: "",
            joinAt = System.currentTimeMillis(),
            isHost = true // Assuming the user is the host when creating the room
        )

        memberRepository.addMember(roomId, member) { result ->
            when (result) {
                is NetworkResult.OnSuccess -> {
                    mView?.onAddSuccess("Member added successfully")
                }
                is NetworkResult.OnError -> {
                    mView?.showError(result.message)
                }
            }
            mView?.showLoading2(false)

        }
    }
}


