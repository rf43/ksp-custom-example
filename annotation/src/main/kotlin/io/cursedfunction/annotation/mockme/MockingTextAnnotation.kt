package io.cursedfunction.annotation.mockme

/**
 * This annotation is used to mark classes that should
 * be processed by the MockingTextProcessor
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class MockingText
