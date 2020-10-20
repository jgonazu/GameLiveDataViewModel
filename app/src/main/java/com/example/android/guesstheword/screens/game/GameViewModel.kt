package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

/**
 * GameViewModel
 *
 * @author (c) 2020, jgonazu
 */
private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

// This is the time when the phone will start buzzing each second
private const val COUNTDOWN_PANIC_SECONDS = 10L

class GameViewModel: ViewModel() {
    // The current word
    // internal
    private val _word = MutableLiveData<String>()
    //external
    val word: LiveData<String>
        get() = _word

    // The current score
    // internal
    private val _score = MutableLiveData<Int>()
    //external
    val score: LiveData<Int>
        get() = _score

    // internal
    private val _gameFinished = MutableLiveData<Boolean>()
    //external
    val gameFinished: LiveData<Boolean>
        get() = _gameFinished

    // The current time
    // internal
    private val _currentTime = MutableLiveData<Long>()
    //external
    val currentTime: LiveData<Long>
        get() = _currentTime

    val currentTimeString = Transformations.map(currentTime, { time ->
        DateUtils.formatElapsedTime(time)
    })

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>


    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 20000L
    }

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    private var timer: CountDownTimer

    private val _buzzType = MutableLiveData<BuzzType>()
    //external
    val buzzType: LiveData<BuzzType>
        get() = _buzzType

    init {
        Log.i("EIIII", "GameViewModel created!")
        _gameFinished.value = false;
        resetList()
        nextWord()
        _score.value = 0
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
                if (millisUntilFinished / ONE_SECOND <= COUNTDOWN_PANIC_SECONDS) {
                    _buzzType.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                _currentTime.value = DONE
                _buzzType.value = BuzzType.GAME_OVER
                _gameFinished.value = true
            }
        }

        timer.start()
    }
    override fun onCleared() {
        super.onCleared()
        Log.i("EIIII", "GameViewModel destroyed!")
        timer.cancel()
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        _buzzType.value = BuzzType.CORRECT
        nextWord()
    }

    fun onGameFinish() {
        _gameFinished.value = false
    }

    fun onBuzzComplete() {
        _buzzType.value = BuzzType.CORRECT
    }
}