package com.example.emaji.ui.main.history

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.emaji.R
import com.example.emaji.models.History
import com.example.emaji.ui.cycle.CycleActivity
import com.example.emaji.utils.Constants
import com.example.emaji.utils.ext.gone
import com.example.emaji.utils.ext.showToast
import com.example.emaji.utils.ext.visible
import kotlinx.android.synthetic.main.fragment_history.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment(R.layout.fragment_history){

    private val historyViewModel : HistoryViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observe()
    }

    private fun setUpRecyclerView(){
        requireView().recycler_view.apply {
            adapter = HistoryAdapter(mutableListOf(), requireActivity())
            layoutManager = GridLayoutManager(requireActivity(), 3)
        }
    }

    private fun observe(){
        observeState()
        observeTools()
    }

    private fun observeState() = historyViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUiState(it) })
    private fun observeTools() = historyViewModel.listenToHistories().observe(viewLifecycleOwner, Observer { handleHistories(it) })

    private fun handleHistories(list: List<History>?) {
        list?.let {
            requireView().recycler_view.adapter?.let { adapter ->
                if (adapter is HistoryAdapter){
                    adapter.updateList(it)
                }
            }
        }
    }

    private fun handleUiState(state: HistoryState?) {
        state?.let {
            when(it){
                is HistoryState.Loading -> handleLoading(it.state)
                is HistoryState.ShowToast -> requireActivity().showToast(it.message)
            }
        }
    }

    private fun handleSuccessScan(toolId : Int) {
        startActivity(Intent(requireActivity(), CycleActivity::class.java).apply {
            putExtra("TOOL_ID", toolId)
        })
    }

    private fun handleLoading(state: Boolean) {
        if (state)requireView().loading.visible() else requireView().loading.gone()
    }

    private fun fetchHistories() = historyViewModel.fetchHistories(Constants.getToken(requireActivity()))

    override fun onResume() {
        super.onResume()
        fetchHistories()
    }

}