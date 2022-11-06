package xyz.vedat.sirius.fragments.authenticated

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.launch
import xyz.vedat.sirius.R
import xyz.vedat.sirius.adapters.LetterGradeStatisticsAdapter
import xyz.vedat.sirius.defaultLogTag
import xyz.vedat.sirius.viewmodels.LetterGradeStatisticsViewModel

class LetterGradeStatisticsFragment : Fragment(R.layout.fragment_letter_grade_statistics) {
    private val viewModel: LetterGradeStatisticsViewModel by activityViewModels()

    private lateinit var loadingScreen: View
    private lateinit var mainContent: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.uiState.value.items == null)
            viewModel.fetch()

        loadingScreen = view.findViewById(R.id.loading_screen)
        mainContent = view.findViewById(R.id.letter_grade_statistics_swipelayout)
        recyclerView = view.findViewById(R.id.letter_grade_statistics_recyclerview)
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))

        val adapter = LetterGradeStatisticsAdapter { onItemClicked(it) }
        recyclerView.adapter = adapter

        mainContent.setOnRefreshListener {
            viewModel.fetch()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    if (it.isLoading) {
                        if (it.items == null) {
                            mainContent.visibility = View.GONE
                            loadingScreen.visibility = View.VISIBLE
                        }
                    } else {
                        loadingScreen.visibility = View.GONE
                        mainContent.visibility = View.VISIBLE
                        mainContent.isRefreshing = false

                        adapter.submitList(it.items)
                        // FIXME: Empty lists should display proper message (this is a problem across the whole app)

                        if (it.shouldNavigate) {
                            Log.v(defaultLogTag, "Navigating to LGS dialog")
                            findNavController().navigate(R.id.letter_grade_statistics_dialog_navfragment)
                            viewModel.navigationConsumed()
                        }
                    }
                }
            }
        }
    }

    private fun onItemClicked(item: LetterGradeStatisticsViewModel.Item) {
        Log.v(defaultLogTag, "LGS for '${item.course.let { "${it.department} ${it.number}" }}' clicked")
        item.fetch()
    }
}
