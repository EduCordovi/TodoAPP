package com.educordovi.todoapp.addtasks.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educordovi.todoapp.addtasks.domain.AddTaskUseCase
import com.educordovi.todoapp.addtasks.domain.DeleteTaskUseCase
import com.educordovi.todoapp.addtasks.domain.GetTasksUseCase
import com.educordovi.todoapp.addtasks.domain.UpdateTaskUseCase
import com.educordovi.todoapp.addtasks.ui.TasksUiState.*
import javax.inject.Inject
import com.educordovi.todoapp.addtasks.ui.model.TaskModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
@HiltViewModel
class TasksViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    getTasksUseCase: GetTasksUseCase
):ViewModel() {

    val uiState: StateFlow<TasksUiState> = getTasksUseCase().map(::Success)
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog:LiveData<Boolean> = _showDialog

    //private val _tasks = mutableStateListOf<TaskModel>()
    //val task:List<TaskModel> = _tasks

    fun onDialogClose(){
        _showDialog.value = false
    }

    fun onTasksCreated(task: String) {
        _showDialog.value = false
        //_tasks.add(TaskModel(task = task))

        viewModelScope.launch {
            addTaskUseCase(TaskModel(task = task))
        }
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onCheckBoxSelected(taskModel: TaskModel) {
        //Actualizar Check
        //val index = _tasks.indexOf(taskModel)
        //_tasks[index] = _tasks[index].let {
        //    it.copy(selected = !it.selected)
        //}
        viewModelScope.launch {
            updateTaskUseCase(taskModel.copy(selected = !taskModel.selected))
        }
    }

    fun onItemRemove(taskModel: TaskModel) {
        // Borrar Item
        //val task = _tasks.find { it.id == taskModel.id }
        //_tasks.remove(task)
        viewModelScope.launch {
            deleteTaskUseCase(taskModel)
        }
    }
}