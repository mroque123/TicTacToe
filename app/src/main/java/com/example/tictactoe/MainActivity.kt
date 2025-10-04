package com.example.tictactoe

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nameInput = findViewById<EditText>(R.id.editName)
        val difficultySpinner = findViewById<Spinner>(R.id.spinnerDifficulty)
        val opponentRadio = findViewById<RadioGroup>(R.id.radioGroupOpponent)
        val startButton = findViewById<Button>(R.id.buttonStart)

        ArrayAdapter.createFromResource(
            this,
            R.array.difficulties,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            difficultySpinner.adapter = adapter
        }

        startButton.setOnClickListener {
            val name = nameInput.text.toString().ifEmpty { "Jugador" }
            val difficulty = difficultySpinner.selectedItem.toString()
            val opponentId = opponentRadio.checkedRadioButtonId
            val opponent = if (opponentId == R.id.radioAI) "AI" else "HUMAN"

            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra("PLAYER_NAME", name)
                putExtra("DIFFICULTY", difficulty)
                putExtra("OPPONENT", opponent)
            }
            startActivity(intent)
        }
    }
}
