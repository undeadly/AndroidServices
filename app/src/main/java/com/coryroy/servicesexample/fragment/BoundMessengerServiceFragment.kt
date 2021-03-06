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
import com.coryroy.servicesexample.service.BoundMessengerCountingService
import com.coryroy.servicesexample.service.BoundMessengerCountingService.Companion.MSG_START
import com.coryroy.servicesexample.service.BoundMessengerCountingService.Companion.MSG_STOP
import com.coryroy.servicesexample.service.StartedCountingService
import com.coryroy.servicesexample.viewmodel.CountingViewModel

class BoundMessengerServiceFragment : Fragment() {

    val TAG = "BoundMessenger"

    private var _binding: FragmentBoundServiceBinding? = null
    private val binding get() = _binding!!

    private var boundService: Messenger? = null
    private var isBound: Boolean = false
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
        Intent(context, BoundMessengerCountingService::class.java)
            .also { intent -> context.bindService(intent, connection, Context.BIND_AUTO_CREATE) }
        super.onAttach(context)
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

        attachButtonListener()
    }

    private fun attachButtonListener() {
        binding.buttonStartService.setOnClickListener {
            if (isBound) {
                started = if (!started) {
                    boundService?.send(Message.obtain(null, MSG_START))
                    true
                } else {
                    boundService?.send(Message.obtain(null, MSG_STOP))
                    false
                }
            } else {
                Toast.makeText(activity, "No service connection", Toast.LENGTH_LONG).show()
            }
            updateButton()
        }
    }


    override fun onDestroy() {
        activity?.stopService(Intent(activity, StartedCountingService::class.java))
        if (isBound) activity?.unbindService(connection)
        isBound = false
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}