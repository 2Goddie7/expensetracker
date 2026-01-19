package com.example.expensetracker.data.repository

import com.example.expensetracker.data.local.ExpenseDao
import com.example.expensetracker.data.local.ExpenseEntity
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val dao: ExpenseDao) {

    val todoLosGastos: Flow<List<ExpenseEntity>> = dao.obtenerTodos()

    val totalGeneral: Flow<Double?> = dao.totalGeneral()

    fun totalPorCategoria(Categoria: String): Flow<Double?> {
        return dao.totalPorCategoria(categoria)
    }

    suspend fun agregar(gasto: ExpenseEntity) {
        dao.insertar(gasto)
    }

    suspend fun actualizar(gasto: ExpenseEntity) {
        dao.actualizar(gasto)
    }

    suspend fun eliminar(gasto: ExpenseEntity) {
        dao.eliminar(gasto)
    }
}