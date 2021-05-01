package com.example.grupee.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.grupee.R
import com.example.grupee.adapters.SongAdapter
import com.example.grupee.other.Constants.MEDIA_LIKED_SONGS_ID
import com.example.grupee.other.Status
import com.example.grupee.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_liked_songs.*
import javax.inject.Inject

@AndroidEntryPoint
class LikedSongsFragment : Fragment(R.layout.fragment_liked_songs) {

    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var songAdapter: SongAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        setupRecyclerView()
        subscribeToObservers()

        if (mainViewModel.parentId != MEDIA_LIKED_SONGS_ID) {
            mainViewModel.unsubscribeMusicService()
            mainViewModel.subscribeMusicService(MEDIA_LIKED_SONGS_ID)
        }

        songAdapter.setItemClickListener {
            mainViewModel.playOrToggleSong(it)
        }
    }

    private fun setupRecyclerView() {
        rvLikedSongs.adapter = songAdapter
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    likedSongsProgressBar.isVisible = false
                    result.data?.let { songs ->
                        songAdapter.songs = songs
                    }
                }
                Status.ERROR -> Unit
                Status.LOADING -> likedSongsProgressBar.isVisible = true
            }
        }
    }
}