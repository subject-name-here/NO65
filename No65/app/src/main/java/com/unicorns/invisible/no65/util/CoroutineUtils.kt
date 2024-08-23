package com.unicorns.invisible.no65.util

import kotlinx.coroutines.*


fun launchCoroutine(block: suspend CoroutineScope.() -> Unit): Job {
    return launchCoroutineWithDispatcher(Dispatchers.Unconfined, block)
}

fun launchCoroutineOnDefault(block: suspend CoroutineScope.() -> Unit): Job {
    return launchCoroutineWithDispatcher(Dispatchers.Default, block)
}

fun launchCoroutineOnDefaultLazy(block: suspend CoroutineScope.() -> Unit): Job {
    return CoroutineScope(Dispatchers.Default).launch(start = CoroutineStart.LAZY) { block() }
}

fun launchCoroutineOnMain(block: suspend CoroutineScope.() -> Unit): Job {
    return launchCoroutineWithDispatcher(Dispatchers.Main, block)
}

fun launchCoroutineOnIO(block: suspend CoroutineScope.() -> Unit): Job {
    return launchCoroutineWithDispatcher(Dispatchers.IO, block)
}

private fun launchCoroutineWithDispatcher(
    dispatcher: CoroutineDispatcher,
    block: suspend CoroutineScope.() -> Unit
) : Job {
    return CoroutineScope(dispatcher).launch { block() }
}