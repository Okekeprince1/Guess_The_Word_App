package com.example.android.guesstheword.screens.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel () {

    //Encapsulation
    private val _word = MutableLiveData<String>()
    val word : LiveData<String>
        get() = _word

    private val _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score

    private val _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished : LiveData<Boolean>
        get() = _eventGameFinished

    init {
        _word.value = ""

        _score.value = 0
    }


    private lateinit var wordList : MutableList<String>

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
    }

    private fun nextWord() {
        if (wordList.isEmpty()) {
            onGameFinished()
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
    private fun onGameFinished() {
        _eventGameFinished.value = true
    }

    fun onGameFinishComplete() {
        _eventGameFinished.value = false
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }
}