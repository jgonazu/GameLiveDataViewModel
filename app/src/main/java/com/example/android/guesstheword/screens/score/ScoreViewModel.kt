package com.example.android.guesstheword.screens.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ScoreViewModel
 *
 * @author (c) 2020, jgonazu
 */
class ScoreViewModel(finalScore: Int): ViewModel() {
    // The current score
    // internal
    private val _score = MutableLiveData<Int>()
    //external
    val score: LiveData<Int>
        get() = _score

    // internal
    private val _playAgain = MutableLiveData<Boolean>()
    //external
    val playAgain: LiveData<Boolean>
        get() = _playAgain

    init {
        _score.value = finalScore
        _playAgain.value = false;
    }

    fun onPlayAgain(){
        _playAgain.value = true
    }

    fun onPlayAgainComplete(){
        _playAgain.value = false
    }
}