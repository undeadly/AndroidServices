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
import com.coryroy.servicesexample.service.StartedCountingService
import com.coryroy.servicesexample.viewmodel.CountingViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class StartedServiceFragment : Fragment() {

    private var _binding: FragmentStartedServiceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var started = false

    private val viewModel = CountingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartedServiceBinding.inflate(inflater, container, false)

        return binding.root

    }

    private fun updateButton() {
        binding.buttonStartService.text =
            if (started) context?.getString(R.string.stop_service)
            else context?.getString(R.string.start_service)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        attachButtonListener()
    }

    private fun attachButtonListener() {
        binding.buttonStartService.setOnClickListener {
            started = if (!started) {
                activity?.startService(Intent(activity, StartedCountingService::class.java))
                true
            } else {
                activity?.stopService(Intent(activity, StartedCountingService::class.java))
                false
            }
            updateButton()
        }

    }

    override fun onPause() {
        if (started)
            activity?.stopService(Intent(activity, StartedCountingService::class.java))
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}