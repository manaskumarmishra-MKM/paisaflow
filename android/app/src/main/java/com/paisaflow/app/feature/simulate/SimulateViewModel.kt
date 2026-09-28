package com.paisaflow.app.feature.simulate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paisaflow.app.core.mock.DemoSimulation
import com.paisaflow.app.core.model.LoanSimRequest
import com.paisaflow.app.core.model.LoanSimResponse
import com.paisaflow.app.core.network.ApiClient
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SimulateUiState(
    val request: LoanSimRequest = LoanSimRequest(),
    val result: LoanSimResponse = DemoSimulation.nineLakh,
    val loading: Boolean = false,
    val usingSampleData: Boolean = true,
)

/** Debounced slider → /simulation/loan. Falls back to sample result when offline. */
class SimulateViewModel : ViewModel() {
    private val _state = MutableStateFlow(SimulateUiState())
    val state: StateFlow<SimulateUiState> = _state
    private var job: Job? = null

    fun setLoan(amount: Int) = update { it.copy(loan = amount) }
    fun setUnits(units: Int) = update { it.copy(newUnits = units) }
    fun setTenure(months: Int) = update { it.copy(tenureMonths = months) }

    /** "Test Safe Plan" — re-run with the engine's safe limit. */
    fun applySafePlan() {
        val sp = _state.value.result.safePlan ?: return
        update { it.copy(loan = sp.loan, newUnits = sp.units) }
    }

    private fun update(f: (LoanSimRequest) -> LoanSimRequest) {
        _state.update { it.copy(request = f(it.request), loading = true) }
        job?.cancel()
        job = viewModelScope.launch {
            delay(350) // debounce slider drags
            val req = _state.value.request
            val res = runCatching { ApiClient.api.simulateLoan(req) }
            _state.update {
                it.copy(
                    result = res.getOrElse { DemoSimulation.nineLakh },
                    usingSampleData = res.isFailure,
                    loading = false,
                )
            }
        }
    }
}
