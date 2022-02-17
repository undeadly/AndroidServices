package com.coryroy.servicesexample.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.coryroy.servicesexample.R
import com.coryroy.servicesexample.databinding.FragmentStartedServiceBinding
import com.coryroy.servicesexample.service.BoundMessengerCountingService
import com.coryroy.servicesexample.service.StartedCountingForegroundService
import com.coryroy.servicesexample.service.StartedCountingService
import com.coryroy.servicesexample.viewmodel.CountingViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class StartedForegroundServiceFragment : Fragment() {

    private var _binding: FragmentStartedServiceBinding? = null
    private val binding get() = _binding!!

    private val viewModel = CountingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartedServiceBinding.inflate(inflater, container, false)

        return binding.root

    }

    private fun updateButton(started : Boolean) {
        binding.buttonStartService.text =
            if (started) context?.getString(R.string.stop_foreground_service)
            else context?.getString(R.string.start_foreground_service)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        updateButton(StartedCountingForegroundService.started)
        attachButtonListener()
    }

    private fun attachButtonListener() {
        binding.buttonStartService.setOnClickListener {
            if (!StartedCountingForegroundService.started) {
                activity?.startForegroundService(Intent(activity, StartedCountingForegroundService::class.java))
                updateButton(true)
                true
            } else {
                activity?.stopService(Intent(activity, StartedCountingForegroundService::class.java))
                updateButton(false)
                false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}