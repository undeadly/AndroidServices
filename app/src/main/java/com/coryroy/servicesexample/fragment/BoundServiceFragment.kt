package com.coryroy.servicesexample.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.coryroy.servicesexample.R
import com.coryroy.servicesexample.databinding.FragmentBoundServiceBinding
import com.coryroy.servicesexample.service.BoundCountingService
import com.coryroy.servicesexample.service.StartedCountingService
import com.coryroy.servicesexample.viewmodel.CountingViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class BoundServiceFragment : Fragment() {

    private val TAG = "BoundSvc"

    private var _binding: FragmentBoundServiceBinding? = null


    private val binding get() = _binding!!


    private val viewModel = CountingViewModel

    private lateinit var boundService: BoundCountingService
    private var isBound : Boolean = false
    private var started = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            boundService = (service as BoundCountingService.CountingBinder).getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) { isBound = false }
    }

    override fun onAttach(context: Context) {
        Intent(context, BoundCountingService::class.java)
            .also { intent -> context.bindService(intent, connection, Context.BIND_AUTO_CREATE) }
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        updateButton()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBoundServiceBinding.inflate(inflater, container, false)

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

        binding.buttonStartService.setOnClickListener {
            if (isBound) {
                started = if (!started) {
                    boundService.startCounting()
                    true
                } else {
                    boundService.stopCounting()
                    false
                }
            } else {
                Toast.makeText(activity, "No service connection", Toast.LENGTH_LONG).show()
            }
            updateButton()
        }
    }

    override fun onDestroy() {
        if (isBound) {
            activity?.stopService(Intent(activity, StartedCountingService::class.java))
            activity?.unbindService(connection)
        }
        isBound = false
        super.onDestroy()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()

    }
}