package org.example.springfeatures

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<SpringFeaturesApplication>().with(TestcontainersConfiguration::class).run(*args)
}
