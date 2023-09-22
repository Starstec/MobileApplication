package com.example.starstec.ui.activity.pss

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.starstec.databinding.PssItemBinding
import com.example.starstec.utils.PssQuestionsUtils

class PSSAdapter(private val questions: List<PssQuestionsUtils.Question>) : RecyclerView.Adapter<PSSAdapter.ViewHolder>() {
    // A list to store the selected answer's points for each question
    private val selectedPoints = MutableList(questions.size) { -1 } // Initialize with -1 for all questions


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PssItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question)

        // Set up RadioButton click listener to update the selected points
        holder.binding.answerGroup.setOnCheckedChangeListener { _, checkedId ->
            val points = question.answers[holder.binding.answerGroup.indexOfChild(holder.binding.root.findViewById(checkedId))].second
            selectedPoints[position] = points
        }
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    fun getSelectedPoints(): List<Int> {
        // Filter the selectedPoints list to get only the points for answered questions
        return selectedPoints.filter { it != -1 }
    }


    fun getTotalPoints(): Int {
        // Sum up all the selected points to get the total stress score
        return selectedPoints.sum()
    }

    fun isAllQuestionsAnswered(): Boolean {
        // Check if there are any unanswered questions (i.e., any question with value -1)
        return !selectedPoints.any { it == -1 }
    }

    inner class ViewHolder(val binding: PssItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(question: PssQuestionsUtils.Question) {
            binding.questionTextView.text = question.questionText

            // Initialize selectedPoints list with 0 for each question
            if (selectedPoints.size <= adapterPosition) {
                selectedPoints.add(0)
            }

            // Set the RadioButton text for each answer
            for ((index, answer) in question.answers.withIndex()) {
                val radioButton = binding.answerGroup.getChildAt(index) as RadioButton
                radioButton.text = answer.first
            }
        }
    }
}
