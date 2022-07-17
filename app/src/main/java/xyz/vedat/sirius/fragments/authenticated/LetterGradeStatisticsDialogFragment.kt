package xyz.vedat.sirius.fragments.authenticated

import android.content.ContentValues
import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import xyz.vedat.sirius.R
import xyz.vedat.sirius.defaultLogTag
import xyz.vedat.sirius.viewmodels.LetterGradeStatisticsViewModel

class LetterGradeStatisticsDialogFragment : BottomSheetDialogFragment() {
    private val viewModel: LetterGradeStatisticsViewModel by activityViewModels()

    private lateinit var loadingScreen: View
    private lateinit var mainContent: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_letter_grade_statistics_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BottomSheetBehavior.from(view.findViewById(R.id.letter_grade_statistics_bottom_sheet)).state =
            BottomSheetBehavior.STATE_EXPANDED

        loadingScreen = view.findViewById(R.id.loading_screen)
        mainContent = view.findViewById(R.id.letter_grade_statistics_graph_layout)

        view.findViewById<Button>(R.id.letter_grade_statistics_download_button).setOnClickListener {
            saveGraph(viewModel.uiState.value.let { it.items!![it.activeIndex!!] })
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (state.activeIndex == null || state.items?.get(state.activeIndex)?.image == null) {
                        mainContent.visibility = View.GONE
                        loadingScreen.visibility = View.VISIBLE
                    } else {
                        loadingScreen.visibility = View.GONE
                        mainContent.visibility = View.VISIBLE

                        val activeItem = state.items[state.activeIndex]
                        view.findViewById<ImageView>(R.id.letter_grade_statistics_graph_iw)
                            .setImageBitmap(activeItem.image)
                    }
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewModel.dismissActive()
        super.onDismiss(dialog)
    }

    override fun onCancel(dialog: DialogInterface) {
        viewModel.dismissActive()
        super.onCancel(dialog)
    }

    private fun saveGraph(item: LetterGradeStatisticsViewModel.Item) {
        val resolver = requireActivity().applicationContext.contentResolver

        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Downloads.EXTERNAL_CONTENT_URI
        }

        val imageDetails = ContentValues().apply {
            put(
                MediaStore.Downloads.DISPLAY_NAME,
                "${item.course.let { "${it.department}_${it.number}" }}_${item.semester.let { "${it.year}_${it.season}" }}_statistics.jpg"
            )
            put(MediaStore.Downloads.IS_PENDING, 1)
        }

        val imageContentUri = resolver.insert(imageCollection, imageDetails)

        if (imageContentUri == null) {
            Log.w(defaultLogTag, "Couldn't create image URI")
            return
        }

        resolver.openOutputStream(imageContentUri).use {
            item.image!!.compress(Bitmap.CompressFormat.JPEG, 90, it)
        }

        imageDetails.apply {
            clear()
            put(MediaStore.Downloads.IS_PENDING, 0)
        }
        resolver.update(imageContentUri, imageDetails, null, null)

        dismiss()
        Snackbar.make(
            requireActivity().findViewById(R.id.fragment_srs_hub),
            "Saved the graph to 'Downloads'.",
            Snackbar.LENGTH_SHORT
        ).show()
    }
}
