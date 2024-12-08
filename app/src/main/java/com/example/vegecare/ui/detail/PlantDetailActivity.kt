package com.example.vegecare.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vegecare.R
import com.example.vegecare.databinding.ActivityPlantDetailBinding
import com.example.vegecare.ui.detect.PlantDetectionActivity
import java.util.*

class PlantDetailActivity : AppCompatActivity() {

    // ViewBinding instance
    private lateinit var binding: ActivityPlantDetailBinding

    // SharedPreferences to store quest completion status
    private val sharedPreferences by lazy {
        getSharedPreferences("QuestPreferences", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout using ViewBinding
        binding = ActivityPlantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize CheckBoxes and Buttons using ViewBinding
        initQuestCheckBoxes()

        // Button click listener for disease detection
        binding.btnToDisease.setOnClickListener {
            val intent = Intent(this, PlantDetectionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initQuestCheckBoxes() {
        // Get current date
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        // Set listeners for quest checkboxes
        val checkBoxes = listOf(binding.checkBox1, binding.checkBox2, binding.checkBox3, binding.checkBox4)

        for (checkBox in checkBoxes) {
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Save the completion status in SharedPreferences
                    markQuestAsCompleted(checkBox)
                    // Show Toast for quest completion
                    Toast.makeText(this, "Anda telah menyelesaikan quest ini dan mendapat 15 leaf point", Toast.LENGTH_SHORT).show()
                } else {
                    // Prevent unchecking if quest is already completed
                    if (isQuestCompleted(checkBox)) {
                        checkBox.isChecked = true // Keep it checked
                    }
                }
            }
        }

        // Disable Checkboxes 3 and 4 if it's not the 15th day of the month
        if (currentDay != 15) {
            binding.checkBox3.isEnabled = false
            binding.checkBox4.isEnabled = false
        } else {
            binding.checkBox3.isEnabled = true
            binding.checkBox4.isEnabled = true
        }
    }

    // This function ensures that the quest status is stored
    private fun markQuestAsCompleted(checkBox: CheckBox) {
        val editor = sharedPreferences.edit()
        when (checkBox.id) {
            R.id.checkBox1 -> editor.putBoolean("quest_harian_done", true)
            R.id.checkBox2 -> editor.putBoolean("quest_harian_done", true)
            R.id.checkBox3 -> editor.putBoolean("quest_bulanan_done", true)
            R.id.checkBox4 -> editor.putBoolean("quest_bulanan_done", true)
        }
        editor.apply()
    }

    // This function checks if the quest has already been completed
    private fun isQuestCompleted(checkBox: CheckBox): Boolean {
        return when (checkBox.id) {
            R.id.checkBox1, R.id.checkBox2 -> sharedPreferences.getBoolean("quest_harian_done", false)
            R.id.checkBox3, R.id.checkBox4 -> sharedPreferences.getBoolean("quest_bulanan_done", false)
            else -> false
        }
    }
}
