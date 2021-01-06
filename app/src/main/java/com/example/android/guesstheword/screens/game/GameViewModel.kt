package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel () {

    companion object {

        // Time when the game is over
        private const val DONE = 0L

        // Countdown time interval
        private const val ONE_SECOND = 1000L

        // Total time for the game
        private const val COUNTDOWN_TIME = 60000L

    }

    //Encapsulation
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished: LiveData<Boolean>
        get() = _eventGameFinished

    // Countdown time
    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    // The String version of the current time
    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    private val timer: CountDownTimer

    init {
        _word.value = ""

        _score.value = 0
    }


    private lateinit var wordList: MutableList<String>

    private fun resetWord() {
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

    init {
        Log.i("GameViewModel", "GameViewModel created!")
        resetWord()
        nextWord()

        // Creates a timer which triggers the end of the game when it finishes
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished / ONE_SECOND
            }

            override fun onFinish() {
                _currentTime.value = DONE
                onGameFinished()
            }
        }
        timer.start()
    }

        private fun nextWord() {
            if (wordList.isEmpty()) {
                resetWord()
            } else {
                //Select and remove a word from the list
                _word.value = wordList.removeAt(0)
            }
        }

        /** Methods for buttons presses **/
        fun onSkip() {
            _score.value = score.value?.minus(1)
            nextWord()
        }

        fun onCorrect() {
            _score.value = score.value?.plus(1)
            nextWord()
        }

        //For game finished
        fun onGameFinished() {
            _eventGameFinished.value = true
        }

        fun onGameFinishComplete() {
            _eventGameFinished.value = false
        }

        override fun onCleared() {
            super.onCleared()
            timer.cancel()
            Log.i("GameViewModel", "GameViewModel destroyed!")
        }
    }