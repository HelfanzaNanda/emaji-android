package com.example.emaji.ui.main.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.emaji.R
import com.example.emaji.models.Tool
import com.example.emaji.ui.cycle.CycleActivity
import com.example.emaji.ui.scanner.ScannerActivity
import com.example.emaji.utils.Constants
import com.example.emaji.utils.ext.gone
import com.example.emaji.utils.ext.showToast
import com.example.emaji.utils.ext.visible
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(R.layout.fragment_home), HomeListener {
    private val homeViewModel : HomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observe()
    }

    private fun setUpRecyclerView(){
        requireView().recycler_view.apply {
            adapter = HomeAdapter(mutableListOf(), this@HomeFragment)
            layoutManager = GridLayoutManager(requireActivity(), 3)
        }
    }

    private fun observe(){
        observeState()
        observeTools()
    }

    private fun observeState() = homeViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUiState(it) })
    private fun observeTools() = homeViewModel.listenToTools().observe(viewLifecycleOwner, Observer { handleTools(it) })

    private fun handleTools(list: List<Tool>?) {
        list?.let {
            requireView().recycler_view.adapter?.let { adapter ->
                if (adapter is HomeAdapter){
                    adapter.updateList(it)
                }
            }
        }
    }

    private fun handleUiState(state: HomeState?) {
        state?.let {
            when(it){
                is HomeState.Loading -> handleLoading(it.state)
                is HomeState.ShowToast -> requireActivity().showToast(it.message)
                is HomeState.SuccessScan -> handleSuccessScan(it.toolId)
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

    private fun getTools() = homeViewModel.getTools(Constants.getToken(requireActivity()))

    override fun onResume() {
        super.onResume()
        getTools()
    }

    override fun click(tool: Tool) {
        startActivityForResult(Intent(requireActivity(), ScannerActivity::class.java), 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == Activity.RESULT_OK && data != null){
            val scanResut = data.getStringExtra("CODE")
            scanResut?.let {
                homeViewModel.validatedTool(Constants.getToken(requireActivity()), it)
            }
        }
    }


}