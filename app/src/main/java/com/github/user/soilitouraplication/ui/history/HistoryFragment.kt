package com.github.user.soilitouraplication.ui.history

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.user.soilitouraplication.R
import com.github.user.soilitouraplication.api.History
import com.github.user.soilitouraplication.database.HistoryDao
import com.github.user.soilitouraplication.databinding.FragmentHistoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@Suppress("DEPRECATION")
class HistoryFragment : Fragment(), HistoryAdapter.OnItemClickListener {

    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var binding: FragmentHistoryBinding
    lateinit var historyAdapter: HistoryAdapter

    private lateinit var itemTouchHelper: ItemTouchHelper
    val historyList: MutableList<History> = mutableListOf()


    // Dependency Injection
    @Inject
    lateinit var historyDao: HistoryDao

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var isRefreshing = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val view = binding.root

        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            if (!isRefreshing) {
                fetchHistory()
            } else {
                swipeRefreshLayout.isRefreshing = false
            }
        }

        val recyclerView: RecyclerView = binding.history
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        historyAdapter = HistoryAdapter(this)
        recyclerView.adapter = historyAdapter

        lifecycleScope.launch {
            historyDao.getAllHistory().collect { historyList ->
                this@HistoryFragment.historyList.clear()
                this@HistoryFragment.historyList.addAll(historyList)
                historyAdapter.setData(historyList.toList())
            }
        }

        val itemSwipeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            private val paint = Paint()

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean,
            ) {
                val icon: Bitmap
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView: View = viewHolder.itemView
                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                    val width = height / 3
                    val cornerRadius = 20.dpToPx().toFloat()

                    val cardRect = RectF(
                        itemView.left.toFloat(),
                        itemView.top.toFloat() + 15.dpToPx().toFloat(),
                        itemView.left.toFloat() + 350.dpToPx().toFloat(),
                        itemView.top.toFloat() + 10.dpToPx().toFloat() + 150.dpToPx().toFloat()
                    )

                    paint.color = Color.parseColor("#B33A3A")

                    c.drawRoundRect(cardRect, cornerRadius, cornerRadius, paint)

                    if (dX > 0) {
                        val background = RectF(
                            itemView.left.toFloat(),
                            itemView.top.toFloat(),
                            dX,
                            itemView.bottom.toFloat()
                        )
                        c.drawRect(background, paint)

                        icon = BitmapFactory.decodeResource(
                            resources,
                            R.drawable.delete
                        )

                        val scaleFactor =
                            1 - (dX / itemView.width)
                        val scaledWidth = icon.width * scaleFactor
                        icon.height * scaleFactor
                        val iconDest = RectF(
                            itemView.left.toFloat() + width,
                            itemView.top.toFloat() + width,
                            itemView.left.toFloat() + width + scaledWidth,
                            itemView.bottom.toFloat() - width
                        )
                        c.drawBitmap(icon, null, iconDest, paint)
                    } else {
                        icon = BitmapFactory.decodeResource(
                            resources,
                            R.drawable.delete
                        )

                        val scaleFactor =
                            1 - (-dX / itemView.width)
                        val scaledWidth = icon.width * scaleFactor
                        icon.height * scaleFactor
                        val iconDest = RectF(
                            itemView.right.toFloat() - width - scaledWidth,
                            itemView.top.toFloat() + width,
                            itemView.right.toFloat() - width,
                            itemView.bottom.toFloat() - width
                        )
                        c.drawBitmap(icon, null, iconDest, paint)
                    }

                }
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            private fun Int.dpToPx(): Int {
                return (this * Resources.getSystem().displayMetrics.density).toInt()
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedItem = historyAdapter.getData()[position]

                onShowBackDialog(deletedItem.id)

            }
        }

        itemTouchHelper = ItemTouchHelper(itemSwipeCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        fetchHistory()

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchHistory() {
        if (!isRefreshing) {
            isRefreshing = true
            swipeRefreshLayout.isRefreshing = true

            try {
                historyViewModel.fetchHistory()
                historyViewModel.historyList.observe(viewLifecycleOwner) { historyList ->
                    this.historyList.clear()
                    this.historyList.addAll(historyList)

                    Log.d("WHYYYYY", "fetchHistory: $historyList")
                    historyAdapter.notifyDataSetChanged()

                    lifecycleScope.launch {
                        historyDao.deleteAllHistory()
                        historyList.forEach { history ->
                            historyDao.insertHistory(history)
                        }

                        this@HistoryFragment.historyList.clear()
                        this@HistoryFragment.historyList.addAll(historyList)
                        historyAdapter.setData(historyList.toList())
                    }
                }
            } catch (e: Exception) {
                Log.e("HistoryFragment", "Failed to fetch history", e)
            } finally {
                isRefreshing = false
                swipeRefreshLayout.isRefreshing = false
            }
        } else {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onShowBackDialog(id: String) {
        val alertDialogBuilder = AlertDialog.Builder(
            requireContext()
        )

        alertDialogBuilder
            .setMessage(R.string.delete_history_desc)
            .setCancelable(true)
            .setPositiveButton(
                R.string.yes
            ) { _, _ ->
                historyViewModel.deleteHistory(id)

                historyViewModel.isSuccessDelete.observe(viewLifecycleOwner) { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(
                            requireContext(),
                            R.string.delete_history_success,
                            Toast.LENGTH_SHORT
                        ).show()

                        fetchHistory()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            R.string.delete_history_failed,
                            Toast.LENGTH_SHORT
                        ).show()

                        fetchHistory()
                    }
                }
            }
            .setNegativeButton(
                R.string.no
            ) { dialog, _ ->
                dialog.cancel()
                historyAdapter.notifyDataSetChanged()
            }

        val alertDialog = alertDialogBuilder.create()

        alertDialog.show()
    }

    private fun startDetailHistoryActivity(history: History) {
        val intent = Intent(requireContext(), DetailHistory::class.java)

        lifecycleScope.launch {
            val selectedHistory = historyDao.getHistoryById(history.id)

            selectedHistory?.let {
                Log.d("DetailHistory", "Selected History: $selectedHistory")

                intent.putExtra("history", selectedHistory)
                startActivity(intent)
            }
        }
    }



    override fun onItemClick(history: History) {
        startDetailHistoryActivity(history)
    }
}
