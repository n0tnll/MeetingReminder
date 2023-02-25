package com.shv.android.meetingreminder.domain.usecase.validation_usecase

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
