/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {
    //initializing view model for game fragment
    private lateinit var viewModel: GameViewModel

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )

        Log.i("GameFragment", "Called ViewModelProvider.get")
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        //Using LiveData Observer to update score and word text and game finished variable
        viewModel.score.observe(viewLifecycleOwner, Observer{
            binding.scoreText.text = it.toString()
        })

        viewModel.word.observe(viewLifecycleOwner, Observer {
            binding.wordText.text = it
        })

        viewModel.eventGameFinished.observe(viewLifecycleOwner, Observer<Boolean> {
            if (it) gameFinished()
        })

        binding.correctButton.setOnClickListener { onCorrect() }
        binding.skipButton.setOnClickListener { onSkip() }
        //implemented end game button
        binding.endGameButton.setOnClickListener{ view : View ->
            Toast.makeText(activity, "Game has just finished", Toast.LENGTH_SHORT).show()
            view.findNavController().navigate(GameFragmentDirections.actionGameToScore(viewModel.score.value ?: 0))
        }

        return binding.root
    }

    private fun gameFinished() {
        Toast.makeText(activity, "Game has just finished", Toast.LENGTH_SHORT).show()
        findNavController().navigate(GameFragmentDirections.actionGameToScore(viewModel.score.value ?: 0))
        viewModel.onGameFinishComplete()
    }

    /** Methods for buttons presses **/

    private fun onSkip() {
        viewModel.onSkip()
    }

    private fun onCorrect() {
        viewModel.onCorrect()
    }
}
