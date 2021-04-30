package com.ankitsingh.bestcard.util;

import androidx.biometric.BiometricPrompt;

public class BiometricAuthenticationUtil {

    public static BiometricPrompt.PromptInfo createSetupBiometricPromptInfoDialog() {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Setup biometric authentication")
                .setNegativeButtonText("Cancel")
                .setConfirmationRequired(true)
                .build();
    }

    public static BiometricPrompt.PromptInfo createBiometricAuthenticationPromptDialog() {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authenticate")
                .setNegativeButtonText("Cancel")
                .setConfirmationRequired(false)
                .build();
    }
}
