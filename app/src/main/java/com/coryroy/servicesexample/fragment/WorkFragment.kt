package com.coryroy.servicesexample.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.coryroy.servicesexample.R
import com.coryroy.servicesexample.databinding.FragmentWorkBinding
import com.coryroy.servicesexample.service.JobCountingService
import com.coryroy.servicesexample.viewmodel.CountingViewModel
import com.coryroy.servicesexample.work.CountingWorkManager

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class WorkFragment : Fragment() {

    private var _binding: FragmentWorkBinding? = null
    private val binding get() = _binding!!

    private val viewModel = CountingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        attachButtonListeners()
    }

    private fun attachButtonListeners() {
        binding.buttonStartWork.setOnClickListener {
                context?.let { c ->
                    CountingWorkManager.startCounting(c, 10)
                }
        }
        binding.buttonStopWork.setOnClickListener {
            context?.let { c ->
                CountingWorkManager.stopCounting(c)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}