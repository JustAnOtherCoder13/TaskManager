package com.picone.core.domain.entity

import com.picone.core.util.Constants
import java.util.*

abstract class BaseTask (
    open val name: String = Constants.UNKNOWN,
    open val description: String = Constants.UNKNOWN,
    open var start: Date?,
    open var deadLine: Date?,
    open var close: Date?
        )