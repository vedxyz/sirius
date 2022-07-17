package xyz.vedat.sirius.fragments.authenticated

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.launch
import xyz.vedat.sirius.R
import xyz.vedat.sirius.adapters.ExamsAdapter
import xyz.vedat.sirius.viewmodels.ExamsViewModel

class ExamsFragment : Fragment(R.layout.fragment_exams) {
    private val viewModel: ExamsViewModel by activityViewModels()

    private lateinit var loadingScreen: View
    private lateinit var mainContent: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.uiState.value.exams == null)
            viewModel.fetch()

        loadingScreen = view.findViewById(R.id.loading_screen)
        mainContent = view.findViewById(R.id.exams_swipelayout)
        recyclerView = view.findViewById(R.id.exams_recyclerview)
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))

        val adapter = ExamsAdapter()
        recyclerView.adapter = adapter

        mainContent.setOnRefreshListener {
            viewModel.fetch()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    if (it.isLoading) {
                        if (it.exams == null) {
                            mainContent.visibility = View.GONE
                            loadingScreen.visibility = View.VISIBLE
                        }
                    } else {
                        loadingScreen.visibility = View.GONE
                        mainContent.visibility = View.VISIBLE
                        mainContent.isRefreshing = false

                        adapter.submitList(it.exams)
                    }
                }
            }
        }
    }
}
