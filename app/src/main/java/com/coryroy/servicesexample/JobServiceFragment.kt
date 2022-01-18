package com.coryroy.servicesexample

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.coryroy.servicesexample.databinding.FragmentJobServiceBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class JobServiceFragment : Fragment() {

    private var _binding: FragmentJobServiceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var started = false

    private val viewModel = CountingViewModel

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

        binding.buttonStartService.setOnClickListener {
            started = if (!started) {
                context?.let {
                    JobCountingService.startJob(it)
                }

                binding.buttonStartService.setText(R.string.stop_service)
                true
            } else {
                context?.let{
                    JobCountingService.stopJob(it)
                }
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