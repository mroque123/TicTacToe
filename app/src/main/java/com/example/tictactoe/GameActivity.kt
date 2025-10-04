package com.example.tictactoe

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private lateinit var cells: Array<Button>
    private lateinit var statusText: TextView
    private lateinit var resetButton: Button
    private lateinit var backButton: Button

    private var board = Array(9) { "" }
    private var currentPlayer = "X"
    private var playerName = "Jugador"
    private var opponent = "AI"
    private var difficulty = "Easy"
    private val engine = GameEngine()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        playerName = intent.getStringExtra("PLAYER_NAME") ?: "Jugador"
        difficulty = intent.getStringExtra("DIFFICULTY") ?: "Easy"
        opponent = intent.getStringExtra("OPPONENT") ?: "AI"

        statusText = findViewById(R.id.textStatus)
        resetButton = findViewById(R.id.buttonReset)
        backButton = findViewById(R.id.buttonBack)

        cells = Array(9) { i ->
            val btnId = resources.getIdentifier("cell_" + i, "id", packageName)
            findViewById<Button>(btnId).also { btn ->
                btn.setOnClickListener { onCellClicked(i) }
            }
        }

        resetButton.setOnClickListener { resetGame() }
        backButton.setOnClickListener { finish() }

        updateStatus()
    }

    private fun onCellClicked(index: Int) {
        if (board[index].isNotEmpty()) return

        if (opponent == "AI") {
            if (currentPlayer != "X") return
            makeMove(index, "X")

            if (!isGameOver()) {
                val aiIndex = computeAIMove()
                if (aiIndex >= 0) makeMove(aiIndex, "O")
            }
        } else {
            makeMove(index, currentPlayer)
        }
    }

    private fun makeMove(index: Int, player: String) {
        if (board[index].isNotEmpty()) return
        board[index] = player
        cells[index].text = player
        cells[index].isEnabled = false

        if (checkWinner(player)) {
            showEndDialog("$player gana!")
            return
        }

        if (board.all { it.isNotEmpty() }) {
            showEndDialog("Empate")
            return
        }

        currentPlayer = if (currentPlayer == "X") "O" else "X"
        updateStatus()
    }

    private fun computeAIMove(): Int {
        return when (difficulty) {
            "Easy" -> engine.randomMove(board)
            "Medium" -> engine.mediumMove(board, "O", "X")
            "Hard" -> engine.bestMove(board, "O", "X")
            else -> engine.randomMove(board)
        }
    }

    private fun checkWinner(player: String): Boolean {
        val wins = arrayOf(
            intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
        )
        for (w in wins) {
            if (board[w[0]] == player && board[w[1]] == player && board[w[2]] == player) return true
        }
        return false
    }

    private fun isGameOver(): Boolean {
        return checkWinner("X") || checkWinner("O") || board.all { it.isNotEmpty() }
    }

    private fun resetGame() {
        board = Array(9) { "" }
        for (i in cells.indices) {
            cells[i].text = ""
            cells[i].isEnabled = true
        }
        currentPlayer = "X"
        updateStatus()
    }

    private fun updateStatus() {
        val whose = if (opponent == "AI") playerName else "Jugador"
        statusText.text = "Turno: $currentPlayer  — $whose"
    }

    private fun showEndDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Juego terminado")
            .setMessage(message)
            .setPositiveButton("Reiniciar") { _, _ -> resetGame() }
            .setNegativeButton("Salir") { _, _ -> finish() }
            .show()
    }
}
