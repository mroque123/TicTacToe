package com.example.tictactoe

import kotlin.random.Random

class GameEngine {

    private val wins = arrayOf(
        intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
    )

    fun randomMove(board: Array<String>): Int {
        val moves = board.indices.filter { board[it].isEmpty() }
        return if (moves.isEmpty()) -1 else moves.random()
    }

    fun mediumMove(board: Array<String>, ai: String, human: String): Int {
        for (i in board.indices) {
            if (board[i].isEmpty()) {
                board[i] = ai
                if (isWinner(board, ai)) { board[i] = ""; return i }
                board[i] = ""
            }
        }
        for (i in board.indices) {
            if (board[i].isEmpty()) {
                board[i] = human
                if (isWinner(board, human)) { board[i] = ""; return i }
                board[i] = ""
            }
        }
        return randomMove(board)
    }

    fun bestMove(board: Array<String>, ai: String, human: String): Int {
        val move = minimax(board, ai, human, ai)
        return if (move.index >= 0) move.index else randomMove(board)
    }

    data class Move(val index: Int, val score: Int)

    private fun minimax(board: Array<String>, ai: String, human: String, player: String): Move {
        if (isWinner(board, ai)) return Move(-1, 10)
        if (isWinner(board, human)) return Move(-1, -10)
        if (board.all { it.isNotEmpty() }) return Move(-1, 0)

        val moves = mutableListOf<Move>()
        for (i in board.indices) {
            if (board[i].isEmpty()) {
                board[i] = player
                val result = minimax(board, ai, human, if (player == ai) human else ai)
                moves.add(Move(i, result.score))
                board[i] = ""
            }
        }

        return if (player == ai) {
            moves.maxByOrNull { it.score }!!
        } else {
            moves.minByOrNull { it.score }!!
        }
    }

    private fun isWinner(board: Array<String>, player: String): Boolean {
        for (w in wins) {
            if (board[w[0]] == player && board[w[1]] == player && board[w[2]] == player) return true
        }
        return false
    }
}
