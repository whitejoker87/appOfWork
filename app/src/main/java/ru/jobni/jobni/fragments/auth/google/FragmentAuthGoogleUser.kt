package ru.jobni.jobni.fragments.auth.google

import android.app.Activity.RESULT_OK
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.common.api.ResultCallback
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.CAuthorizationUserGoogleBinding
import ru.jobni.jobni.viewmodel.AuthViewModel
import ru.jobni.jobni.viewmodel.MainViewModel


class FragmentAuthGoogleUser :
        Fragment(),
        ConnectionCallbacks,
        OnConnectionFailedListener,
        View.OnClickListener {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private val viewModelAuth: AuthViewModel by lazy {
        ViewModelProviders.of(activity!!).get(AuthViewModel::class.java)
    }

    private lateinit var binding: CAuthorizationUserGoogleBinding

    // A magic number we will use to know that our sign-in error
    // resolution activity has completed.
    private val OUR_REQUEST_CODE = 49404

    private val SIGNED_IN = 0
    private val STATE_SIGNING_IN = 1
    private val STATE_IN_PROGRESS = 2
    private val RC_SIGN_IN = 0

    private lateinit var mGoogleApiClient: GoogleApiClient
    private var mSignInProgress: Int = 0
    private lateinit var mSignInIntent: PendingIntent

    private lateinit var mSignInButton: SignInButton
    private lateinit var mSignOutButton: Button
    private lateinit var mRevokeButton: Button
    private lateinit var mStatus: TextView

    private lateinit var mConnectionResult: ConnectionResult

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.c_authorization_user_google, container, false)

        binding.lifecycleOwner = this

        val view = binding.root

        binding.viewmodelauth = viewModelAuth

        binding.viewmodelmain = viewModel

        // Get references to all of the UI views
        mSignInButton = binding.signInButton
        mSignOutButton = binding.signOutButton
        mRevokeButton = binding.revokeAccessButton
        mStatus = binding.statuslabel

        // Add click listeners for the buttons
        mSignInButton.setOnClickListener(this)
        mSignOutButton.setOnClickListener(this)
        mRevokeButton.setOnClickListener(this)

        // Build a GoogleApiClient
        mGoogleApiClient = buildGoogleApiClient()

        return view
    }

    private fun buildGoogleApiClient(): GoogleApiClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        return GoogleApiClient.Builder(activity as Context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(activity as FragmentActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient.connect()
    }

    override fun onStop() {
        mGoogleApiClient.disconnect()
        super.onStop()
    }

    override fun onConnected(connectionHint: Bundle?) {
        mSignInButton.isEnabled = false
        mSignOutButton.isEnabled = true
        mRevokeButton.isEnabled = true

        // Indicate that the sign in process is complete.
        mSignInProgress = SIGNED_IN
        val opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient)

        opr.setResultCallback(object : ResultCallback<GoogleSignInResult> {
            override fun onResult(@NonNull result: GoogleSignInResult) {
                if (result.isSuccess) {
                    try {
                        val account = result.signInAccount
                        mStatus.text = String.format("Signed In to My App as %s", account?.email)
                    } catch (ex: Exception) {
                        val exception = ex.localizedMessage
                        val exceptionString = ex.toString()
                        // Note that you should log these errors in a ‘real' app to aid in debugging
                    }
                }
            }
        })
    }

    // Если связь прервалась по не известным причинам, переподключение
    override fun onConnectionSuspended(cause: Int) {
        mGoogleApiClient.connect()
    }

    override fun onConnectionFailed(result: ConnectionResult) {
        if (mSignInProgress != STATE_IN_PROGRESS) {
            mSignInIntent = result.resolution!!
            if (mSignInProgress == STATE_SIGNING_IN) {
                resolveSignInError()
            }
        }
        // Will implement shortly
        onSignedOut()
    }

    fun resolveSignInError() {
        if (mSignInIntent != null) {
            try {
                mSignInProgress = STATE_IN_PROGRESS
                mConnectionResult.startResolutionForResult(activity, OUR_REQUEST_CODE)
            } catch (e: IntentSender.SendIntentException) {
                mSignInProgress = STATE_SIGNING_IN
                mGoogleApiClient.connect()
            }

        } else {
            // You have a play services error -- inform the user
        }
    }

    private fun onSignedOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback {
            // Update the UI to reflect that the user is signed out.
            mSignInButton.isEnabled = true
            mSignOutButton.isEnabled = false
            mRevokeButton.isEnabled = false
            mStatus.text = "Signed out"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RC_SIGN_IN -> {
                if (resultCode == RESULT_OK) {
                    mSignInProgress = STATE_SIGNING_IN
                } else {
                    mSignInProgress = SIGNED_IN
                }

                if (!mGoogleApiClient.isConnecting) {
                    mGoogleApiClient.connect()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        if (!mGoogleApiClient.isConnecting) {
            when (v?.id) {
                R.id.sign_in_button -> {
                    mStatus.text = "Signing In"
                    val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
                    startActivityForResult(signInIntent, RC_SIGN_IN)
                }
                R.id.sign_out_button -> {
                    onSignedOut()
                    mGoogleApiClient.disconnect()
                    mGoogleApiClient.connect()
                }
                R.id.revoke_access_button -> {
                    Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient)
                    mGoogleApiClient = buildGoogleApiClient()
                    mGoogleApiClient.connect()
                }
            }
        }
    }
}
