package com.coryroy.servicesexample.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.coryroy.servicesexample.R
import com.coryroy.servicesexample.databinding.FragmentBoundServiceBinding
import com.coryroy.servicesexample.service.BoundAidlCountingService
import com.coryroy.servicesexample.service.BoundCountingService
import com.coryroy.servicesexample.service.ICountingAidlInterface
import com.coryroy.servicesexample.service.StartedCountingService
import com.coryroy.servicesexample.viewmodel.CountingViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class BoundAidlServiceFragment : Fragment() {

    private var _binding: FragmentBoundServiceBinding? = null
    private lateinit var boundService: ICountingAidlInterface
    private var isBound : Boolean = false

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var started = false

    private val viewModel = CountingViewModel

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            boundService = ICountingAidlInterface.Stub.asInterface(service)
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onAttach(context: Context) {
        Intent(context, BoundAidlCountingService::class.java).also { intent -> context.bindService(intent, connection, Context.BIND_AUTO_CREATE) }
        super.onAttach(context)
    }

    override fun onStop() {
        activity?.unbindService(connection)
        isBound = false
        super.onStop()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBoundServiceBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        binding.buttonStartService.setOnClickListener {
            if (isBound) {
                started = if (!started) {
                    boundService.startCounting()
                    binding.buttonStartService.setText(R.string.stop_service)
                    true
                } else {
                    boundService.stopCounting()
                    binding.buttonStartService.setText(R.string.start_service)
                    false
                }
            } else {
                Toast.makeText(activity, "No service connection", Toast.LENGTH_LONG).show()
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