package com.caller.id.app.prototype.presentation.incoming

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adwardstark.mtextdrawable.MaterialTextDrawable
import com.caller.id.app.prototype.R
import com.caller.id.app.prototype.databinding.FragmentIncomingBinding
import com.caller.id.app.prototype.domain.models.Contact
import com.caller.id.app.prototype.presentation.base.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture

class IncomingFragment : BaseFragment<FragmentIncomingBinding>() {

    private var vibrationJob: Job? = null
    private var animationJob: Job? = null

    private val args: IncomingFragmentArgs by navArgs()

    override fun setupView() {
        setData(args.contact)
        animateDots(getString(R.string.is_calling))
        setUpListener()
    }

    private fun setUpListener() {
        binding.buttonEnd.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onResume() {
        super.onResume()
        startVibrationTask()
    }

    override fun onPause() {
        stopAllJobs()
        super.onPause()
    }

    private fun setData(contact: Contact) {
        binding.apply {
            textViewName.text = contact.name
            textViewNumber.text = contact.number
            MaterialTextDrawable.with(imageViewPhoto.context).text(contact.name)
                .into(imageViewPhoto)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun animateDots(baseText: String) {
        var dotCount = 0
        animationJob = viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                dotCount = (dotCount % 3) + 1
                val dots = ".".repeat(dotCount)
                binding.textViewCalling.text = "$baseText$dots"
                delay(500L)
            }
        }
    }

    private fun vibrate() {
        context?.let { ctx ->
            val vib = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                ctx.getSystemService(VIBRATOR_SERVICE) as Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val vibrationEffect =
                    VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
                vib.vibrate(vibrationEffect)
            } else {
                vib.vibrate(500)
            }
        }
    }

    private fun startVibrationTask() {
        vibrationJob = viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                vibrate()
                delay(800)
            }
        }
    }

    private fun stopAllJobs() {
        vibrationJob?.cancel()
        animationJob?.cancel()
    }
}