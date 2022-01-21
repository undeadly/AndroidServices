package com.coryroy.servicesexample.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.coryroy.servicesexample.R
import com.coryroy.servicesexample.databinding.FragmentBoundServiceBinding
import com.coryroy.servicesexample.service.BoundCountingService
import com.coryroy.servicesexample.service.BoundMessengerCountingService.Companion.MSG_START
import com.coryroy.servicesexample.service.BoundMessengerCountingService.Companion.MSG_STOP
import com.coryroy.servicesexample.service.StartedCountingService
import com.coryroy.servicesexample.viewmodel.CountingViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class BoundMessengerServiceFragment : Fragment() {

    private var _binding: FragmentBoundServiceBinding? = null
    private var boundService: Messenger? = null
    private var isBound : Boolean = false

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var started = false

    private val viewModel = CountingViewModel

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            boundService = Messenger(service)
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            boundService = null
            isBound = false
        }
    }

    override fun onAttach(context: Context) {
        Intent(context, BoundCountingService::class.java).also { intent -> context.bindService(intent, connection, Context.BIND_AUTO_CREATE) }
        super.onAttach(context)
    }

    override fun onStop() {
        if (isBound)
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
                    boundService?.send(Message.obtain(null, MSG_START))
                    binding.buttonStartService.setText(R.string.stop_service)
                    true
                } else {
                    boundService?.send(Message.obtain(null, MSG_STOP))
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