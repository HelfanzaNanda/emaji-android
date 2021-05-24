package com.example.emaji.ui.main.file

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emaji.models.File
import com.example.emaji.repositories.FileRepository
import com.example.emaji.utils.ArrayResponse
import com.example.emaji.utils.SingleLiveEvent

class FileViewModel (private val fileRepository: FileRepository) : ViewModel(){
    private val state : SingleLiveEvent<FileState> = SingleLiveEvent()
    private val files = MutableLiveData<List<File>>()

    private fun isLoading(b : Boolean){ state.value = FileState.Loading(b) }
    private fun toast(m : String){ state.value = FileState.ShowToast(m) }

    fun fetchHistories(token : String){
        isLoading(true)
        fileRepository.fetchFiles(token, object : ArrayResponse<File>{
            override fun onSuccess(datas: List<File>?) {
                isLoading(false)
                datas?.let { files.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToFiles() = files
}

sealed class FileState{
    data class Loading (var state : Boolean = false) : FileState()
    data class ShowToast(var message : String) : FileState()
}