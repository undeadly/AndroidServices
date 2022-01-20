package com.coryroy.servicesexample.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.coryroy.servicesexample.R
import com.coryroy.servicesexample.databinding.FragmentStartedServiceBinding
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        binding.buttonStartService.setOnClickListener {
            started = if (!started) {
                activity?.startService(Intent(activity, StartedCountingService::class.java))
                binding.buttonStartService.setText(R.string.stop_service)
                true
            } else {
                activity?.stopService(Intent(activity, StartedCountingService::class.java))
                binding.buttonStartService.setText(R.string.start_service)
                false
            }

        }
    }

    override fun onPause() {
        activity?.stopService(Intent(activity, StartedCountingService::class.java))
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}