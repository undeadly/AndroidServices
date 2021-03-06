package com.coryroy.servicesexample.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.coryroy.servicesexample.R
import com.coryroy.servicesexample.databinding.FragmentJobServiceBinding
import com.coryroy.servicesexample.service.JobCountingService
import com.coryroy.servicesexample.service.StartedCountingService
import com.coryroy.servicesexample.viewmodel.CountingViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class JobServiceFragment : Fragment() {

    private var _binding: FragmentJobServiceBinding? = null
    private val binding get() = _binding!!

    private val viewModel = CountingViewModel

    private var started = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobServiceBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        attachButtonListener()
    }

    private fun updateButton() {
        binding.buttonStartService.text =
            if (started) context?.getString(R.string.stop_service)
            else context?.getString(R.string.start_service)
    }

    private fun attachButtonListener() {
        binding.buttonStartService.setOnClickListener {
            started = if (!started) {
                context?.let {
                    JobCountingService().startJob(it)
                }
                true
            } else {
                context?.let {
                    JobCountingService().stopJob(it)
                }
                false
            }
            updateButton()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}