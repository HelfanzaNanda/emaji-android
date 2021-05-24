package com.example.emaji.ui.main.file

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.emaji.R
import com.example.emaji.models.File
import com.example.emaji.utils.Constants
import com.example.emaji.utils.ext.gone
import com.example.emaji.utils.ext.showToast
import com.example.emaji.utils.ext.visible
import kotlinx.android.synthetic.main.fragment_file.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FileFragment : Fragment(R.layout.fragment_file){
    private val fileViewModel : FileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observe()
    }

    private fun setUpRecyclerView() {
        requireView().recycler_view.apply {
            adapter = FileAdapter(mutableListOf(), requireActivity())
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun observe() {
        observeState()
        observeFiles()
    }

    private fun observeState() = fileViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUiState(it) })
    private fun observeFiles() = fileViewModel.listenToFiles().observe(viewLifecycleOwner, Observer { handleFiles(it) })
    private fun fetchFiles() = fileViewModel.fetchHistories(Constants.getToken(requireActivity()))

    private fun handleFiles(list: List<File>?) {
        list?.let {
            requireView().recycler_view.adapter?.let { adapter ->
                if (adapter is FileAdapter) adapter.updateList(it)
            }
        }
    }

    private fun handleUiState(state: FileState?) {
        state?.let {
            when(it){
                is FileState.Loading -> handleLoading(it.state)
                is FileState.ShowToast -> requireActivity().showToast(it.message)
            }
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state) requireView().loading.visible() else requireView().loading.gone()
    }

    override fun onResume() {
        super.onResume()
        fetchFiles()
    }
}