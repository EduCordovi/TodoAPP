package com.educordovi.todoapp.addtasks.domain

import com.educordovi.todoapp.addtasks.data.TaskRepository
import com.educordovi.todoapp.addtasks.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase  @Inject constructor(private val taskRepository: TaskRepository){
    operator fun invoke():Flow<List<TaskModel>>{
        return  taskRepository.tasks
    }
}