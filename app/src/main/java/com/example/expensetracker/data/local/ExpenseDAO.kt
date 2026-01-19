package com.example.expensetracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.expensetracker.data.local.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao

interface ExpenseDao{

    //Gastos ordenados del mas reciente al mas antiguo
    @Query("SELECT * FROM gastos ORDER BY fecha DESC")
    fun obtenerTodos(): Flow<List<ExpenseEntity>>

    //Calcula el total gastado en una categoria especifica
    @Query("SELECT SUM(monto) FROM gastos WHERE categoria = :categoria")
    fun totalPorCategoria(categoria: String): Flow<Double?>

    // Calcula el total de todos los gastos
    @Query("SELECT SUM(monto) FROM gastos")
    fun totalGeneral(): Flow<Double?>

    // Insertar un nuevo gasto
    @Insert
    suspend fun insertar(gasto: ExpenseEntity)

    // Actualizar un gasto existente
    @Update
    suspend fun actualizar(gasto: ExpenseEntity)

    //Eliminar un gasto
    @Delete
    suspend fun eliminar(gasto: ExpenseEntity)
}